package com.bangkit.capstone.lukaku.ui.profile.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.capstone.lukaku.adapters.DetectionAdapter
import com.bangkit.capstone.lukaku.databinding.FragmentHistoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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
            detectionAdapter = DetectionAdapter()
            adapter = detectionAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    private fun onSubscribe() {
        viewModel.apply {
            getDetectionSaved(auth.currentUser!!.uid).observe(viewLifecycleOwner) {
                detectionAdapter.submitList(it)
            }
        }
    }
}