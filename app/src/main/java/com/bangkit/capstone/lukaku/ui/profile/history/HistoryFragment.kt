package com.bangkit.capstone.lukaku.ui.profile.history

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.adapters.DetectionAdapter
import com.bangkit.capstone.lukaku.data.local.entity.DetectionEntity
import com.bangkit.capstone.lukaku.databinding.FragmentHistoryBinding
import com.bangkit.capstone.lukaku.utils.toast
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private val viewModel: HistoryViewModel by viewModels()
    private lateinit var detectionAdapter: DetectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        initRecyclerView()
        onSubscribe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView() {
        binding.rvHistory.apply {
            detectionAdapter = DetectionAdapter(this@HistoryFragment::showPopup)
            adapter = detectionAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    private fun onSubscribe() {
        viewModel.apply {
            getDetectionSaved(auth.currentUser!!.uid).observe(viewLifecycleOwner) {
                if (it.isEmpty()) {
                    binding.apply {
                        emptyMessage.visibility = VISIBLE
                        rvHistory.visibility = GONE
                    }
                } else {
                    detectionAdapter.submitList(it)
                    binding.apply {
                        rvHistory.visibility = VISIBLE
                        emptyMessage.visibility = GONE
                    }
                }
            }
        }
    }

    private fun showPopup(detectionEntity: DetectionEntity, view: View) {
        val popup = PopupMenu(requireActivity(), view)
        val inflater: MenuInflater = popup.menuInflater

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popup.setForceShowIcon(true)
        }

        inflater.inflate(R.menu.popup_menu_more, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_delete_saved -> {
                    viewModel.deleteDetection(detectionEntity.id.toLong())
                }
                R.id.item_feedback -> {
                    requireActivity().toast(getString(R.string.still_under_development))
                }
            }
            true
        }
        popup.show()
    }
}