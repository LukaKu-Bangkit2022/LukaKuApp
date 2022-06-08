package com.bangkit.capstone.lukaku.ui.appsettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.databinding.FragmentAppSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AppSettingsFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentAppSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AppSettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAppSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTheme()
        onSetListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.iv_back -> {
                requireActivity().onBackPressed()
            }
            R.id.tv_feedback -> {
                findNavController().navigate(R.id.action_global_feedbackFragment)
            }
            R.id.tv_about -> {
                findNavController().navigate(R.id.action_appSettingsFragment_to_aboutFragment)
            }
        }
    }

    private fun onSetListener() {
        binding.apply {
            ivBack.setOnClickListener(this@AppSettingsFragment)
            tvFeedback.setOnClickListener(this@AppSettingsFragment)
            tvAbout.setOnClickListener(this@AppSettingsFragment)
        }
    }

    private fun setTheme() {
        binding.swTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveThemeSetting(isChecked)
        }

        lifecycleScope.launch {
            viewModel.getThemeSetting().collect { isDarkModeActive ->
                val isThemeChecked: Boolean
                val theme = if (isDarkModeActive == true) {
                    isThemeChecked = true
                    MODE_NIGHT_YES
                } else {
                    isThemeChecked = false
                    MODE_NIGHT_NO
                }
                setDefaultNightMode(theme)
                binding.swTheme.isChecked = isThemeChecked
            }
        }
    }
}