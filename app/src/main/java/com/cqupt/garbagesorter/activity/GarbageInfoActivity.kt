package com.cqupt.garbagesorter.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.runtime.Composable
import com.cqupt.garbagesorter.R
import com.cqupt.garbagesorter.db.bean.Garbage

class GarbageInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_garbage_info)
    }


}