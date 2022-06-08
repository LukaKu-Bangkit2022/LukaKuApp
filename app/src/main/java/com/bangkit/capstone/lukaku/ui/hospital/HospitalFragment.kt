package com.bangkit.capstone.lukaku.ui.hospital

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.databinding.FragmentHospitalBinding
import com.bangkit.capstone.lukaku.helper.ActivityLifeObserver
import com.bangkit.capstone.lukaku.utils.toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.ibrahimsn.lib.SmoothBottomBar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.*

@AndroidEntryPoint
class HospitalFragment : Fragment() {
    private var _binding: FragmentHospitalBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomBar: SmoothBottomBar
    private val viewModel: HospitalViewModel by viewModels()
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var dialog: Dialog

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        mMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isCompassEnabled = true
            isMapToolbarEnabled = true
        }
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
    ): View {
        _binding = FragmentHospitalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        initProgressDialog()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
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
                    mMap.addMarker(
                        MarkerOptions().position(latLng).title(getString(R.string.your_location))
                            .icon(
                                vectorToBitmap(
                                    R.drawable.ic_emoji_emotions,
                                    Color.parseColor("#FBBA38")
                                )
                            )
                    )

                    val jsonObject = JSONObject()
                    jsonObject.put("latitude", location.latitude.toString())
                    jsonObject.put("longitude", location.longitude.toString())
                    val requestBody =
                        jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())

                    markLocation(requestBody)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
                } else {
                    requireActivity().toast(getString(R.string.location_error_message))
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    @SuppressLint("MissingPermission")
    private fun markLocation(requestBody: RequestBody) {
        dialog.show()
        lifecycleScope.launch {
            viewModel.getAllNearestHospital(requestBody).collect { result ->
                result.onSuccess { response ->
                    response.forEach { responseItem ->
                        val latLng =
                            LatLng(
                                responseItem.latitude.toDouble(),
                                responseItem.longitude.toDouble()
                            )
                        mMap.addMarker(
                            MarkerOptions().position(latLng)
                                .title(responseItem.nama)
                                .snippet(
                                    getCityName(
                                        responseItem.latitude.toDouble(),
                                        responseItem.longitude.toDouble()
                                    ) + " " + getCountryName(
                                        responseItem.latitude.toDouble(),
                                        responseItem.longitude.toDouble()
                                    )
                                )
                        )
                    }
                    dialog.dismiss()
                }
                result.onFailure {
                    requireActivity().toast(getString(R.string.article_error_message))
                    dialog.dismiss()
                }
            }
        }
    }

    private fun vectorToBitmap(@DrawableRes id: Int, @ColorInt color: Int): BitmapDescriptor {
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
        DrawableCompat.setTint(vectorDrawable, color)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun initProgressDialog() {
        dialog = Dialog(requireActivity()).apply {
            setContentView(R.layout.dialog_loading)
            setCancelable(false)
        }
    }

    private fun getCityName(lat: Double, lon: Double): String {
        val cityName: String
        val geoCoder = Geocoder(requireActivity(), Locale.getDefault())
        val address = geoCoder.getFromLocation(lat, lon, 1)
        cityName = address[0]?.subAdminArea ?: ""

        return cityName
    }

    private fun getCountryName(lat: Double, lon: Double): String {
        val countryName: String
        val geoCoder = Geocoder(requireActivity(), Locale.getDefault())
        val address = geoCoder.getFromLocation(lat, lon, 1)
        countryName = address[0]?.countryName ?: ""
        return countryName
    }
}