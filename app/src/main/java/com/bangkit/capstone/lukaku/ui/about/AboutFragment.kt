package com.bangkit.capstone.lukaku.ui.about

import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bangkit.capstone.lukaku.BuildConfig.VERSION_NAME
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.databinding.FragmentAboutBinding
import com.bangkit.capstone.lukaku.utils.Constants.GITHUB_US
import com.bangkit.capstone.lukaku.utils.Constants.LINKEDIN_US
import com.bangkit.capstone.lukaku.utils.Constants.MAIL_ADDRESS_US
import com.bangkit.capstone.lukaku.utils.Constants.MAIL_SUBJECT_US

class AboutFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val version = "v.$VERSION_NAME"
        binding.tvVersion.text = version

        onSetListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.iv_back -> requireActivity().onBackPressed()
            R.id.ib_email -> onSendMail()
            R.id.ib_github -> onOpenMedia(GITHUB_US)
            R.id.ib_linkedin -> onOpenMedia(LINKEDIN_US)
        }
    }

    private fun onSetListener() {
        binding.apply {
            ivBack.setOnClickListener(this@AboutFragment)
            ibEmail.setOnClickListener(this@AboutFragment)
            ibGithub.setOnClickListener(this@AboutFragment)
            ibLinkedin.setOnClickListener(this@AboutFragment)
        }
    }

    private fun onOpenMedia(linkUri: String) {
        val uri = Uri.parse(linkUri)
        val intent = Intent(ACTION_VIEW, uri)

        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(createChooser(intent, getString(R.string.title_open)))
        }
    }

    private fun onSendMail() {
        val uri = Uri.parse(MAIL_URI)
            .buildUpon()
            .appendQueryParameter(MAIL_TO, MAIL_ADDRESS_US)
            .appendQueryParameter(MAIL_SUBJECT, MAIL_SUBJECT_US)
            .build()

        val intent = Intent(ACTION_SENDTO, uri)

        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(createChooser(intent, getString(R.string.title_email)))
        }
    }

    companion object {
        private const val MAIL_URI = "mailto:"
        private const val MAIL_TO = "to"
        private const val MAIL_SUBJECT = "subject"
    }
}