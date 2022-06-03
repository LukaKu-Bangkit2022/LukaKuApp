package com.bangkit.capstone.lukaku.ui.result

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.adapters.ResultPagerAdapter
import com.bangkit.capstone.lukaku.data.models.DetectionResult
import com.bangkit.capstone.lukaku.databinding.FragmentResultBinding
import com.bangkit.capstone.lukaku.utils.loadImage
import com.bangkit.capstone.lukaku.utils.withDateFormat
import com.bangkit.capstone.lukaku.utils.withFirstUpperCase
import com.google.android.material.tabs.TabLayoutMediator

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private var photo: Bitmap? = null
    private var detectionResult: DetectionResult? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photo = ResultFragmentArgs.fromBundle(arguments as Bundle).image
        detectionResult = ResultFragmentArgs.fromBundle(arguments as Bundle)
            .resultParcelable as DetectionResult
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setResult()
        resultPager()
        backToActivity()
    }

    private fun setResult() {
        binding.apply {
            ivPhoto.loadImage(photo)

            detectionResult?.apply {
                tvDateValue.text = detectionResponse?.date?.withDateFormat()
                tvTypeValue.text = detectionResponse?.name?.withFirstUpperCase()
            }
        }
    }

    private fun resultPager() {
        val sectionsPagerAdapter = ResultPagerAdapter(requireActivity())
        sectionsPagerAdapter.detectionResult = detectionResult

        binding.apply {
            viewPager.adapter = sectionsPagerAdapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES_RESULT[position])
            }.attach()
        }
    }

    private fun backToActivity() {
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    companion object {
        val TAB_TITLES_RESULT = intArrayOf(
            R.string.tab_first_aid,
            R.string.tab_medicine
        )
    }
}