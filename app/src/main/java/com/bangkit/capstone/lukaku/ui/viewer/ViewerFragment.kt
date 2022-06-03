package com.bangkit.capstone.lukaku.ui.viewer

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.databinding.FragmentViewerBinding
import com.bangkit.capstone.lukaku.ui.viewer.ViewerFragmentDirections.actionViewerFragmentToDetectionFragment
import com.bangkit.capstone.lukaku.utils.bitmapToFile
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

class ViewerFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentViewerBinding? = null
    private val binding get() = _binding!!

    private var imageFile: File? = null
    private val args: ViewerFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageFile = args.image
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentViewerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            ivSelectedImage.of(Uri.fromFile(imageFile))
                .withAspect(ASPECT_X, ASPECT_Y)
                .withOutputSize(IMAGE_WIDTH, IMAGE_HEIGHT)
                .initialize(context)

            tvReshoot.setOnClickListener(this@ViewerFragment)
            tvContinue.setOnClickListener(this@ViewerFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_continue -> onNavigateContinue()
            R.id.tv_reshoot -> onNavigateReshoot()
        }
    }

    private fun onNavigateReshoot() {
        findNavController().navigate(R.id.action_viewerFragment_to_captureFragment)
    }

    private fun onNavigateContinue() {
        lifecycleScope.launchWhenStarted {
            val directions = actionViewerFragmentToDetectionFragment().apply {
                image = bitmapToFile(binding.ivSelectedImage.output, requireContext())
            }

            findNavController().navigate(directions)
        }
    }

    companion object {
        private const val ASPECT_X = 1
        private const val ASPECT_Y = 1
        private const val IMAGE_WIDTH = 500
        private const val IMAGE_HEIGHT = 500
    }
}