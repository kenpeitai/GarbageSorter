package com.cqupt.garbagesorter.db.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "garbage_en")
data class GarbageEN(
    @PrimaryKey val id: String,
    val type: String?,
    val name: String?,
    val description: String?,
    @ColumnInfo(name = "like_index") var likeIndex: Int?
)
