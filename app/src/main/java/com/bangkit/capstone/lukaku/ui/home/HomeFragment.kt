package com.bangkit.capstone.lukaku.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.adapters.ArticleAdapter
import com.bangkit.capstone.lukaku.adapters.HeadlineAdapter
import com.bangkit.capstone.lukaku.data.resources.HeadlineData
import com.bangkit.capstone.lukaku.databinding.FragmentHomeBinding
import com.bangkit.capstone.lukaku.helper.ActivityLifeObserver
import com.bangkit.capstone.lukaku.utils.Constants.EXTRA_ARTICLE
import com.bangkit.capstone.lukaku.utils.Constants.INTERVAL
import com.bangkit.capstone.lukaku.utils.ViewPager.autoScroll
import com.bangkit.capstone.lukaku.utils.ViewPager.mediator
import com.bangkit.capstone.lukaku.utils.ViewPager.transformer
import com.bangkit.capstone.lukaku.utils.loadCircleImage
import com.bangkit.capstone.lukaku.utils.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.ibrahimsn.lib.SmoothBottomBar

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var bottomBar: SmoothBottomBar
    private lateinit var auth: FirebaseAuth
    private lateinit var articleAdapter: ArticleAdapter

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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        setProfile()
        onStartHeadline()
        goToViewMore()
        goToNotifications()
        goToProfile()
        initRecyclerView()
        onDetail()
        getAllArticle()
    }

    override fun onResume() {
        super.onResume()
        bottomBar.visibility = VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        bottomBar.visibility = View.GONE
    }

    private fun setProfile() {
        val user = auth.currentUser
        binding.apply {
            ivProfile.loadCircleImage(user?.photoUrl)
            tvName.text = getString(R.string.name_display, user?.displayName)
        }
    }

    private fun onStartHeadline() {
        val headlines = HeadlineData.getHeadlines()
        val headlineAdapter = HeadlineAdapter(headlines)

        val title = mutableListOf<String>()
        for (titleItem in headlines) {
            title.add(titleItem.title.toString())
        }

        binding.vpHeadline.apply {
            adapter = headlineAdapter
            transformer()
            autoScroll(viewLifecycleOwner.lifecycleScope, INTERVAL)
            mediator(binding.tabLayout, title)
        }
    }

    private fun getAllArticle() {
        true.showLoading()
        lifecycleScope.launch {
            viewModel.getAllArticle().collect { result ->
                result.onSuccess { response ->
                    articleAdapter.differ.submitList(response.take(6).toList())
                    false.showLoading()
                }
                result.onFailure {
                    requireActivity().toast(getString(R.string.article_error_message))
                    false.showLoading()
                }
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

    private fun goToViewMore() {
        binding.btnViewMore.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_articlesFragment)
        }
    }

    private fun goToNotifications() {
        binding.ivNotifications.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_notificationFragment)
        }
    }

    private fun goToProfile() {
        binding.ivProfile.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_navigation_profile)
        }
    }

    private fun onDetail() {
        articleAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                requireActivity().intent.putExtra(EXTRA_ARTICLE, it)
            }

            findNavController().navigate(
                R.id.action_navigation_home_to_detailArticleFragment,
                bundle
            )
        }
    }

    private fun Boolean.showLoading() = if (this) {
        binding.progressBar.visibility = VISIBLE
    } else {
        binding.progressBar.visibility = INVISIBLE
    }
}
