package com.cqupt.garbagesorter.db.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "garbage")
data class Garbage(
    @PrimaryKey val id: String,
    val type: String?,
    val name: String?,
    val description: String?,
    val img: String?,
    @ColumnInfo(name = "like_index") val likeIndex: Int?
)
