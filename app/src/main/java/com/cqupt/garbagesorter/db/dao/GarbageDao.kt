package com.cqupt.garbagesorter.db.dao

import androidx.room.*

import com.cqupt.garbagesorter.db.bean.Garbage

@Dao
interface GarbageDao {
    @Query("SELECT * FROM garbage")
    fun getAll(): List<Garbage>

    @Query("SELECT * FROM garbage WHERE id = :id")
    fun getById(id: String): Garbage?
    @Query("SELECT * FROM garbage WHERE type = :type")
    fun getByType(type: String): List<Garbage>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(garbage: Garbage)

    @Delete
    fun delete(garbage: Garbage)
}
