package com.bangkit.capstone.lukaku.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.data.models.ArticleResponseItem
import com.bangkit.capstone.lukaku.databinding.ItemListArticleBinding
import com.bangkit.capstone.lukaku.utils.loadImage

class ArticleAdapter : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {
    private val callback = object : DiffUtil.ItemCallback<ArticleResponseItem>() {
        override fun areItemsTheSame(
            oldItem: ArticleResponseItem,
            newItem: ArticleResponseItem
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: ArticleResponseItem,
            newItem: ArticleResponseItem
        ): Boolean = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding =
            ItemListArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(parent.context, binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ArticleViewHolder(
        private val context: Context,
        private val binding: ItemListArticleBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ArticleResponseItem) {
            binding.apply {
                tvContent.text = item.category
                ivPhoto.loadImage(item.imageUrl)
                tvTitle.text = item.definition
                tvDate.text = context.getString(R.string.line, item.publishedAt)
                tvAuthor.text = item.author
                tvDescription.text = item.definition_description
            }
        }
    }
}