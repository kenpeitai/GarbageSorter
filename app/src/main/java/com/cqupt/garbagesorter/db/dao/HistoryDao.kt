package com.cqupt.garbagesorter.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cqupt.garbagesorter.db.bean.SearchHistory

@Dao
interface HistoryDao {
    @Query("SELECT * FROM search_history")
    fun getAllSearchHistory(): List<SearchHistory>

    @Insert
    suspend fun insertSearchHistory(searchHistory: SearchHistory)
    @Update
    suspend fun updateSearchTimes(searchHistory: SearchHistory)


    @Query("SELECT * FROM search_history WHERE garbage_id = :garbageId")
    suspend fun getSearchHistoryByGarbageId(garbageId: Int): SearchHistory?



}