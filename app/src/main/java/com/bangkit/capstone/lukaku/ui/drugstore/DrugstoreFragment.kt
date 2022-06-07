package com.bangkit.capstone.lukaku.ui.drugstore

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.databinding.FragmentDrugstoreBinding
import com.bangkit.capstone.lukaku.helper.ActivityLifeObserver
import com.bangkit.capstone.lukaku.utils.toast
import me.ibrahimsn.lib.SmoothBottomBar

class DrugstoreFragment : Fragment() {

    private var _binding: FragmentDrugstoreBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomBar: SmoothBottomBar

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.lifecycle?.addObserver(ActivityLifeObserver {
            bottomBar = requireActivity().findViewById(R.id.bottomBar)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDrugstoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val maintenanceOf = getString(R.string.title_drugstore)
        val message = getString(R.string.maintenance_message, maintenanceOf)

        binding.inMaintenance.apply {
            tvMessage.text = message
            btnFeedback.setOnClickListener {
                onShowFeedback()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        bottomBar.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bottomBar.visibility = View.GONE
        _binding = null
    }

    private fun onShowFeedback() {
        context?.toast(getString(R.string.still_under_development))
    }
}