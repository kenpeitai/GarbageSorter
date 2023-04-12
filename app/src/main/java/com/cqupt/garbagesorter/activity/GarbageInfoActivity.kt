package com.cqupt.garbagesorter.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.room.Room
import com.cqupt.garbagesorter.R
import com.cqupt.garbagesorter.activity.ui.theme.GarbageSorterTheme
import com.cqupt.garbagesorter.db.MyDatabase
import com.cqupt.garbagesorter.db.bean.Garbage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GarbageInfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = intent.getStringExtra("EXTRA_GARBAGE")
        Log.d("TAG GARBAGE id-------------->", "onCreate: $id")


        val appDatabase = Room.databaseBuilder(
            this,
            MyDatabase::class.java,
            "garbage_info_database"
        ).createFromAsset("test.db").build()

        setContent {

            var garbage: Garbage by remember {
                mutableStateOf<Garbage>(
                    Garbage(
                        "0",
                        null,
                        null,
                        null,
                        null,
                        null
                    )
                )
            }
            LaunchedEffect(garbage) {
                withContext(Dispatchers.IO) {
                    garbage = id?.let { appDatabase.GarbageDao()?.getById(it) }!!
                }
            }

            GarbageSorterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    InitView(garbage)


                }
            }
        }
    }


    @Composable
    private fun InitView(garbage: Garbage) {
        Column() {
            SetBar()
            Spacer(modifier = Modifier.heightIn(20.dp))
            SetTitleAndType(garbage)
            SetImage(garbage)
            SetDescription(garbage)
        }

    }

    private @Composable
    fun SetDescription(garbage: Garbage) {
        val textColor = when(garbage.type){
            "可回收物" -> Color(0xFF3162A7)
            "厨余垃圾" -> Color(0xFF1C7070)
            "其他垃圾" -> Color(0xFF56686C)
            "有害垃圾" -> Color(0xFFA42B3E)
            else -> Color.DarkGray
        }
        val tip = when(garbage.type){
            "可回收物" -> "轻投轻放\n清洁干燥，避免污染\n废纸尽量平整\n立体包装物请清空内容物，清洁后压扁投放\n有尖锐边角的，应包裹后投放"
            "厨余垃圾" -> "纯流质的食物垃圾，如牛奶等，应直接倒进下水口\n有包装物的湿垃圾应将包装物去除后分类投放，包装物请投放到对应的可回收物或干垃圾容器"
            "其他垃圾" -> "尽量沥干水分\n难以辨识类别的生活垃圾投入干垃圾容器内"
            "有害垃圾" -> "投放时请注意轻放\n易破损的请连带包装或包裹后轻放\n如易挥发，请密封后投放"
            else -> ""
        }

        Spacer(modifier = Modifier.heightIn(20.dp))
        garbage.description?.let {
            Box(modifier = Modifier.padding(horizontal = 15.dp)) {
                Text(text = it, style = MaterialTheme.typography.body2.copy(lineHeight = 25.sp))
            }
        }
        Spacer(modifier = Modifier.heightIn(20.dp))
        Text(
            text = "▋ 投放要求",
            style = MaterialTheme.typography.body1.copy(color = textColor),
            modifier = Modifier.padding(start = 20.dp, end = 20.dp,)
        )
        Spacer(modifier = Modifier.heightIn(20.dp))
        Text(
            text = tip,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp,)
        )

    }

    private @Composable
    fun SetImage(garbage: Garbage) {
        val name1 = "a" + garbage.id + "_0"
        val name2 = "a" + garbage.id + "_1"
        val name3 = "a" + garbage.id + "_2"
        val drawableId1 = LocalContext.current.resources.getIdentifier(
            name1,
            "drawable",
            LocalContext.current.packageName
        )
        var drawableId2 = LocalContext.current.resources.getIdentifier(
            name2,
            "drawable",
            LocalContext.current.packageName
        )
        var drawableId3 = LocalContext.current.resources.getIdentifier(
            name3,
            "drawable",
            LocalContext.current.packageName
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

            Image(
                painter = painterResource(id = drawableId1),           //展示一张图片
                contentDescription = "Image of $name1",
                modifier = Modifier
                    .weight(1f)
                    .padding(15.dp)
                    .clip(
                        RoundedCornerShape(5.dp)
                    )
            )


            Image(
                painter = painterResource(id = drawableId2),           //展示一张图片
                contentDescription = "Image of $name1",
                modifier = Modifier
                    .weight(1f)
                    .padding(15.dp)
                    .clip(
                        RoundedCornerShape(5.dp)
                    )
            )


            Image(
                painter = painterResource(id = drawableId3),           //展示一张图片
                contentDescription = "Image of $name1",
                modifier = Modifier
                    .weight(1f)
                    .padding(15.dp)
                    .clip(
                        RoundedCornerShape(5.dp)
                    )
            )

        }
    }

    @Composable
    private fun SetTitleAndType(garbage: Garbage) {
        val imageId = when(garbage.type){
            "可回收物" -> R.drawable.kehuishouwu_xiao
            "厨余垃圾" -> R.drawable.chuyulaji_xiao
            "其他垃圾" -> R.drawable.qitalaji_xiao
            "有害垃圾" -> R.drawable.youhailaji_xiao
            else -> R.drawable.kehuishouwu_xiao
        }
        Row() {

            Image(painter = painterResource(id = imageId), contentDescription = "image_${garbage.type}", modifier = Modifier.size(80.dp).padding(start = 15.dp).weight(1f))
            garbage.name?.let { Text(text = it, style = MaterialTheme.typography.subtitle1,modifier = Modifier
                .padding(horizontal = 15.dp)
                .weight(1f)) }


        }

    }


    @Preview
    @Composable
    private fun SetBar() {
        TopAppBar(
            title = {
                Text(text = "搜索结果", color = Color.White)
            },
            navigationIcon = {
                IconButton(onClick = { onBackPressed() }) {
                    Icon(Icons.Filled.ArrowBack, "Back")
                }
            },
            backgroundColor = MaterialTheme.colors.secondary
        )
    }
}



