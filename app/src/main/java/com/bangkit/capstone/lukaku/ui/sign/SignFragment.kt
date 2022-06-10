package com.bangkit.capstone.lukaku.ui.sign

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.databinding.FragmentSignBinding
import com.bangkit.capstone.lukaku.utils.toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignFragment : Fragment() {
    private var _binding: FragmentSignBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var client: GoogleSignInClient
    private lateinit var dialog: Dialog

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    Log.w("TAG", "Google sign in failed", e)
                }
                dialog.show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        auth = Firebase.auth
        client = GoogleSignIn.getClient(requireActivity(), gso)

        initProgressDialog()

        binding.btnSignIn.setOnClickListener { signIn() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun signIn() {
        val signInIntent = client.signInIntent
        resultLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { task ->
                val user = auth.currentUser
                val message = if (task.additionalUserInfo!!.isNewUser) {
                    getString(R.string.account_created_success_message)
                } else {
                    getString(R.string.sign_in_welcome_message, user?.displayName)
                }

                requireActivity().toast(message)
                moveToMainActivity()
            }
            .addOnFailureListener {
                requireActivity().toast(getString(R.string.sign_in_failed_message))
                dialog.dismiss()
            }
    }

    private fun moveToMainActivity() {
        findNavController().navigate(R.id.action_signFragment_to_navigation_home)
        dialog.dismiss()
    }

    private fun initProgressDialog() {
        dialog = Dialog(requireActivity()).apply {
            setContentView(R.layout.dialog_loading)
            setCancelable(false)
        }
    }
}