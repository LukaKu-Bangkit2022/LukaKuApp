package com.bangkit.capstone.lukaku.ui.hospital

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.data.models.HospitalBody
import com.bangkit.capstone.lukaku.databinding.FragmentHospitalBinding
import com.bangkit.capstone.lukaku.helper.ActivityLifeObserver
import com.bangkit.capstone.lukaku.utils.toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.ibrahimsn.lib.SmoothBottomBar

@AndroidEntryPoint
class HospitalFragment : Fragment() {
    private var _binding: FragmentHospitalBinding? = null
    private val binding get() = _binding
    private lateinit var bottomBar: SmoothBottomBar
    private val viewModel: HospitalViewModel by viewModels()
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        mMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isCompassEnabled = true
            isMapToolbarEnabled = true
        }

        onSetMapTheme()
        getLastLocation()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getLastLocation()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.lifecycle?.addObserver(ActivityLifeObserver {
            bottomBar = requireActivity().findViewById(R.id.bottomBar)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHospitalBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        handleOnBackPressed()
    }

    override fun onResume() {
        super.onResume()
        bottomBar.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        bottomBar.visibility = View.GONE
    }

    private fun handleOnBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_navigation_hospital_to_navigation_home)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (ContextCompat.checkSelfPermission(
                requireActivity().applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    mMap.addMarker(MarkerOptions().position(latLng).title(getString(R.string.your_location)).icon(vectorToBitmap(R.drawable.ic_my_location)))
                    markLocation(HospitalBody(location.latitude.toString(), location.longitude.toString()))
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
                } else {
                    requireActivity().toast(getString(R.string.location_error_message))
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    @SuppressLint("MissingPermission")
    private fun markLocation(hospitalBody: HospitalBody) {
        lifecycleScope.launch {
            viewModel.getAllNearestHospital(hospitalBody).collect { result ->
                result.onSuccess { response ->
                    response.forEach { responseItem ->
                        val latLng = LatLng(
                            responseItem.latitude?.toDouble() ?: 0.0,
                            responseItem.longitude?.toDouble() ?: 0.0
                        )
                        mMap.addMarker(
                            MarkerOptions().position(latLng).title(responseItem.nama)
                                .snippet(responseItem.place)
                        )
                    }
                }
                result.onFailure {
                    requireActivity().toast(getString(R.string.article_error_message))
                }
            }
        }
    }

    private fun vectorToBitmap(@DrawableRes id: Int): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)
        if (vectorDrawable == null) {
            Log.e("BitmapHelper", "Resource not found")
            return BitmapDescriptorFactory.defaultMarker()
        }
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(vectorDrawable, Color.BLUE)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun onSetMapTheme() {
        lifecycleScope.launch {
            viewModel.getThemeSetting().collect {
                setMapStyle(false, (it == true))
            }
        }

        binding?.tglStyle?.setOnCheckedChangeListener { _, isChecked ->
            setMapStyle(isChecked, false)
        }
    }

    private fun setMapStyle(isChecked: Boolean, isTheme: Boolean) {
        try {
            val success = if (isChecked || isTheme) {
                binding?.tglStyle?.isChecked = true
                mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        requireContext(),
                        R.raw.night_map_style
                    )
                )
            } else {
                mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        requireContext(),
                        R.raw.light_map_style
                    )
                )
            }

            if (!success) context?.toast(getString(R.string.error_style_parsing))
        } catch (exception: Resources.NotFoundException) {
            context?.toast(getString(R.string.error_style_find, exception.message))
        }
    }
}