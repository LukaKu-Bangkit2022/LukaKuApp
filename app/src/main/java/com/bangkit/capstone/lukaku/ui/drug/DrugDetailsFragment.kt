package com.bangkit.capstone.lukaku.ui.drug

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bangkit.capstone.lukaku.data.models.MedicineResponseItem
import com.bangkit.capstone.lukaku.databinding.FragmentDrugDetailsBinding
import com.bangkit.capstone.lukaku.utils.loadImage
import com.bangkit.capstone.lukaku.utils.withCurrencyFormat
import com.bangkit.capstone.lukaku.utils.withFirstUpperCase

class DrugDetailsFragment : Fragment() {

    private var _binding: FragmentDrugDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var drugItem: MedicineResponseItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        drugItem = DrugDetailsFragmentArgs.fromBundle(arguments as Bundle).drugDetail
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDrugDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onSetItems()
    }

    private fun onSetItems() {
        binding.apply {
            ivImage.loadImage(drugItem.imageUrl, 8)
            tvContent.text = drugItem.label?.withFirstUpperCase()
            tvName.text = drugItem.name
            tvPiece.text = drugItem.price?.withCurrencyFormat()
            tvDescription.text = drugItem.description

            ivBack.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }
}