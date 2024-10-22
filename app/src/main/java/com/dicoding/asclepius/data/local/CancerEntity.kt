package com.dicoding.asclepius.data.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "cancers_history")
@Parcelize
data class CancerEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "image")
    var image: String,

    @ColumnInfo(name = "result")
    var result: String

): Parcelable