package com.cqupt.garbagesorter.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.compose.setContent
import androidx.appcompat.widget.Toolbar
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView

import com.cqupt.garbagesorter.R

class DetailActivity : AppCompatActivity() {
    private lateinit var garbageType: String
    private lateinit var composeView: ComposeView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initView()
        initComposeView()


    }

    private fun initComposeView() {
        composeView = findViewById(R.id.detail_compose_view)
        composeView.setContent {


        }
    }

    private fun initView() {
        // 设置工具栏
        val toolbar: Toolbar = findViewById(R.id.detail_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 获取传入的垃圾类型参数
        garbageType = intent.getStringExtra("garbage_type") ?: ""

        // 更新工具栏中的垃圾类型
        supportActionBar?.title = garbageType
    }

    // 处理工具栏中的返回按钮点击事件
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}