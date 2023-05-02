package com.cqupt.garbagesorter.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.cqupt.garbagesorter.R
import com.cqupt.garbagesorter.activity.base.BaseActivity
import com.cqupt.garbagesorter.db.MyDatabase
import com.cqupt.garbagesorter.db.bean.Garbage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DetailActivity : BaseActivity() {
    private lateinit var garbageType: String
    private lateinit var toolbar: Toolbar
    private lateinit var composeView: ComposeView
    private var imageId: Int = 0
    private lateinit var title: String
    private lateinit var subTitle: String
    private var color: Long = 0
    private lateinit var discription: String
    private lateinit var discription2: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initView()
        initViewData()
        initComposeView()


    }

    private fun initViewData() {
        when (intent.getStringExtra("garbage_type")) {
            resources.getString(R.string.type1) -> {
                imageId = R.drawable.kehuishouwu_xiao
                title = resources.getString(R.string.type1)
                color = 0xFF3162A7
                subTitle = "RECYCLE WASTE"
                discription =resources.getString(R.string.deatil_des11)
                discription2 = resources.getString(R.string.detail_des12)
            }
            resources.getString(R.string.type3) -> {
                imageId = R.drawable.qitalaji_xiao
                title = resources.getString(R.string.type3)
                color = 0xFF56686C
                subTitle = "RESIDUAL(OTHER) WASTE"
                discription = resources.getString(R.string.detail_des21)
                discription2 =resources.getString(R.string.detail_des22)

            }
            resources.getString(R.string.type4)-> {
                imageId = R.drawable.youhailaji_xiao
                title = resources.getString(R.string.type4)
                color = 0xFFA42B3E
                subTitle = "HAZARDOUS WASTE"
                discription =resources.getString(R.string.detail_des31)
                discription2 =resources.getString(R.string.detail_des32)

            }
            resources.getString(R.string.type2) -> {
                imageId = R.drawable.chuyulaji_xiao
                title = resources.getString(R.string.type2)
                color = 0xFF1C7070
                subTitle = "HOUSEHOLD FOOD WASTE"
                discription =resources.getString(R.string.detail_des41)
                discription2 = resources.getString(R.string.detail_des42)

            }


        }
    }

    private fun initComposeView() {
        composeView = findViewById(R.id.detail_compose_view)
        composeView.setContent {
            Column {
                ImageWithText()
                ListByRoom()
            }


        }
    }

    @Composable
    private fun ListByRoom() {
        var type: String? = intent.getStringExtra("garbage_type")
        val database = MyDatabase.getDatabase(this)
        val dao = database.GarbageDao()
        val garbages = remember { mutableStateListOf<Garbage>() }
        LaunchedEffect(garbages) {
            withContext(Dispatchers.IO) {
                val result = type?.let { dao?.getTop10ByTypeChooser(it,this@DetailActivity) }
                garbages.clear()
                if (result != null) {
                    garbages.addAll(result)
                }
            }

        }
        LazyColumn(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 25.dp)) {
            items(garbages.size) { index ->
                var backgroundColor = if (index % 2 == 0) Color.White else Color.LightGray
                garbages[index].name?.let {
                    Box(contentAlignment = Alignment.Center,) {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier
                                .height(25.dp)
                                .padding(start = 20.dp, end = 20.dp)
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .background(backgroundColor),
                            textAlign = TextAlign.Justify
                        )
                    }


                }
            }
        }
    }

    @Composable
    @Preview
    fun ImageWithText() {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(20.dp)
            ) {
                Image(
                    painter = painterResource(id = imageId),
                    contentDescription = "Image Description",
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.subtitle1.copy(
                            color = Color(color),
                            fontSize = 18.sp
                        )
                    )
                    Text(
                        text = subTitle,
                        style = MaterialTheme.typography.subtitle1.copy(
                            color = Color(color)
                        )
                    )
                }
            }
            Column {
                Text(
                    text = resources.getString(R.string.detail_text1),
                    style = MaterialTheme.typography.body1.copy(color = Color(color)),
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                )
                Text(
                    text = discription,
                    style = MaterialTheme.typography.body2.copy(lineHeight = 25.sp),
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp)
                )
                Text(
                    text = resources.getString(R.string.detail_text2),
                    style = MaterialTheme.typography.body1.copy(color = Color(0xff4d8dc6)),
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp)
                )
                Text(
                    text = discription2,
                    style = MaterialTheme.typography.body2.copy(lineHeight = 25.sp),
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp)
                )
                Text(
                    text = resources.getString(R.string.detail_text3),
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp)
                )
            }

        }

    }

    private fun initView() {
        // 设置工具栏
        toolbar = findViewById(R.id.detail_toolbar)
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

    override fun onDestroy() {
        super.onDestroy()
        setSupportActionBar(null)
    }
}




