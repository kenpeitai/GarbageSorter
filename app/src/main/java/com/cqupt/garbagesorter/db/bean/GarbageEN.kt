package com.cqupt.garbagesorter.db.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.bytedeco.javacpp.annotation.UniquePtr

@Entity(tableName = "garbage_en")
data class GarbageEN(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id_en: String,
    @ColumnInfo(name = "type")
    val type_en: String?,
    @ColumnInfo(name = "name")
    val name_en: String?,
    @ColumnInfo(name = "description")
    val description_en: String?,
    @ColumnInfo(name = "like_index")
    val likeIndex_en: Int?
)
