package com.bangkit.capstone.lukaku.ui.appsettings

import androidx.appcompat.app.AppCompatDelegate.*
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.databinding.FragmentAppSettingsBinding
import com.bangkit.capstone.lukaku.utils.action
import com.bangkit.capstone.lukaku.utils.snack
import com.bangkit.capstone.lukaku.utils.toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AppSettingsFragment : Fragment() {

    private var _binding: FragmentAppSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AppSettingsViewModel by viewModels()

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var location: Location? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                getLastLocation()
            }
            else -> {
                binding.root.snack(getString(R.string.location_error_message)) {
                    action(getString(R.string.settings)) {
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also { intent ->
                            val uri = Uri.fromParts("package", requireActivity().packageName, null)
                            intent.data = uri

                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                }
                binding.swLocation.isChecked = false
            }
        }
    }

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

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        setTheme()
        setLocation()
        backToActivity()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    private fun setLocation() {
        binding.swLocation.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveRealtimeLocation(isChecked)
        }

        lifecycleScope.launch {
            viewModel.getRealtimeLocation().collect { isLocationActive ->
                if (isLocationActive == true) {
                    binding.swLocation.isChecked = true
                    getLastLocation()
                } else {
                    binding.swLocation.isChecked = false
                    this@AppSettingsFragment.location = null
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (ContextCompat.checkSelfPermission(
                requireActivity().applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    this.location = location
                    Log.i("TAG", "getLastLocation: ${location.latitude}, ${location.longitude}")
                } else {
                    requireActivity().toast(getString(R.string.location_error_message))
                    binding.swLocation.isChecked = false
                }
            }
        } else {
            requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }

    private fun backToActivity() {
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}