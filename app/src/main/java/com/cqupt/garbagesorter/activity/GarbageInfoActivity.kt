package com.cqupt.garbagesorter.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.cqupt.garbagesorter.R
import com.cqupt.garbagesorter.activity.ui.theme.GarbageSorterTheme
import com.cqupt.garbagesorter.db.MyDatabase
import com.cqupt.garbagesorter.db.bean.Garbage
import com.cqupt.garbagesorter.db.dao.GarbageDao
import kotlinx.coroutines.*

class GarbageInfoActivity : ComponentActivity() {
    lateinit var dao: GarbageDao
    lateinit var appDatabase: MyDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = intent.getStringExtra("EXTRA_GARBAGE")
        Log.d("TAG GARBAGE id-------------->", "onCreate: $id")


        appDatabase = MyDatabase.getDatabase(this)
        dao = appDatabase.GarbageDao()!!
        setContent {

            var garbage: Garbage by remember {
                mutableStateOf<Garbage>(
                    Garbage(
                        "0",
                        null,
                        null,
                        null,
                        null,
                        0
                    )
                )
            }
            LaunchedEffect(garbage,garbage.likeIndex) {
                withContext(Dispatchers.IO) {
                    garbage = id?.let { dao.getById(it) }!!
                }

            }


            GarbageSorterTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
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
        val textColor = when (garbage.type) {
            "可回收物" -> Color(0xFF3162A7)
            "厨余垃圾" -> Color(0xFF1C7070)
            "其他垃圾" -> Color(0xFF56686C)
            "有害垃圾" -> Color(0xFFA42B3E)
            else -> Color.DarkGray
        }
        val tip = when (garbage.type) {
            "可回收物" -> LocalContext.current.resources.getString(R.string.type11)
            "厨余垃圾" -> LocalContext.current.resources.getString(R.string.type22)
            "其他垃圾" -> LocalContext.current.resources.getString(R.string.type33)
            "有害垃圾" -> LocalContext.current.resources.getString(R.string.type44)
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
            modifier = Modifier.padding(start = 20.dp, end = 20.dp)
        )
        Spacer(modifier = Modifier.heightIn(20.dp))
        Text(
            text = tip,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp)
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
        val drawableId2 = LocalContext.current.resources.getIdentifier(
            name2,
            "drawable",
            LocalContext.current.packageName
        )
        val drawableId3 = LocalContext.current.resources.getIdentifier(
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
                    .padding(vertical = 15.dp, horizontal = 5.dp)
                    .clip(
                        RoundedCornerShape(5.dp)
                    )
            )

            Image(
                painter = painterResource(id = drawableId2),           //展示一张图片
                contentDescription = "Image of $name1",
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 15.dp, horizontal = 5.dp)
                    .clip(
                        RoundedCornerShape(5.dp)
                    )
            )


            Image(
                painter = painterResource(id = drawableId3),           //展示一张图片
                contentDescription = "Image of $name1",
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 15.dp, horizontal = 5.dp)
                    .clip(
                        RoundedCornerShape(5.dp)
                    )
            )

        }
    }

    @Composable
    private fun SetTitleAndType(garbage: Garbage) {
        val imageId = when (garbage.type) {
            "可回收物" -> R.drawable.kehuishouwu_xiao
            "厨余垃圾" -> R.drawable.chuyulaji_xiao
            "其他垃圾" -> R.drawable.qitalaji_xiao
            "有害垃圾" -> R.drawable.youhailaji_xiao
            else -> R.drawable.kehuishouwu_xiao
        }
        Row(modifier = Modifier.fillMaxWidth()) {

            Image(
                painter = painterResource(id = imageId),
                contentDescription = "image_${garbage.type}",
                modifier = Modifier
                    .size(100.dp)
                    .padding(start = 15.dp)
                    .weight(1f)
            )

            garbage.name?.let {

                Text(
                    text = it, style = MaterialTheme.typography.h6,
                    modifier = Modifier

                        .padding(horizontal = 15.dp)
                        .weight(1f)
                        .align(alignment = Alignment.CenterVertically),

                    )


            }
            val coroutineScope = rememberCoroutineScope()
            val context = LocalContext.current
            Button(
                onClick = {
                    if (garbage.likeIndex == 1) {
                        Toast.makeText(context, "收藏已存在", Toast.LENGTH_SHORT).show()
                    } else {
                        coroutineScope.launch {
                            withContext(Dispatchers.IO) {
                                dao.updateGarbageLikeIndex(garbage.id, 1)
                                withContext(Dispatchers.Main) {

                                    Toast.makeText(
                                        context,
                                        "收藏成功！garbage_likeindex is ${garbage.likeIndex}",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }
                            }
                        }
                        garbage.likeIndex = 1

                    }

                },
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Column() {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Text(text = "添加至收藏")
                }

            }

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

    override fun onBackPressed() {
        Log.d("Tag of GarbageInfoActivity", "onBackPressed: ")
        val resultIntent = Intent()
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
        super.onBackPressed()
    }
}



