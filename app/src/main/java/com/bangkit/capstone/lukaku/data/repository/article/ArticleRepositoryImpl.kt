package com.bangkit.capstone.lukaku.data.repository.article

import com.bangkit.capstone.lukaku.data.models.ArticleResponse
import com.bangkit.capstone.lukaku.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ArticleRepository {
    override suspend fun getAllArticle(): Flow<Result<ArticleResponse>> = flow {
        try {
            val response = apiService.getAllArticles()
            emit(Result.success(response))
        } catch (exception: Exception) {
            exception.printStackTrace()
            emit(Result.failure(exception))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun searchArticle(searchQuery: String): Flow<Result<ArticleResponse>> = flow {
        try {
            val response = apiService.searchArticles(searchQuery)
            emit(Result.success(response))
        } catch (exception: Exception) {
            exception.printStackTrace()
            emit(Result.failure(exception))
        }
    }
}