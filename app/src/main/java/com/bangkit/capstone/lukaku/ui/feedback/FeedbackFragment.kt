package com.bangkit.capstone.lukaku.ui.feedback

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.data.models.Feedback
import com.bangkit.capstone.lukaku.databinding.FragmentFeedbackBinding
import com.bangkit.capstone.lukaku.utils.Constants.RESPOND_OK
import com.bangkit.capstone.lukaku.utils.loadImage
import com.bangkit.capstone.lukaku.utils.toast
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FeedbackFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentFeedbackBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FeedbackViewModel by viewModels()
    private var isDraft: Boolean = false

    private lateinit var auth: FirebaseAuth
    private lateinit var name: String
    private lateinit var email: String

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (isDraft) {
                AlertDialog.Builder(requireContext()).apply {
                    setMessage(requireActivity().getString(R.string.dialog_feedback))
                    setCancelable(false)
                    setPositiveButton(getString(R.string.positive_btn_feedback)) { _, _ ->
                        isEnabled = false
                        requireActivity().onBackPressed()
                    }
                    setNegativeButton(getString(R.string.negative_btn_feedback)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    create()
                    show()
                }
            } else {
                isEnabled = false
                requireActivity().onBackPressed()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFeedbackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        val user = auth.currentUser
        val image = user?.photoUrl
        name = user?.displayName.toString()
        email = user?.email.toString()

        binding.apply {
            imageView3.loadImage(image, 100)
            tvName.text = name
            tvEmail.text = email
        }

        onSetListener()
    }

    private fun onSetListener() {
        binding.apply {
            tvSend.setOnClickListener(this@FeedbackFragment)
            ivClose.setOnClickListener(this@FeedbackFragment)

            tvFeedback.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    onSetTextChange(p0)
                }

                override fun afterTextChanged(p0: Editable?) {
                    onSetTextChange(p0)
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tv_send -> onSendFeedback()
            R.id.iv_close -> requireActivity().onBackPressed()
        }
    }

    private fun onSetTextChange(char: CharSequence?) {
        isDraft = char?.isEmpty() != true

        binding.apply {
            tvSend.isEnabled = isDraft
            tvCounter.text = getString(R.string.counter, char?.length.toString())
        }
    }

    private fun onSendFeedback() {
        val text = binding.tvFeedback.text.toString()
        val feedback = Feedback(name, email, text)

        lifecycleScope.launch {
            viewModel.insertFeedback(feedback).observe(viewLifecycleOwner) { result ->
                result.onSuccess {
                    if (it.result.equals(RESPOND_OK)) {
                        requireActivity().onBackPressed()
                        context?.toast(getString(R.string.feedback_success))
                    }
                }

                result.onFailure {
                    context?.toast(getString(R.string.feedback_error))
                }
            }
        }
    }
}