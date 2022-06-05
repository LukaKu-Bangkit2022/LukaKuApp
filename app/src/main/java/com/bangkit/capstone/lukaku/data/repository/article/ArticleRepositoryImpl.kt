package com.bangkit.capstone.lukaku.data.repository.article

import com.bangkit.capstone.lukaku.data.local.entity.ArticleEntity
import com.bangkit.capstone.lukaku.data.local.room.ArticleDao
import com.bangkit.capstone.lukaku.data.models.ArticleResponse
import com.bangkit.capstone.lukaku.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val articleDao: ArticleDao
) : ArticleRepository {
    override fun getArticle(): Flow<Result<List<ArticleEntity>>> = flow {
        try {
            val response = apiService.getAllArticles()
            val articleList = response.map { item ->
                val isBookmarked = articleDao.isArticleBookmarked(item.id)
                ArticleEntity(
                    item.author,
                    item.category,
                    item.definition_description,
                    item.definition_title,
                    item.id,
                    item.imageUrl,
                    item.publishedAt,
                    item.symptom_description,
                    item.symptom_title,
                    item.title,
                    item.treatment_description,
                    item.treatment_title,
                    isBookmarked
                )
            }
            emit(Result.success(articleList))
            articleDao.deleteAll()
            articleDao.insertArticle(articleList)
        } catch (exception: Exception) {
            exception.printStackTrace()
            emit(Result.failure(exception))
        }
        articleDao.getArticle().map {
            emit(Result.success(it))
        }
    }.flowOn(Dispatchers.IO)

    override fun getBookmarkedArticle(): Flow<List<ArticleEntity>> =
        articleDao.getBookmarkedArticle()

    override fun searchArticle(searchQuery: String): Flow<Result<List<ArticleEntity>>> = flow {
        try {
            val response = apiService.searchArticles(searchQuery)
            val articleList = response.map { item ->
                val isBookmarked = articleDao.isArticleBookmarked(item.id)
                ArticleEntity(
                    item.author,
                    item.category,
                    item.definition_description,
                    item.definition_title,
                    item.id,
                    item.imageUrl,
                    item.publishedAt,
                    item.symptom_description,
                    item.symptom_title,
                    item.title,
                    item.treatment_description,
                    item.treatment_title,
                    isBookmarked
                )
            }
            emit(Result.success(articleList))
        } catch (exception: Exception) {
            exception.printStackTrace()
            emit(Result.failure(exception))
        }
    }

    override suspend fun setArticleBookmark(articleEntity: ArticleEntity, bookmarkState: Boolean) {
        articleEntity.isBookmarked = bookmarkState
        articleDao.updateArticle(articleEntity)
    }
}