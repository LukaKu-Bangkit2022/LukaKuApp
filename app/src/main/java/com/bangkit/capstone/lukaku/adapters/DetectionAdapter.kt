package com.bangkit.capstone.lukaku.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.data.local.entity.DetectionSaved
import com.bangkit.capstone.lukaku.databinding.ItemListHistoryBinding
import com.bangkit.capstone.lukaku.databinding.ItemListHistoryBinding.inflate
import com.bangkit.capstone.lukaku.helper.withDateFormat
import com.bangkit.capstone.lukaku.helper.withFirstUpperCase
import com.bangkit.capstone.lukaku.utils.loadImage
import java.io.File

class DetectionAdapter :
    ListAdapter<DetectionSaved, DetectionAdapter.ListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val saved = getItem(position)
        holder.bind(saved)
    }

    class ListViewHolder(
        private var binding: ItemListHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(saved: DetectionSaved) {
            val detectionInfo = saved.detectionResult?.detectionResponse
            val medicine = saved.detectionResult?.medicineResponse
            val imageFile = File(saved.photoPath!!)

            binding.apply {
                tvDate.text = detectionInfo?.date?.withDateFormat()
                tvType.text = detectionInfo?.name?.withFirstUpperCase()
                tvMedicineSize.text = medicine?.size.toString()
                ivPhoto.loadImage(imageFile)
            }

            itemView.apply {
                setOnClickListener {
                    it.findNavController().navigate(
                        R.id.action_global_resultFragment,
                        bundleOf(
                            "id" to saved.id.toString(),
                            "image" to imageFile,
                            "result_parcelable" to saved.detectionResult
                        )
                    )
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<DetectionSaved> =
            object : DiffUtil.ItemCallback<DetectionSaved>() {
                override fun areItemsTheSame(
                    oldSaved: DetectionSaved,
                    saved: DetectionSaved
                ): Boolean {
                    return oldSaved.id == saved.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldSaved: DetectionSaved,
                    saved: DetectionSaved
                ): Boolean {
                    return oldSaved == saved
                }
            }
    }
}