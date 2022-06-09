package com.bangkit.capstone.lukaku.data.repository.article

import androidx.lifecycle.LiveData
import com.bangkit.capstone.lukaku.data.local.entity.ArticleEntity
import com.bangkit.capstone.lukaku.data.local.room.ArticleDao
import com.bangkit.capstone.lukaku.data.remote.ApiService
import com.google.firebase.auth.FirebaseAuth
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

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val uid: String = auth.currentUser?.uid.toString()

    override fun getArticle(): Flow<Result<List<ArticleEntity>>> = flow {
        try {
            val response = apiService.getAllArticles()
            val articleList = response.map { item ->
                val isBookmarked = uid.let { articleDao.isArticleBookmarked(item.id, it) }
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
                    isBookmarked,
                    uid
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

    override fun getBookmarkedArticle(): LiveData<List<ArticleEntity>> {
        return articleDao.getBookmarkedArticle(uid)
    }

    override fun searchArticle(searchQuery: String): Flow<Result<List<ArticleEntity>>> = flow {
        try {
            val response = apiService.searchArticles(searchQuery)
            val articleList = response.map { item ->
                val isBookmarked = articleDao.isArticleBookmarked(item.id, uid)
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
                    isBookmarked,
                    uid
                )
            }
            emit(Result.success(articleList))
        } catch (exception: Exception) {
            exception.printStackTrace()
            emit(Result.failure(exception))
        }
    }

    override suspend fun setArticleBookmark(articleEntity: ArticleEntity, bookmarkState: Boolean) {
        articleEntity.uid = uid
        articleEntity.isBookmarked = bookmarkState
        articleDao.updateArticle(articleEntity)
    }
}

