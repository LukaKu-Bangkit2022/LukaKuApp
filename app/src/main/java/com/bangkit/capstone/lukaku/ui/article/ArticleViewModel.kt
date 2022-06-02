package com.bangkit.capstone.lukaku.ui.article

import androidx.lifecycle.ViewModel
import com.bangkit.capstone.lukaku.data.repository.article.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val articleRepository: ArticleRepository
) : ViewModel() {

    suspend fun getAllArticle() = articleRepository.getAllArticle()

    suspend fun searchArticle(searchQuery: String) = articleRepository.searchArticle(searchQuery)
}