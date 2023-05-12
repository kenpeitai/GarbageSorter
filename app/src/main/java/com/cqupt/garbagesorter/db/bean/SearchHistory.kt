package com.cqupt.garbagesorter.db.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "search_history",
    foreignKeys = [ForeignKey(
        entity = Garbage::class,
        parentColumns = ["id"],
        childColumns = ["garbage_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class SearchHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var searchtimes: Int,
    val time: String,
    @ColumnInfo(name = "garbage_id", index = true)
    val garbageId: Int
)
