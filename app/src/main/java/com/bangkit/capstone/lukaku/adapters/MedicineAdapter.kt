package com.bangkit.capstone.lukaku.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.data.models.MedicineResponseItem
import com.bangkit.capstone.lukaku.databinding.ItemListDrugBinding
import com.bangkit.capstone.lukaku.databinding.ItemListDrugBinding.inflate
import com.bangkit.capstone.lukaku.utils.loadImage

class MedicineAdapter : RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder>() {
    private val callback = object : DiffUtil.ItemCallback<MedicineResponseItem>() {
        override fun areItemsTheSame(
            oldItem: MedicineResponseItem,
            newItem: MedicineResponseItem
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: MedicineResponseItem,
            newItem: MedicineResponseItem
        ): Boolean = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val binding = inflate(LayoutInflater.from(parent.context), parent, false)
        return MedicineViewHolder(parent.context, binding)
    }

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class MedicineViewHolder(
        private val context: Context,
        private val binding: ItemListDrugBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MedicineResponseItem) {
            binding.apply {
                tvContent.text = item.label
                ivImage.loadImage(item.imageUrl)
                tvName.text = item.name
                tvPiece.text = context.getString(R.string.piece, item.price)
                tvDescription.text = item.description
                tvDescription.setOnClickListener {
                    it.findNavController().navigate(
                        R.id.action_global_drugDetailsFragment,
                        bundleOf("drug_detail" to item)
                    )
                }
            }
        }
    }
}