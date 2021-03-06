package com.bangkit.capstone.lukaku.ui.detection

import android.app.AlertDialog.Builder
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.data.models.DetectionResult
import com.bangkit.capstone.lukaku.databinding.FragmentDetectionBinding
import com.bangkit.capstone.lukaku.helper.Network
import com.bangkit.capstone.lukaku.helper.withFirstUpperCase
import com.bangkit.capstone.lukaku.ui.detection.DetectionFragmentDirections.actionDetectionFragmentToResultFragment
import com.bangkit.capstone.lukaku.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@AndroidEntryPoint
class DetectionFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentDetectionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetectionViewModel by viewModels()
    private val args: DetectionFragmentArgs by navArgs()
    private var photo: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photo = args.image
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            ivSelectedImage.loadImage(photo, 30)
            lottieFinger.setOnClickListener(this@DetectionFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.lottie_finger -> {
                startDetection()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun showDialog() {
        Builder(requireContext()).apply {
            setMessage(requireActivity().getString(R.string.dialog_process))
            setCancelable(false)
            setPositiveButton(getString(R.string.positive_btn_detection)) { _, _ ->
                findNavController().navigate(R.id.action_detectionFragment_to_navigation_detection)
            }
            setNegativeButton(getString(R.string.negative_btn_detection)) { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }

    private fun startDetection() {
        binding.apply {
            lottieFinger.apply {
                playAnimation()
                isClickable = false
            }
            lottieDetect.playAnimation()
            tvStatus.text = getString(R.string.status_detection)
        }

        val photoFilePart = photo?.let {
            MultipartBody.Part.createFormData(
                "file",
                photo?.name,
                it.asRequestBody("image/*".toMediaTypeOrNull())
            )
        }

        lifecycleScope.launch {
            if (photoFilePart != null) {
                viewModel.detection(photoFilePart).observe(viewLifecycleOwner) { result ->
                    var message = ""

                    result.onSuccess {
                        binding.tvStatus.text = getString(R.string.status_detection_complete)

                        val resultInfo = result.getOrNull()?.detectionResponse
                        if (resultInfo != null) {
                            onNavigateResult(result.getOrNull())
                        } else message = getString(R.string.no_result_message)
                    }

                    result.onFailure {
                        binding.apply {
                            lottieFinger.apply {
                                pauseAnimation()
                                frame = 0
                                isClickable = true
                            }
                            lottieDetect.apply {
                                pauseAnimation()
                                frame = 0
                            }
                        }

                        message = if (Network.isConnect(requireContext())) {
                            getString(R.string.failed_message)
                        } else getString(R.string.failed_message)
                    }

                    if (message.isNotEmpty()) {
                        binding.tvStatus.text = message.withFirstUpperCase()
                    }
                }
            }
        }
    }

    private fun onNavigateResult(detectionResult: DetectionResult?) {
        lifecycleScope.launchWhenStarted {
            val directions = actionDetectionFragmentToResultFragment().apply {
                image = photo
                resultParcelable = detectionResult
            }

            findNavController().navigate(directions)
        }
    }
}