package com.bangkit.capstone.lukaku.ui.result

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.adapters.ResultPagerAdapter
import com.bangkit.capstone.lukaku.data.models.DetectionResult
import com.bangkit.capstone.lukaku.databinding.FragmentResultBinding
import com.bangkit.capstone.lukaku.utils.*
import com.google.android.material.tabs.TabLayoutMediator
import java.io.File

class ResultFragment : Fragment(), OnClickListener {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private var photo: File? = null
    private var detectionResult: DetectionResult? = null
    private val args: ResultFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photo = args.image
        detectionResult = args.resultParcelable
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
        setResult()
        resultPager()
        onSetListener()
    }

    private fun onSetListener() {
        binding.apply {
            ivBack.setOnClickListener(this@ResultFragment)
            fab.setOnClickListener(this@ResultFragment)
            fabSave.setOnClickListener(this@ResultFragment)
            fabReshoot.setOnClickListener(this@ResultFragment)
            fabFeedback.setOnClickListener(this@ResultFragment)
            fabBackground.setOnClickListener(this@ResultFragment)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivBack -> requireActivity().onBackPressed()
            R.id.fab -> onFABVisible()
            R.id.fab_save -> onSaveResult()
            R.id.fab_reshoot -> onNavigateReshoot()
            R.id.fab_feedback -> onShowFeedbackPopup()
            R.id.fab_background -> closeFABMenu()
        }
    }

    private fun onNavigateReshoot() {
        findNavController().navigate(
            R.id.action_resultFragment_to_navigation_detection
        )
    }

    private fun onShowFeedbackPopup() {
        context?.toast("Still under development!")
    }

    private fun onSaveResult() {
        context?.toast("Still under development!")
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
        }
    }

    private fun closeFABMenu() {
        binding.apply {
            fabBackground.visibility = GONE
            fab.animate().rotation(0F)
            fabLayout1.withAnimationY()
            fabLayout2.withAnimationY()
            fabLayout3.withAnimationY()
            fabLayout3.withAnimationY()
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animator: Animator) {}
                    override fun onAnimationEnd(animator: Animator) {
                        if (GONE == fabBackground.visibility) {
                            fabLayout1.visibility = GONE
                            fabLayout2.visibility = GONE
                            fabLayout3.visibility = GONE
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