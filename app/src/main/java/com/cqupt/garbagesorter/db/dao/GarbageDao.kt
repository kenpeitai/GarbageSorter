package com.cqupt.garbagesorter.db.dao

import androidx.room.*

import com.cqupt.garbagesorter.db.bean.Garbage

@Dao
interface GarbageDao {
    @Query("SELECT * FROM garbage")
    fun getAll(): List<Garbage>
    @Query("SELECT * FROM garbage WHERE like_index = :likeIndex")
    fun getCollection(likeIndex: Int):List<Garbage>?
    @Query("SELECT * FROM garbage WHERE id = :id")
    fun getById(id: String): Garbage?
    @Query("SELECT * FROM garbage WHERE type = :type")
    fun getByType(type: String): List<Garbage>?
    @Query("SELECT * FROM garbage WHERE type = :type LIMIT 10")
    fun getTop10ByType(type: String): List<Garbage>?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(garbage: Garbage)
    @Query("SELECT * FROM garbage WHERE name LIKE '%' || :name || '%'")
    fun getGarbageListByName(name: String): List<Garbage>
    @Query("UPDATE garbage SET like_index = :likeIndex WHERE id = :id")
    suspend fun updateGarbageLikeIndex(id: String, likeIndex: Int)
    @Delete
    fun delete(garbage: Garbage)
    @Update
    suspend fun updateGarbage(garbage: Garbage)
}
