package com.bangkit.capstone.lukaku.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "article")
data class ArticleEntity(
    @field:ColumnInfo(name = "author")
    val author: String,

    @field:ColumnInfo(name = "category")
    val category: String,

    @field:ColumnInfo(name = "definition_description")
    val definition_description: String,

    @field:ColumnInfo(name = "definition_title")
    val definition_title: String,

    @field:ColumnInfo(name = "id")
    @field:PrimaryKey val id: Int,

    @field:ColumnInfo(name = "imageUrl")
    val imageUrl: String,

    @field:ColumnInfo(name = "publishedAt")
    val publishedAt: String,

    @field:ColumnInfo(name = "symptom_description")
    val symptom_description: String,

    @field:ColumnInfo(name = "symptom_title")
    val symptom_title: String,

    @field:ColumnInfo(name = "title")
    val title: String,

    @field:ColumnInfo(name = "treatment_description")
    val treatment_description: String,

    @field:ColumnInfo(name = "treatment_title")
    val treatment_title: String,

    @field:ColumnInfo(name = "bookmarked")
    var isBookmarked: Boolean,

    @field:ColumnInfo(name = "uid")
    var uid: String? = "",
) : Parcelable