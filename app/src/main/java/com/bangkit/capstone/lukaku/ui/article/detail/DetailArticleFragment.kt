package com.bangkit.capstone.lukaku.ui.article.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.data.local.entity.ArticleEntity
import com.bangkit.capstone.lukaku.databinding.FragmentDetailArticleBinding
import com.bangkit.capstone.lukaku.utils.Constants.EXTRA_ARTICLE
import com.bangkit.capstone.lukaku.utils.loadImage

class DetailArticleFragment : Fragment() {
    private var _binding: FragmentDetailArticleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val item =
            requireActivity().intent.getParcelableExtra<ArticleEntity>(EXTRA_ARTICLE) as ArticleEntity

        catchArticleData(item)
        backToActivity()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun backToActivity() {
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun catchArticleData(item: ArticleEntity) {
        binding.apply {
            ivPhoto.loadImage(item.imageUrl)
            tvTitle.text = item.title
            if (item.category == "Kesehatan" || item.category == "Health") {
                tvContent.background.setTint(ContextCompat.getColor(requireContext(), R.color.pink))
            } else {
                tvContent.background.setTint(ContextCompat.getColor(requireContext(), R.color.yellow))
            }
            tvContent.text = item.category
            tvDate.text = getString(R.string.line, item.publishedAt)
            tvAuthor.text = item.author
            tvDefinition.text = item.definition_title
            tvDefinitionDescription.text = item.definition_description
            tvSymptom.text = item.symptom_title
            tvSymptomDescription.text = item.symptom_description
            tvTreatment.text = item.treatment_title
            tvTreatmentDescription.text = item.treatment_description
        }
    }
}