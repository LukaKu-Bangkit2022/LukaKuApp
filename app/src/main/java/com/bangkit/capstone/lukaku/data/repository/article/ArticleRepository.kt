package com.bangkit.capstone.lukaku.data.repository.article

import androidx.lifecycle.LiveData
import com.bangkit.capstone.lukaku.data.local.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    fun getArticle(): Flow<Result<List<ArticleEntity>>>
    fun getBookmarkedArticle(): LiveData<List<ArticleEntity>>
    fun searchArticle(searchQuery: String): Flow<Result<List<ArticleEntity>>>
    suspend fun setArticleBookmark(articleEntity: ArticleEntity, bookmarkState: Boolean)
}