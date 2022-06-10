package com.bangkit.capstone.lukaku.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.data.local.entity.DetectionEntity
import com.bangkit.capstone.lukaku.databinding.ItemListHistoryBinding
import com.bangkit.capstone.lukaku.databinding.ItemListHistoryBinding.inflate
import com.bangkit.capstone.lukaku.helper.withDateFormat
import com.bangkit.capstone.lukaku.helper.withFirstUpperCase
import com.bangkit.capstone.lukaku.utils.loadImage
import java.io.File

class DetectionAdapter(
    private val onMoreOptions: (DetectionEntity, View) -> Unit
) : ListAdapter<DetectionEntity, DetectionAdapter.ListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(parent.context, binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val saved = getItem(position)
        holder.bind(saved)

        val imageView = holder.binding.ivMore
        imageView.setOnClickListener { onMoreOptions(saved, it) }
    }

    class ListViewHolder(
        private val context: Context,
        val binding: ItemListHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(saved: DetectionEntity) {
            val detectionInfo = saved.detectionResult?.detectionResponse
            val firstAids = saved.detectionResult?.firstAidResponse
            val medicine = saved.detectionResult?.medicineResponse
            val imageFile = File(saved.photoPath!!)
            val isAvailable = if (firstAids?.size != 0) {
                context.getString(R.string.available)
            } else context.getString(R.string.no_available)

            binding.apply {
                tvDate.text = detectionInfo?.date?.withDateFormat()
                tvType.text = detectionInfo?.name?.withFirstUpperCase()
                tvFirstAids.text = context.getString(R.string.aid_available, isAvailable)
                tvMedicine.text = context.getString(R.string.drag_size, medicine?.size.toString())
                ivPhoto.loadImage(imageFile)
            }

            itemView.apply {
                setOnClickListener {
                    it.findNavController().navigate(
                        R.id.action_global_resultFragment,
                        bundleOf(
                            ID_PAIR to saved.id.toString(),
                            IMAGE_PAIR to imageFile,
                            RESULT_PAIR to saved.detectionResult
                        )
                    )
                }
            }
        }
    }

    companion object {
        private const val ID_PAIR = "id"
        private const val IMAGE_PAIR = "image"
        private const val RESULT_PAIR = "result_parcelable"

        val DIFF_CALLBACK: DiffUtil.ItemCallback<DetectionEntity> =
            object : DiffUtil.ItemCallback<DetectionEntity>() {
                override fun areItemsTheSame(
                    oldSaved: DetectionEntity,
                    saved: DetectionEntity
                ): Boolean {
                    return oldSaved.id == saved.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldSaved: DetectionEntity,
                    saved: DetectionEntity
                ): Boolean {
                    return oldSaved == saved
                }
            }
    }
}