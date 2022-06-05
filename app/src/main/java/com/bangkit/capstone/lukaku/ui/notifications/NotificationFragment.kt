package com.bangkit.capstone.lukaku.ui.notifications

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.adapters.NotificationPagerAdapter
import com.bangkit.capstone.lukaku.databinding.FragmentNotificationBinding
import com.google.android.material.tabs.TabLayoutMediator

class NotificationFragment : Fragment() {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sectionPager()
        backToActivity()
    }

    private fun sectionPager() {
        val notificationPagerAdapter = NotificationPagerAdapter(requireActivity())
        binding.viewPager.adapter = notificationPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES_NOTIFICATION[position])
        }.attach()
    }

    private fun backToActivity() {
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    companion object {
        @StringRes
        val TAB_TITLES_NOTIFICATION = intArrayOf(
            R.string.updates,
            R.string.messages
        )
    }
}