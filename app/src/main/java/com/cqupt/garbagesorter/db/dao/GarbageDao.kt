package com.cqupt.garbagesorter.db.dao

import android.content.Context
import androidx.room.*

import com.cqupt.garbagesorter.db.bean.Garbage
import com.cqupt.garbagesorter.db.bean.GarbageEN
import java.util.*
/**
*
* 不要直接使用普通方法或EN结尾方法
 ** 使用对应Chooser方法
*
*
*/
@Dao
interface GarbageDao {
    @Query("SELECT * FROM garbage")
    fun getAll(): List<Garbage>

    @Query("SELECT * FROM garbage_en")
    fun getAllEN(): List<GarbageEN>
    @Transaction
    fun getAllChooser(context: Context):List<Garbage>{
        val language = context.applicationContext.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE).getString("language", Locale.CHINA.language)
        when(language){
            Locale.CHINA.language -> return getAll()
            Locale.ENGLISH.language ->return toGarbageList(getAllEN())!!
            else -> return getAll()
        }
    }

    @Query("SELECT * FROM garbage WHERE like_index = :likeIndex")
    fun getCollection(likeIndex: Int):List<Garbage>?

    @Query("SELECT * FROM garbage_en WHERE like_index = :likeIndex")
    fun getCollectionEN(likeIndex: Int):List<GarbageEN>?
    @Transaction
    fun getCollectionChooser(likeIndex: Int,context: Context):List<Garbage>?{
        val language = context.applicationContext.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE).getString("language", Locale.CHINA.language)
        when(language){
            Locale.CHINA.language -> return getCollection(likeIndex)
            Locale.ENGLISH.language -> return toGarbageList(getCollectionEN(likeIndex))
            else ->return getCollection(likeIndex)
        }
    }

    @Query("SELECT * FROM garbage WHERE id = :id")
    fun getById(id: String): Garbage?

    @Query("SELECT * FROM garbage_en WHERE id = :id")
    fun getByIdEN(id: String): GarbageEN?

    @Transaction
    fun getByIdChooser(id: String,context: Context):Garbage?{
        val language = context.applicationContext.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE).getString("language", Locale.CHINA.language)
        when(language){
            Locale.CHINA.language -> return getById(id)
            Locale.ENGLISH.language -> return toGarbage(getByIdEN(id))
            else ->return getById(id)
        }
    }

    @Query("SELECT * FROM garbage WHERE type = :type")
    fun getByType(type: String): List<Garbage>?

    @Query("SELECT * FROM garbage_en WHERE type = :type")
    fun getByTypeEN(type: String): List<GarbageEN>?
    @Transaction
    fun getByTypeChooser(type: String,context: Context): List<Garbage>?{
        val language = context.applicationContext.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE).getString("language", Locale.CHINA.language)
        when(language){
            Locale.CHINA.language ->return getByType(type)
            Locale.ENGLISH.language ->return toGarbageList(getByTypeEN(type))
            else ->return getByType(type)
        }
    }

    @Query("SELECT * FROM garbage WHERE type = :type LIMIT 10")
    fun getTop10ByType(type: String): List<Garbage>?

    @Query("SELECT * FROM garbage_en WHERE type = :type LIMIT 10")
    fun getTop10ByTypeEN(type: String): List<GarbageEN>?
    fun getTop10ByTypeChooser(type: String,context: Context):List<Garbage>?{
        val language = context.applicationContext.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE).getString("language", Locale.CHINA.language)
        when(language){
            Locale.CHINA.language -> return getTop10ByType(type)
            Locale.ENGLISH.language -> return toGarbageList(getTop10ByTypeEN(type))
            else -> return getTop10ByType(type)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(garbage: Garbage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEN(garbage: GarbageEN)

    @Query("SELECT * FROM garbage WHERE name LIKE '%' || :name || '%'")
    fun getGarbageListByName(name: String): List<Garbage>

    @Query("SELECT * FROM garbage_en WHERE name LIKE '%' || :name || '%'")
    fun getGarbageListByNameEN(name: String): List<GarbageEN>
    @Transaction
    fun getGarbageListByNameChooser(name: String,context: Context):List<Garbage>{
        val language = context.applicationContext.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE).getString("language", Locale.CHINA.language)
        when(language){
            Locale.CHINA.language -> return getGarbageListByName(name)
            Locale.ENGLISH.language -> return toGarbageList(getGarbageListByNameEN(name))!!
            else -> return getGarbageListByName(name)
        }
    }

    @Query("UPDATE garbage SET like_index = :likeIndex WHERE id = :id")
    suspend fun updateGarbageLikeIndex(id: String, likeIndex: Int)

    @Query("UPDATE garbage_en SET like_index = :likeIndex WHERE id = :id")
    suspend fun updateGarbageLikeIndexEN(id: String, likeIndex: Int)

    suspend fun updateGarbageLikeIndexAll(id: String, likeIndex: Int){
        updateGarbageLikeIndex(id,likeIndex)
        updateGarbageLikeIndexEN(id,likeIndex)
    }

    @Delete
    fun delete(garbage: Garbage)

    @Delete
    fun deleteEN(garbage: GarbageEN)

    @Update
    suspend fun updateGarbage(garbage: Garbage)

    @Update
    suspend fun updateGarbageEN(garbage: GarbageEN)

    fun toGarbage(en: GarbageEN?):Garbage? {
        if (en != null) {
            return Garbage(id = en.id_en, type = en.type_en, name = en.name_en, description = en.description_en, likeIndex = en.likeIndex_en, img = null)
        }else{
            return null
        }
    }
    fun toGarbageList(enList: List<GarbageEN>?): List<Garbage>? {
        return enList?.map { en ->
            Garbage(id = en.id_en, type = en.type_en, name = en.name_en, description = en.description_en, likeIndex = en.likeIndex_en, img = null)
        }
    }
}
