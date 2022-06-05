package com.bangkit.capstone.lukaku.data.local.room

import androidx.room.*
import com.bangkit.capstone.lukaku.data.local.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Query("SELECT * FROM article ORDER BY id ASC")
    fun getArticle(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM article where bookmarked = 1")
    fun getBookmarkedArticle(): Flow<List<ArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArticle(articleEntity: List<ArticleEntity>)

    @Update
    suspend fun updateArticle(articleEntity: ArticleEntity)

    @Query("DELETE FROM article WHERE bookmarked = 0")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM article WHERE id = :id AND bookmarked = 1)")
    suspend fun isArticleBookmarked(id: Int): Boolean
}