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
import com.cqupt.garbagesorter.db.MyDatabase
import com.cqupt.garbagesorter.db.bean.Garbage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {
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
            "可回收物" -> {
                imageId = R.drawable.kehuishouwu_xiao
                title = "可回收物"
                color = 0xFF3162A7
                subTitle = "RECYCLE WASTE"
                discription =
                    "指适宜回收利用和资源化利用的生活废弃物。可回收物主要包括：废纸、废弃塑料瓶、废金属、废包装物、废旧纺织物、废弃电器电子产品、废玻璃、废纸塑铝复合包装等"
                discription2 = "轻投轻放\n清洁干燥，避免污染\n废纸尽量平整\n立体包装物请清空内容物，清洁后压扁投放\n有尖锐边角的，应包裹后投放"
            }
            "其他垃圾" -> {
                imageId = R.drawable.qitalaji_xiao
                title = "其他垃圾"
                color = 0xFF56686C
                subTitle = "RESIDUAL WASTE"
                discription = "干垃圾即其它垃圾，指除可回收垃圾、有害垃圾、湿垃圾以外的其它生活废弃物"
                discription2 = "尽量沥干水分\n难以辨识类别的生活垃圾投入干垃圾容器内"

            }
            "有害垃圾" -> {
                imageId = R.drawable.youhailaji_xiao
                title = "有害垃圾"
                color = 0xFFA42B3E
                subTitle = "HAZARDOUS WASTE"
                discription =
                    "指对人体健康或者自然环境造成直接或者潜在危害生活的废弃物。常见的有害垃圾包括废灯管、废油漆、杀虫剂、废弃化妆品、过期药品、废电池、废灯泡、废水银温度计等，有害垃圾需按照特殊正确的方法安全处理。"
                discription2 = "投放时请注意轻放\n易破损的请连带包装或包裹后轻放\n如易挥发，请密封后投放"

            }
            "厨余垃圾" -> {
                imageId = R.drawable.chuyulaji_xiao
                title = "厨余垃圾"
                color = 0xFF1C7070
                subTitle = "HOUSEHOLD FOOD WASTE"
                discription =
                    "易腐垃圾（餐厨垃圾），也可称湿垃圾，一般是指餐饮经营者、单位食堂等生产过程中产生的餐厨废弃物，以及家庭生活中产生的易腐性垃圾，主要包括：剩菜剩饭、菜梗菜叶、肉食内脏、果壳瓜皮等等。"
                discription2 = "纯流质的食物垃圾，如牛奶等，应直接倒进下水口\n有包装物的湿垃圾应将包装物去除后分类投放，包装物请投放到对应的可回收物或干垃圾容器"

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
                val result = type?.let { dao?.getTop10ByType(it) }
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
                    text = "▋ 定义",
                    style = MaterialTheme.typography.body1.copy(color = Color(color)),
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                )
                Text(
                    text = discription,
                    style = MaterialTheme.typography.body2.copy(lineHeight = 25.sp),
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp)
                )
                Text(
                    text = "▋ 投放要求",
                    style = MaterialTheme.typography.body1.copy(color = Color(0xff4d8dc6)),
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp)
                )
                Text(
                    text = discription2,
                    style = MaterialTheme.typography.body2.copy(lineHeight = 25.sp),
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp)
                )
                Text(
                    text = "▋ 常见垃圾",
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




