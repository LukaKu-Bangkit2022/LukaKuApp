package com.bangkit.capstone.lukaku.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bangkit.capstone.lukaku.data.models.DetectionResult
import com.bangkit.capstone.lukaku.ui.result.ResultFragment.Companion.TAB_TITLES_RESULT
import com.bangkit.capstone.lukaku.ui.result.aid.FirstAidFragment
import com.bangkit.capstone.lukaku.ui.result.medicine.MedicineFragment
import com.bangkit.capstone.lukaku.utils.Constants.ARG_RESULT

class ResultPagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    var detectionResult: DetectionResult? = null

    override fun getItemCount(): Int = TAB_TITLES_RESULT.size

    override fun createFragment(position: Int): Fragment {
        var fragment = Fragment()

        when (position) {
            0 -> fragment = FirstAidFragment()
            1 -> fragment = MedicineFragment()
        }

        fragment.arguments = Bundle().apply {
            putParcelable(ARG_RESULT, detectionResult)
        }

        return fragment
    }
}