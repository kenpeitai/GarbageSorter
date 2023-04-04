package com.cqupt.garbagesorter.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cqupt.garbagesorter.db.bean.Garbage
import com.cqupt.garbagesorter.db.dao.GarbageDao



@Database(entities = [Garbage::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract fun GarbageDao(): GarbageDao?
}
