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
import com.cqupt.garbagesorter.activity.base.BaseActivity
import com.cqupt.garbagesorter.activity.locale.LocaleManager
import com.cqupt.garbagesorter.db.MyDatabase
import com.cqupt.garbagesorter.db.bean.Garbage
import kotlinx.coroutines.*
import java.util.*

class WelcomeActivity : BaseActivity() {
    private lateinit var appDatabase: MyDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLanguage()
        setContentView(R.layout.activity_welcome)
        initDB()

    }

    private fun initDB() {


        //dbTest()
        goToMainAc()
    }

    private fun setLanguage() {
        // 获取 SharedPreferences 实例
        val sharedPref = applicationContext.getSharedPreferences(
            "MyAppPreferences",
            Context.MODE_PRIVATE
        )

// 读取之前保存的语言设置，如果不存在则使用中文
        val savedLanguage = sharedPref.getString("language", Locale.CHINA.language)
        Log.d("----------->Language:", "setLanguage: $savedLanguage")
        val appLocale = Locale(savedLanguage)
// 使用读取到的语言设置设置当前语言
        LocaleManager(this).setLocale(appLocale)
        LocaleManager(applicationContext).setLocale(appLocale)


    }


    private fun goToMainAc() {
        // 在2秒后跳转到主界面
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 1000)
    }


}