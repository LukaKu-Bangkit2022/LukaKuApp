package com.bangkit.capstone.lukaku.ui.result.medicine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.capstone.lukaku.adapters.MedicineAdapter
import com.bangkit.capstone.lukaku.data.models.DetectionResult
import com.bangkit.capstone.lukaku.data.models.MedicineResponse
import com.bangkit.capstone.lukaku.databinding.FragmentMedicineBinding
import com.bangkit.capstone.lukaku.utils.Constants.ARG_RESULT

class MedicineFragment : Fragment() {

    private var _binding: FragmentMedicineBinding? = null
    private val binding get() = _binding!!

    private var detectionResult: DetectionResult? = null
    private var medicineResponse: MedicineResponse? = null

    private lateinit var medicineAdapter: MedicineAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detectionResult = arguments?.getParcelable(ARG_RESULT)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMedicineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        medicineResponse = detectionResult?.medicineResponse

        initRecyclerView()
        getMedicine()
    }

    private fun initRecyclerView() {
        binding.rvDrugs.apply {
            medicineAdapter = MedicineAdapter()
            adapter = medicineAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    private fun getMedicine() {
        if (medicineResponse?.size != 0) {
            medicineAdapter.differ.submitList(medicineResponse?.toList())
            binding.apply {
                tvLabelList.visibility = View.VISIBLE
                rvDrugs.visibility = View.VISIBLE
                emptyMessage.visibility = View.GONE
            }
        } else {
            binding.apply {
                tvLabelList.visibility = View.GONE
                rvDrugs.visibility = View.GONE
                emptyMessage.visibility = View.VISIBLE
            }
        }
    }
}