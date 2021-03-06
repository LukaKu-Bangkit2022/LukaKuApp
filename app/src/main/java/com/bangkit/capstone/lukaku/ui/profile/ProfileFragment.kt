package com.bangkit.capstone.lukaku.ui.profile

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.adapters.LocalProfilePagerAdapter
import com.bangkit.capstone.lukaku.databinding.FragmentProfileBinding
import com.bangkit.capstone.lukaku.helper.ActivityLifeObserver
import com.bangkit.capstone.lukaku.utils.loadCircleImage
import com.bangkit.capstone.lukaku.utils.toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import me.ibrahimsn.lib.SmoothBottomBar

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var client: GoogleSignInClient
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
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gso = Builder(DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        auth = Firebase.auth
        client = GoogleSignIn.getClient(requireActivity(), gso)

        setProfile()
        localProfilePager()

        binding.ivSettings.setOnClickListener { showPopup(it) }

        handleOnBackPressed()
    }

    private fun handleOnBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_navigation_profile_to_navigation_home)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
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

    private fun setProfile() {
        val user = auth.currentUser
        binding.apply {
            ivProfile.loadCircleImage(user?.photoUrl)
            tvName.text = user?.displayName
            tvEmail.text = user?.email
        }
    }

    private fun showPopup(v: View) {
        val popup = PopupMenu(requireActivity(), v)
        val inflater: MenuInflater = popup.menuInflater

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popup.setForceShowIcon(true)
        }

        inflater.inflate(R.menu.popup_menu_profile, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.edit_profile -> {
                    requireActivity().toast(getString(R.string.still_under_development))
                }
                R.id.app_settings -> {
                    findNavController().navigate(R.id.action_navigation_profile_to_appSettingsFragment)
                }
                R.id.sign_out -> {
                    showSignOutDialog()
                }
            }
            true
        }
        popup.show()
    }

    private fun showSignOutDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_title_sign_out))
            .setMessage(getString(R.string.dialog_message_sign_out))
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.sign_out)) { _, _ ->
                signOut()
            }.show()
    }

    private fun signOut() {
        auth.signOut()
        client.signOut()

        findNavController().navigate(R.id.action_navigation_profile_to_signFragment)
    }

    private fun localProfilePager() {
        val localProfilePagerAdapter = LocalProfilePagerAdapter(requireActivity())
        binding.viewPager.adapter = localProfilePagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES_LOCAL_PROFILE[position])
        }.attach()
    }

    companion object {
        val TAB_TITLES_LOCAL_PROFILE = intArrayOf(
            R.string.history,
            R.string.bookmarks
        )
    }
}