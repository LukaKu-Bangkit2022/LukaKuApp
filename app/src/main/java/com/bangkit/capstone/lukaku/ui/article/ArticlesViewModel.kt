package com.bangkit.capstone.lukaku.ui.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.capstone.lukaku.data.local.entity.ArticleEntity
import com.bangkit.capstone.lukaku.data.repository.article.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val articleRepository: ArticleRepository
) : ViewModel() {

    fun getAllArticle() = articleRepository.getArticle()

    suspend fun searchArticle(searchQuery: String) = articleRepository.searchArticle(searchQuery)

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