package com.bangkit.capstone.lukaku.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.capstone.lukaku.data.local.entity.ArticleEntity
import com.bangkit.capstone.lukaku.data.repository.article.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val articleRepository: ArticleRepository
) : ViewModel() {
    fun getAllArticle() = articleRepository.getArticle()

    fun saveArticle(articleEntity: ArticleEntity) {
        viewModelScope.launch {
            articleRepository.setArticleBookmark(articleEntity, true)
        }
    }

    fun deleteArticle(articleEntity: ArticleEntity) {
        viewModelScope.launch {
            articleRepository.setArticleBookmark(articleEntity, false)
        }
    }
}