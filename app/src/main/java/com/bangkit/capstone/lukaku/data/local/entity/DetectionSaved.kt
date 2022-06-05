package com.bangkit.capstone.lukaku.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bangkit.capstone.lukaku.data.models.DetectionResult
import kotlinx.parcelize.Parcelize

@Entity(tableName = "detection")
@Parcelize
data class DetectionSaved(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @field:ColumnInfo(name = "uid")
    var uid: String? = null,

    @field:ColumnInfo(name = "photo_path")
    var photoPath: String? = null,

    var detectionResult: DetectionResult? = null,
) : Parcelable
