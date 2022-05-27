package com.bangkit.capstone.lukaku.data.repository.article

import com.bangkit.capstone.lukaku.data.models.ArticleResponse
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    suspend fun getAllArticle(): Flow<Result<ArticleResponse>>
}