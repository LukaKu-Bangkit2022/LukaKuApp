package com.bangkit.capstone.lukaku.ui.profile.bookmarks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.adapters.ArticleAdapter
import com.bangkit.capstone.lukaku.databinding.FragmentBookmarksBinding
import com.bangkit.capstone.lukaku.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarksFragment : Fragment() {
    private var _binding: FragmentBookmarksBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BookmarksViewModel by viewModels()
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        onDetail()
        getBookmarkedArticle()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getBookmarkedArticle() {
        viewModel.getBookmarkedArticle().observe(viewLifecycleOwner) { bookmarkedArticle ->
            if (bookmarkedArticle.isEmpty()) {
                binding.apply {
                    emptyMessage.text = getString(R.string.no_bookmarked_article)
                    emptyMessage.visibility = VISIBLE
                    rvArticles.visibility = GONE
                }
            } else {
                articleAdapter.differ.submitList(bookmarkedArticle)
                binding.emptyMessage.visibility = GONE
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvArticles.apply {
            articleAdapter = ArticleAdapter { articleEntity ->
                if (articleEntity.isBookmarked) {
                    viewModel.deleteArticle(articleEntity)
                } else {
                    viewModel.saveArticle(articleEntity)
                }
            }
            adapter = articleAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    private fun onDetail() {
        articleAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                requireActivity().intent.putExtra(Constants.EXTRA_ARTICLE, it)
            }

            findNavController().navigate(
                R.id.action_navigation_profile_to_detailArticleFragment,
                bundle
            )
        }
    }
}