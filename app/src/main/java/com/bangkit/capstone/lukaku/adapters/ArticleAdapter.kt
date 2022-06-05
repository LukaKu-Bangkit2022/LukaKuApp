package com.bangkit.capstone.lukaku.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.data.local.entity.ArticleEntity
import com.bangkit.capstone.lukaku.databinding.ItemListArticleBinding
import com.bangkit.capstone.lukaku.adapters.ArticleAdapter.ArticleViewHolder
import com.bangkit.capstone.lukaku.utils.loadImage
import com.bangkit.capstone.lukaku.utils.toast

class ArticleAdapter(private val onBookmarkClick: (ArticleEntity) -> Unit) :
    ListAdapter<ArticleEntity, ArticleViewHolder>(DIFF_CALLBACK) {

    val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding =
            ItemListArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(parent.context, binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.bind(article)

        val ivBookmark = holder.binding.ivBookmark
        isBookmarked(article, ivBookmark)

        ivBookmark.setOnClickListener {
            onBookmarkClick(article)
            if (article.isBookmarked) {
                ivBookmark.setImageDrawable(
                    ContextCompat.getDrawable(
                        ivBookmark.context,
                        R.drawable.ic_bookmarked
                    )
                )
                holder.itemView.context.toast(holder.itemView.resources.getString(R.string.article_bookmarked))
            } else {
                ivBookmark.setImageDrawable(
                    ContextCompat.getDrawable(
                        ivBookmark.context,
                        R.drawable.ic_bookmark
                    )
                )
                holder.itemView.context.toast(holder.itemView.resources.getString(R.string.article_removed_bookmark))
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ArticleViewHolder(
        private val context: Context,
        val binding: ItemListArticleBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ArticleEntity) {
            binding.apply {
                if (item.category == "Kesehatan" || item.category == "Health") {
                    tvContent.background.setTint(ContextCompat.getColor(context, R.color.pink))
                } else {
                    tvContent.background.setTint(ContextCompat.getColor(context, R.color.yellow))
                }
                tvContent.text = item.category

                ivPhoto.loadImage(item.imageUrl)
                tvTitle.text = item.title
                tvDate.text = context.getString(R.string.line, item.publishedAt)
                tvAuthor.text = item.author
                tvDescription.text = item.definition_description

                itemView.setOnClickListener {
                    onItemClickListener?.let {
                        it(item)
                    }
                }
            }
        }
    }

    private var onItemClickListener: ((ArticleEntity) -> Unit)? = null

    fun setOnItemClickListener(listener: (ArticleEntity) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ArticleEntity> =
            object : DiffUtil.ItemCallback<ArticleEntity>() {
                override fun areItemsTheSame(
                    oldItem: ArticleEntity,
                    newItem: ArticleEntity
                ): Boolean = oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: ArticleEntity,
                    newItem: ArticleEntity
                ): Boolean = oldItem == newItem

            }
    }

    private fun isBookmarked(article: ArticleEntity, ivBookmark: ImageView) {
        if (article.isBookmarked) {
            ivBookmark.setImageDrawable(
                ContextCompat.getDrawable(
                    ivBookmark.context,
                    R.drawable.ic_bookmarked
                )
            )
        } else {
            ivBookmark.setImageDrawable(
                ContextCompat.getDrawable(
                    ivBookmark.context,
                    R.drawable.ic_bookmark
                )
            )
        }
    }
}