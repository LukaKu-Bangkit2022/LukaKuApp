package com.bangkit.capstone.lukaku.ui.result

import android.animation.Animator
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2.*
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.adapters.ResultPagerAdapter
import com.bangkit.capstone.lukaku.data.local.entity.DetectionEntity
import com.bangkit.capstone.lukaku.data.models.DetectionResult
import com.bangkit.capstone.lukaku.databinding.FragmentResultBinding
import com.bangkit.capstone.lukaku.helper.withDateFormat
import com.bangkit.capstone.lukaku.helper.withFirstUpperCase
import com.bangkit.capstone.lukaku.utils.Constants.COPY
import com.bangkit.capstone.lukaku.utils.loadImage
import com.bangkit.capstone.lukaku.utils.toast
import com.bangkit.capstone.lukaku.utils.withAnimationY
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class ResultFragment : Fragment(), OnClickListener {

    private var _binding: FragmentResultBinding? = null
    val binding get() = _binding!!

    private var photo: File? = null
    private var detectionResult: DetectionResult? = null
    private val args: ResultFragmentArgs by navArgs()
    private val viewModel: ResultViewModel by viewModels()

    private var isSave: Boolean = false
    private var resultId: Long? = null
    private var index: Int = 0

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photo = args.image
        detectionResult = args.resultParcelable
        resultId = if (args.id.isNotEmpty()) args.id.toLong() else null
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

        auth = FirebaseAuth.getInstance()

        setResult()
        resultPager()
        onSetListener()
        onSubscribe()
    }

    override fun onStop() {
        super.onStop()

        closeFABMenu()
        viewModel.apply {
            mutableIsSave.value = isSave
            mutableResultId.value = resultId
        }
    }

    override fun onResume() {
        super.onResume()

        val stageSave = if (isSave || (resultId != null)) {
            isSave = true
            getString(R.string.title_delete)
        } else getString(R.string.title_save)

        binding.tvSave.text = stageSave
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.iv_back -> requireActivity().onBackPressed()
            R.id.fab -> onFABVisible()
            R.id.fab_save -> onSaveResult()
            R.id.fab_reshoot -> onNavigateReshoot()
            R.id.fab_feedback -> onShowFeedbackPopup()
            R.id.fab_copy_all -> copyAllFirstAids()
            R.id.fab_background -> closeFABMenu()
        }
    }

    private fun isHaveContent(): Boolean {
        return detectionResult?.firstAidResponse?.size != 0 && index == 0
    }

    private fun copyAllFirstAids() {
        if (isHaveContent()) {
            val firstAidResponseItem = detectionResult?.firstAidResponse!![0]
            val firstAidContent = firstAidResponseItem.firstaid

            val firstAidList = firstAidContent?.split("*")?.toList()?.joinToString(
                prefix = getString(R.string.prefix_first),
                separator = getString(R.string.separator_next),
                postfix = getString(R.string.postfix_by),
            )

            val clipboard =
                context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(COPY, firstAidList)
            clipboard.setPrimaryClip(clip)

            context?.toast(getString(R.string.clipboard_all))
        }
    }

    private fun onSetListener() {
        binding.apply {
            ivBack.setOnClickListener(this@ResultFragment)
            fab.setOnClickListener(this@ResultFragment)
            fabSave.setOnClickListener(this@ResultFragment)
            fabReshoot.setOnClickListener(this@ResultFragment)
            fabFeedback.setOnClickListener(this@ResultFragment)
            fabCopyAll.setOnClickListener(this@ResultFragment)
            fabBackground.setOnClickListener(this@ResultFragment)

            viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    index = position
                }
            })
        }
    }

    private fun onSubscribe() {
        viewModel.apply {
            getResultId().observe(viewLifecycleOwner) { resultId = it }

            isSave().observe(viewLifecycleOwner) { isSave = it }
        }
    }

    private fun onNavigateReshoot() {
        findNavController().navigate(
            R.id.action_resultFragment_to_navigation_detection
        )
    }

    private fun onShowFeedbackPopup() {
        context?.toast(getString(R.string.still_under_development))
    }

    private fun onSaveResult() {
        if (isSave) {
            lifecycleScope.launch {
                viewModel.deleteDetection(resultId!!)
                resultId = null
            }

            isSave = false
            binding.tvSave.text = getString(R.string.title_save)
        } else {
            val user = auth.currentUser
            val detectionSaved = DetectionEntity().also {
                it.uid = user?.uid
                it.photoPath = photo?.path
                it.detectionResult = detectionResult
            }

            lifecycleScope.launch {
                resultId = viewModel.insertDetection(detectionSaved)
            }

            isSave = true
            binding.tvSave.text = getString(R.string.title_delete)
        }
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

    private fun onFABVisible() {
        if (GONE == binding.fabBackground.visibility) {
            showFABMenu()
        } else closeFABMenu()
    }

    private fun showFABMenu() {
        binding.apply {
            fabLayout1.visibility = VISIBLE
            fabLayout2.visibility = VISIBLE
            fabLayout3.visibility = VISIBLE
            fabBackground.visibility = VISIBLE

            fab.animate().rotationBy(180F)
            fabLayout1.withAnimationY(-resources.getDimension(R.dimen.fab1))
            fabLayout2.withAnimationY(-resources.getDimension(R.dimen.fab2))
            fabLayout3.withAnimationY(-resources.getDimension(R.dimen.fab3))
            if (isHaveContent()) {
                fabLayout4.apply {
                    visibility = VISIBLE
                    withAnimationY(-resources.getDimension(R.dimen.fab4))
                }
            }
        }
    }

    private fun closeFABMenu() {
        binding.apply {
            fabBackground.visibility = GONE
            fab.animate().rotation(0F)
            fabLayout1.withAnimationY()
            fabLayout2.withAnimationY()
            if (isHaveContent()) fabLayout4.apply { withAnimationY() }
            fabLayout3.withAnimationY().setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    if (GONE == fabBackground.visibility) {
                        fabLayout1.visibility = GONE
                        fabLayout2.visibility = GONE
                        fabLayout3.visibility = GONE
                        fabLayout4.visibility = GONE
                        if (isHaveContent()) fabLayout4.apply { visibility = GONE }
                    }
                }

                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            })
        }
    }

    companion object {
        val TAB_TITLES_RESULT = intArrayOf(
            R.string.tab_first_aid,
            R.string.tab_medicine
        )
    }
}