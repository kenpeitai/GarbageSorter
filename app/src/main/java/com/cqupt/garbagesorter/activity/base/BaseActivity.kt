package com.cqupt.garbagesorter.activity.base

import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    companion object {
        private var activeCount = 0
    }

    override fun onStart() {
        super.onStart()
        activeCount++
    }

    override fun onStop() {
        super.onStop()
        activeCount--
    }

    fun isAppInForeground(): Boolean {
        return activeCount > 0
    }
    fun isAppInBackground():Boolean{
        return activeCount == 0
    }
}
