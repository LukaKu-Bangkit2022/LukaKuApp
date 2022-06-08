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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.adapters.DetectionAdapter
import com.bangkit.capstone.lukaku.data.local.entity.DetectionEntity
import com.bangkit.capstone.lukaku.databinding.FragmentHistoryBinding
import com.bangkit.capstone.lukaku.utils.onShimmer
import com.bangkit.capstone.lukaku.utils.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


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
        binding.apply {
            shimmer.onShimmer()

            rvHistory.apply {
                detectionAdapter = DetectionAdapter(this@HistoryFragment::showPopup)
                adapter = detectionAdapter
                layoutManager = LinearLayoutManager(requireActivity())
            }
        }
    }

    private fun onSubscribe() {
        viewModel.apply {
            getDetectionSaved(auth.currentUser!!.uid).observe(viewLifecycleOwner) {
                if (it.isEmpty()) {
                    binding.apply {
                        shimmer.onShimmer(true)
                        inHistory.root.visibility = VISIBLE
                        rvHistory.visibility = GONE
                    }
                } else {
                    detectionAdapter.submitList(it)
                    binding.apply {
                        shimmer.onShimmer(true)
                        rvHistory.visibility = VISIBLE
                        inHistory.root.visibility = GONE
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
                    onShowDialog(detectionEntity)
                }
                R.id.item_feedback -> {
                    findNavController().navigate(R.id.action_global_feedbackFragment)
                }
            }
            true
        }
        popup.show()
    }

    private fun onShowDialog(id: DetectionEntity) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_title_deleted))
            .setMessage(getString(R.string.dialog_message_deleted))
            .setPositiveButton(getString(R.string.positive_btn_deleted)) { _, _ ->
                viewModel.deleteDetection(id.id.toLong())
                val isDelete = id.photoPath?.let { File(it).delete() }
                if (isDelete == true) context?.toast(getString(R.string.deleted))
            }
            .setNegativeButton(getString(R.string.negative_btn_deleted)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}