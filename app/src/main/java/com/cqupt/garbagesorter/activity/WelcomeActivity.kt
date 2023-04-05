package com.cqupt.garbagesorter.activity


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.cqupt.garbagesorter.R
import com.cqupt.garbagesorter.db.MyDatabase
import com.cqupt.garbagesorter.db.bean.Garbage
import kotlinx.coroutines.*


class WelcomeActivity : AppCompatActivity() {
    private lateinit var appDatabase: MyDatabase
    private val DATABASE_NAME = "garbage.db"
    private val DB_FILE_NAME = "test.db"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        initDB()

    }

    private fun initDB() {


        appDatabase = Room.databaseBuilder(
            applicationContext,
            MyDatabase::class.java,
            DATABASE_NAME
        ).createFromAsset(DB_FILE_NAME).build()
        //dbTest()
        goToMainAc()
    }

    private fun dbTest() {
        CoroutineScope(Dispatchers.IO).launch {
            var garbageList = appDatabase.GarbageDao()?.getAll()
            if (garbageList != null) {
                for (garbage in garbageList) {
                    Log.d("DBtestTAG", garbage.name + "-" + garbage.id)
                }
            }
        }
    }


    private fun goToMainAc() {
        // 在2秒后跳转到主界面
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 1000)
    }

    private fun databaseExists(context: Context, databaseName: String): Boolean {
        val databaseFile = context.getDatabasePath(databaseName)
        return databaseFile.exists()
    }

    //从sql文件中导入数据库表
    private fun importDataFromSqlFile(
        context: Context,
        appDatabase: MyDatabase,
        sqlFileName: String,
    ) {


        val inputStream = context.assets.open(sqlFileName)
        val sqlStatements = inputStream.bufferedReader().use { it.readText() }.split(";")

        for (statement in sqlStatements) {
            Log.d("SQL--------------------------------->", statement.toString())
            appDatabase.openHelper.writableDatabase.execSQL(statement)
        }


    }
}