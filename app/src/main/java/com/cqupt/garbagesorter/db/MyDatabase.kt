package com.cqupt.garbagesorter.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cqupt.garbagesorter.db.bean.Garbage
import com.cqupt.garbagesorter.db.bean.GarbageEN
import com.cqupt.garbagesorter.db.dao.GarbageDao
import java.util.Locale


@Database(entities = [Garbage::class,GarbageEN::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract fun GarbageDao(): GarbageDao?

    companion object {
        private var INSTANCE: MyDatabase? = null

        fun getDatabase(context: Context): MyDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val appDatabase = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    "garbage_info_database"
                ).createFromAsset("test1.db").build()
                INSTANCE = appDatabase
                return appDatabase

            }

        }
    }

}
