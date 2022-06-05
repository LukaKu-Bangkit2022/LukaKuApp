package com.bangkit.capstone.lukaku.ui.result.aid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.adapters.FirstAidsAdapter
import com.bangkit.capstone.lukaku.data.models.DetectionResult
import com.bangkit.capstone.lukaku.data.models.FirstAidResponse
import com.bangkit.capstone.lukaku.data.models.FirstAidResponseItem
import com.bangkit.capstone.lukaku.databinding.FragmentFirstAidBinding
import com.bangkit.capstone.lukaku.utils.Constants.ARG_RESULT
import com.bangkit.capstone.lukaku.utils.toast

class FirstAidFragment : Fragment() {

    private var _binding: FragmentFirstAidBinding? = null
    private val binding get() = _binding!!

    private var detectionResult: DetectionResult? = null
    private var firstAidResponse: FirstAidResponse? = null

    private lateinit var firstAidsAdapter: FirstAidsAdapter
    private lateinit var firstAidResponseItem: FirstAidResponseItem


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detectionResult = arguments?.getParcelable(ARG_RESULT)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFirstAidBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firstAidResponse = detectionResult?.firstAidResponse

        getFirstAids()
    }

    private fun getFirstAids() {
        if (firstAidResponse?.size != 0) {
            firstAidResponseItem = firstAidResponse!![0]

            val firstAidContent = firstAidResponseItem.firstaid
            val firstAidList = firstAidContent?.split("*")?.toList()

            if (!firstAidList.isNullOrEmpty()) {
                binding.rvFirstAids.apply {
                    firstAidsAdapter = FirstAidsAdapter(firstAidList)
                    adapter = firstAidsAdapter
                    layoutManager = LinearLayoutManager(requireActivity())
                }
            } else context?.toast(getString(R.string.first_aids_error_message))
        } else context?.toast(getString(R.string.first_aids_error_message))
    }
}