package com.bangkit.capstone.lukaku.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bangkit.capstone.lukaku.data.local.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Query("SELECT * FROM article ORDER BY id ASC")
    fun getArticle(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM article WHERE uid =:uid AND bookmarked = 1")
    fun getBookmarkedArticle(uid: String): LiveData<List<ArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArticle(articleEntity: List<ArticleEntity>)

    @Update
    suspend fun updateArticle(articleEntity: ArticleEntity)

    @Query("DELETE FROM article WHERE bookmarked = 0")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM article WHERE uid = :uid AND id = :id AND bookmarked = 1)")
    suspend fun isArticleBookmarked(id: Int, uid: String): Boolean
}