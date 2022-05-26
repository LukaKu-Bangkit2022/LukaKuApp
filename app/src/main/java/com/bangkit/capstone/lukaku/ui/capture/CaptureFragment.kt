package com.bangkit.capstone.lukaku.ui.capture

import android.Manifest.permission.CAMERA
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.KeyEvent.KEYCODE_UNKNOWN
import android.view.KeyEvent.KEYCODE_VOLUME_DOWN
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.WindowManager.LayoutParams.ROTATION_ANIMATION_CROSSFADE
import android.widget.SeekBar
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.camera.core.*
import androidx.camera.core.CameraSelector.*
import androidx.camera.core.ImageCapture.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.databinding.FragmentCaptureBinding
import com.bangkit.capstone.lukaku.ui.capture.CaptureFragmentDirections.actionCaptureFragmentToViewerFragment
import com.bangkit.capstone.lukaku.utils.*
import com.bangkit.capstone.lukaku.utils.Constants.ANIMATION_FAST_MILLIS
import com.bangkit.capstone.lukaku.utils.Constants.ANIMATION_SLOW_MILLIS
import com.bangkit.capstone.lukaku.utils.Constants.DECIMAL_FORMAT
import com.bangkit.capstone.lukaku.utils.Constants.IMAGE_TYPE
import com.bangkit.capstone.lukaku.utils.Constants.KEY_EVENT_ACTION
import com.bangkit.capstone.lukaku.utils.Constants.KEY_EVENT_EXTRA
import java.io.File
import java.text.DecimalFormat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CaptureFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentCaptureBinding? = null
    private val binding get() = _binding!!

    private lateinit var broadcastManager: LocalBroadcastManager
    private lateinit var cameraExecutor: ExecutorService

    private var cameraProvider: ProcessCameraProvider? = null
    private var cameraControl: CameraControl? = null
    private var imageCapture: ImageCapture? = null
    private var cameraInfo: CameraInfo? = null
    private var lensFacing: Int = LENS_FACING_BACK
    private var preview: Preview? = null
    private var isFlash: Boolean = false
    private var camera: Camera? = null

    private val viewModel: CaptureViewModel by viewModels()

    private val requestPermissions = registerForActivityResult(
        RequestMultiplePermissions()
    ) {
        it.entries.forEach { permission ->
            when (permission.key) {
                CAMERA -> {
                    if (!permission.value) {
                        requireActivity().apply {
                            toast(getString(R.string.error_camera_permission))
                            onBackPressed()
                        }
                    }
                }
            }
        }
    }

    private val launcherGallery = registerForActivityResult(
        StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val imageUri: Uri = it.data?.data as Uri
            val imageFile = uriToFile(imageUri, requireContext())

            onNavigate(imageFile)
        }
    }

    private val volumeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getIntExtra(KEY_EVENT_EXTRA, KEYCODE_UNKNOWN)) {
                KEYCODE_VOLUME_DOWN -> {
                    binding.ibTakeImage.simulateClick()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCaptureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraExecutor = Executors.newSingleThreadExecutor()
        broadcastManager = LocalBroadcastManager.getInstance(view.context)

        val filter = IntentFilter().apply { addAction(KEY_EVENT_ACTION) }
        broadcastManager.registerReceiver(volumeReceiver, filter)

        if (savedInstanceState == null) {
            binding.viewFinder.post(this@CaptureFragment::startCamera)
        }

        onSubscribe()
        startListener()
    }

    override fun onResume() {
        super.onResume()
        setAnimation()
        behaviorSystemUI(true)

        requestPermissions.launch(REQUIRED_PERMISSIONS)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        behaviorSystemUI()

        _binding = null
        cameraExecutor.shutdown()
        broadcastManager.unregisterReceiver(volumeReceiver)
    }

    override fun onStop() {
        super.onStop()
        viewModel.apply {
            mutableLensFacing.value = lensFacing
            mutableIsFlash.value = isFlash
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ib_take_image -> takePhoto()
            R.id.ib_switch_camera -> swichCamera()
            R.id.ib_close -> requireActivity().onBackPressed()
            R.id.ib_flash -> swichFlash()
            R.id.ib_open_gallery -> startGallery()
        }
    }

    private fun onSubscribe() {
        viewModel.apply {
            getCameraProvider().observe(viewLifecycleOwner) {
                cameraProvider = it
            }

            getLensFacing().observe(viewLifecycleOwner) {
                lensFacing = it
                onBindCamera()
            }

            isFlash().observe(viewLifecycleOwner) {
                isFlash = it
                updateCameraFlash()
            }

            getCamera().observe(viewLifecycleOwner) {
                camera = it
                cameraInfo = camera?.cameraInfo
                cameraControl = camera?.cameraControl

                updateCameraFlash()
                zoomCamera()
            }
        }
    }

    private fun startListener() {
        binding.apply {
            ibTakeImage.setOnClickListener(this@CaptureFragment)
            ibSwitchCamera.setOnClickListener(this@CaptureFragment)
            ibClose.setOnClickListener(this@CaptureFragment)
            ibFlash.setOnClickListener(this@CaptureFragment)
            ibOpenGallery.setOnClickListener(this@CaptureFragment)
        }
    }

    private fun startGallery() {
        val intent = Intent().apply {
            action = Intent.ACTION_GET_CONTENT
            type = IMAGE_TYPE
        }

        val chooser = Intent.createChooser(intent, getString(R.string.title_gallery))
        launcherGallery.launch(chooser)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun startCamera() {
        ProcessCameraProvider.getInstance(requireContext()).let {
            cameraProvider = it.get()
            viewModel.mutableCameraProvider.value = cameraProvider

            it.addListener({
                lensFacing = when {
                    hasBackCamera() -> LENS_FACING_BACK
                    hasFrontCamera() -> LENS_FACING_FRONT
                    else -> throw IllegalStateException(
                        getString(R.string.error_camera_availability)
                    )
                }

                imageCapture?.flashMode = when {
                    isFlash -> FLASH_MODE_ON
                    else -> FLASH_MODE_OFF
                }

                updateCameraFlip()
                updateCameraFlash()
                onBindCamera()
            }, ContextCompat.getMainExecutor(requireContext()))
        }
    }

    private fun onBindCamera() {
        val screenAspectRatio = AspectRatio.RATIO_16_9
        val cameraProvider = cameraProvider
            ?: throw IllegalStateException(getString(R.string.error_camera_initialization))
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()

        preview = Preview.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(CAPTURE_MODE_MINIMIZE_LATENCY)
            .setTargetAspectRatio(screenAspectRatio)
            .build()

        cameraProvider.unbindAll()

        try {
            camera = cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageCapture
            )
            viewModel.mutableCamera.value = camera

        } catch (exc: Exception) {
            requireActivity().toast(getString(R.string.error_camera_start))
        }
    }

    private fun setAnimation() {
        requireActivity().window.attributes.rotationAnimation = ROTATION_ANIMATION_CROSSFADE
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun zoomCamera() {
        binding.seekBarZoom.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                cameraControl?.setLinearZoom(progress.toFloat() / 100)
                zoomControl()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        val listener = object : SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                val ratio = cameraInfo?.zoomState?.value?.zoomRatio as Float
                val scale = detector.scaleFactor
                cameraControl?.setZoomRatio(scale * ratio)
                zoomControl()
                return true
            }
        }

        val scaleGestureDetector = ScaleGestureDetector(context, listener)

        binding.viewFinder.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> return@setOnTouchListener true
                MotionEvent.ACTION_UP -> {
                    if (hasFlashCamera() && isFlash) {
                        binding.root.apply {
                            postDelayed({
                                cameraControl?.enableTorch(true)
                                postDelayed({
                                    cameraControl?.enableTorch(false)
                                }, ANIMATION_FAST_MILLIS)
                            }, ANIMATION_SLOW_MILLIS)
                        }
                    }

                    val factory = binding.viewFinder.meteringPointFactory
                    val point = factory.createPoint(event.x, event.y)
                    val action = FocusMeteringAction.Builder(point).build()
                    cameraControl?.startFocusAndMetering(action)

                    return@setOnTouchListener true
                }
                MotionEvent.ACTION_MOVE -> {
                    scaleGestureDetector.onTouchEvent(event)
                    return@setOnTouchListener true
                }
                else -> return@setOnTouchListener false
            }
        }

        zoomControl(true)
    }

    private fun zoomControl(isUpdate: Boolean = false) {
        val state = cameraInfo?.zoomState?.value
        val ratio = state?.zoomRatio as Float
        val liner = state.linearZoom
        val dFormat = DecimalFormat(DECIMAL_FORMAT)

        val zoomStatus =
            String.format(getString(R.string.zoom_status), dFormat.format(ratio))
        val visibility = if (ratio >= 1.01) View.VISIBLE else View.INVISIBLE

        if (isUpdate) camera?.cameraControl?.setZoomRatio(ratio)

        binding.apply {
            seekBarZoom.progress = (liner * 100).toInt()
            tvZoom.text = zoomStatus
            tvZoom.visibility = visibility
        }
    }

    private fun takePhoto() {
        imageCapture?.let {
            val imageFile = createFile(requireActivity().application)
            val metadata = Metadata().apply {
                isReversedHorizontal = lensFacing == LENS_FACING_FRONT
            }

            val outputOptions = OutputFileOptions.Builder(imageFile)
                .setMetadata(metadata)
                .build()

            it.takePicture(
                outputOptions, cameraExecutor, object : OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
                        requireActivity().toast(getString(R.string.error_camera_take))
                    }

                    override fun onImageSaved(output: OutputFileResults) {
                        onNavigate(imageFile)
                    }

                })

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.root.apply {
                    postDelayed({
                        foreground = ColorDrawable(Color.WHITE)
                        postDelayed({ foreground = null }, ANIMATION_FAST_MILLIS)
                    }, ANIMATION_SLOW_MILLIS)
                }
            }
        }
    }

    private fun onNavigate(imageFile: File) {
        lifecycleScope.launchWhenStarted {
            val directions = actionCaptureFragmentToViewerFragment(imageFile)
            findNavController().navigate(directions)
        }
    }

    private fun swichCamera() {
        lensFacing = if (LENS_FACING_FRONT == lensFacing) {
            LENS_FACING_BACK
        } else LENS_FACING_FRONT

        onBindCamera()
        updateCameraFlash()
        zoomControl()
    }

    private fun updateCameraFlip() {
        try {
            binding.ibSwitchCamera.isEnabled =
                hasBackCamera() && hasFrontCamera()
        } catch (exception: CameraInfoUnavailableException) {
            binding.ibSwitchCamera.isEnabled = false
        }
    }

    private fun swichFlash() {
        isFlash = !isFlash
        updateCameraFlash()
    }

    private fun updateCameraFlash() {
        imageCapture?.flashMode = when {
            isFlash -> FLASH_MODE_ON
            else -> FLASH_MODE_OFF
        }

        val iconFlash = if (isFlash) {
            R.drawable.ic_flash_on
        } else R.drawable.ic_flash_off

        try {
            binding.ibFlash.apply {
                setImageResource(iconFlash)
                isEnabled = hasFlashCamera()
                visibility = if (hasFlashCamera()) View.VISIBLE else View.INVISIBLE
            }
        } catch (exception: CameraInfoUnavailableException) {
            binding.ibFlash.apply {
                isEnabled = false
                visibility = View.INVISIBLE
            }
        }
    }

    private fun hasFlashCamera(): Boolean {
        return cameraInfo?.hasFlashUnit() ?: false
    }

    private fun hasBackCamera(): Boolean {
        return cameraProvider?.hasCamera(DEFAULT_BACK_CAMERA) ?: false
    }

    private fun hasFrontCamera(): Boolean {
        return cameraProvider?.hasCamera(DEFAULT_FRONT_CAMERA) ?: false
    }

    private fun behaviorSystemUI(isHide: Boolean = false) {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, !isHide)
        val controller = WindowInsetsControllerCompat(
            requireActivity().window,
            binding.root
        )
        val typesBar = WindowInsetsCompat.Type.systemBars()

        if (isHide) {
            controller.let {
                it.hide(typesBar)
                it.systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else controller.show(typesBar)
    }
}