package com.bangkit.capstone.lukaku.adapters

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.databinding.ItemListFirstAidBinding
import com.bangkit.capstone.lukaku.utils.Constants.COPY
import com.bangkit.capstone.lukaku.utils.toast

class FirstAidsAdapter(
    private val firstAidItems: List<String>
) : RecyclerView.Adapter<FirstAidsAdapter.FirstAidsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirstAidsViewHolder {
        val binding =
            ItemListFirstAidBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FirstAidsViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: FirstAidsViewHolder, position: Int) {
        holder.bind(firstAidItems[position], position)
    }

    override fun getItemCount(): Int = firstAidItems.size

    inner class FirstAidsViewHolder(
        private val binding: ItemListFirstAidBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String, position: Int) {
            val number = (position + 1).toString()

            binding.apply {
                tvNumber.text = number
                tvDescription.text = item

                ivCopy.setOnClickListener {
                    val clipboard = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText(COPY, item)
                    clipboard.setPrimaryClip(clip)

                    context.toast(context.getString(R.string.clipboard, number))
                }
            }
        }
    }
}