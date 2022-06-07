package com.bangkit.capstone.lukaku.ui.onboarding

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.databinding.FragmentOnboardingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OnboardingViewModel by viewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        checkUser()
        startAction()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startAction() {
        binding.btnJoin.setOnClickListener {
            viewModel.saveOnboarding(true)
        }
    }

    private fun onboardingFinished() {
        lifecycleScope.launch {
            viewModel.getOnboarding().collect {
                if (it == true) {
                    findNavController().navigate(R.id.action_onboardingFragment_to_signFragment)
                }
            }
        }
    }

    private fun checkUser() {
        val firebaseUser = auth.currentUser
        // Check if user logged in or not
        if (firebaseUser != null) {
            // User is already logged in
            findNavController().navigate(R.id.action_onboardingFragment_to_navigation_home)
        } else onboardingFinished()
    }
}

