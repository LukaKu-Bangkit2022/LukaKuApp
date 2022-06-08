package com.bangkit.capstone.lukaku.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.adapters.ArticleAdapter
import com.bangkit.capstone.lukaku.databinding.FragmentArticlesBinding
import com.bangkit.capstone.lukaku.helper.Network
import com.bangkit.capstone.lukaku.utils.Constants.EXTRA_ARTICLE
import com.bangkit.capstone.lukaku.utils.onShimmer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticlesFragment : Fragment() {
    private var _binding: FragmentArticlesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ArticlesViewModel by viewModels()
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentArticlesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        onDetail()
        getAllArticle()
        setSearchView()
        backToActivity()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun backToActivity() {
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            shimmer.onShimmer()

            rvArticles.apply {
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
    }

    private fun getAllArticle() {
        lifecycleScope.launch {
            viewModel.getAllArticle().collect { result ->
                result.onSuccess { response ->
                    articleAdapter.differ.submitList(response.toList())
                    binding.apply {
                        shimmer.onShimmer(true)
                        rvArticles.visibility = VISIBLE
                    }
                }
                result.onFailure {
                    onFailRequest()
                }
            }
        }
    }

    private fun getSearchArticle(query: String) {
        lifecycleScope.launch {
            viewModel.searchArticle(query).collect { result ->
                result.onSuccess { response ->
                    binding.apply {
                        shimmer.onShimmer(true)
                        rvArticles.visibility = VISIBLE
                    }
                    if (response.isEmpty()) {
                        true.isArticleFound()
                    } else false.isArticleFound()

                    articleAdapter.differ.submitList(response.toList())
                }
                result.onFailure {
                    onFailRequest()
                }
            }
        }
    }

    private fun onFailRequest() {
        binding.apply {
            shimmer.onShimmer(true)
            false.isArticleFound()

            if (Network.isConnect(requireContext())) {
                inNetwork.root.visibility = VISIBLE
            } else inNetwork.root.visibility = VISIBLE

            inNetwork.btnRetry.setOnClickListener {
                shimmer.onShimmer()
                inNetwork.root.visibility = GONE

                if (searchView.query.isNotEmpty()) {
                    getSearchArticle(searchView.query.toString())
                } else getAllArticle()
            }
        }
    }

    private fun onDetail() {
        articleAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                requireActivity().intent.putExtra(EXTRA_ARTICLE, it)
            }

            findNavController().navigate(
                R.id.action_articlesFragment_to_detailArticleFragment,
                bundle
            )
        }
    }

    private fun Boolean.isArticleFound() = if (this) {
        binding.lottie.visibility = VISIBLE
    } else {
        binding.lottie.visibility = INVISIBLE
    }

    private fun setSearchView() {
        binding.searchView.setOnSearchClickListener {
            binding.apply {
                header.visibility = GONE
                ivBack.visibility = GONE
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                onStartQuery(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                onStartQuery(newText.toString())
                return true
            }
        })

        binding.searchView.setOnCloseListener {
            binding.apply {
                header.visibility = VISIBLE
                ivBack.visibility = VISIBLE
            }
            getAllArticle()
            false
        }
    }

    fun onStartQuery(query: String) {
        binding.apply {
            shimmer.onShimmer()
            inNetwork.root.visibility = GONE
            rvArticles.visibility = GONE
        }
        getSearchArticle(query)
    }
}