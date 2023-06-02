package com.cqupt.garbagesorter.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.cqupt.garbagesorter.R
import com.cqupt.garbagesorter.activity.base.BaseActivity
import com.cqupt.garbagesorter.activity.ui.theme.GarbageSorterTheme
import com.cqupt.garbagesorter.db.MyDatabase
import com.cqupt.garbagesorter.db.bean.Garbage
import com.cqupt.garbagesorter.db.dao.GarbageDao
import kotlinx.coroutines.*

class GarbageInfoActivity : BaseActivity() {
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
            LaunchedEffect(garbage, garbage.likeIndex) {
                withContext(Dispatchers.IO) {
                    garbage = id?.let { dao.getByIdChooser(it, this@GarbageInfoActivity) }!!
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
        var showDialog by remember { mutableStateOf(false) }
        var currentItem by remember { mutableStateOf(-1) }
        Box() {
            Column() {
                SetBar(garbage)
                Spacer(modifier = Modifier.heightIn(20.dp))
                SetTitleAndType(garbage)

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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    Image(
                        painter = painterResource(id = drawableId1),           //展示一张图片
                        contentDescription = "Image of $name1",
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 15.dp, horizontal = 5.dp)
                            .clip(
                                RoundedCornerShape(5.dp)
                            )
                            .clickable {
                                showDialog = true
                                currentItem = drawableId1
                            }
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
                            .clickable {
                                showDialog = true
                                currentItem = drawableId2
                            }
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
                            .clickable {
                                showDialog = true
                                currentItem = drawableId3
                            }
                    )

                }


                SetDescription(garbage)
            }
            if (showDialog) {
                Dialog(onDismissRequest = { showDialog = false }) {
                    Box() {
                        ImageWithZoomableBackground(painterResource(id = currentItem))
                    }
                }
            }

        }


    }

    private @Composable
    fun SetDescription(garbage: Garbage) {
        val textColor = when (garbage.type) {
            resources.getString(R.string.type1) -> Color(0xFF3162A7)
            resources.getString(R.string.type2) -> Color(0xFF1C7070)
            resources.getString(R.string.type3) -> Color(0xFF56686C)
            resources.getString(R.string.type4) -> Color(0xFFA42B3E)
            else -> Color.DarkGray
        }
        val tip = when (garbage.type) {
            resources.getString(R.string.type1) -> resources.getString(R.string.type11)
            resources.getString(R.string.type2) -> resources.getString(R.string.type22)
            resources.getString(R.string.type3) -> resources.getString(R.string.type33)
            resources.getString(R.string.type4) -> resources.getString(R.string.type44)
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
            text = resources.getString(R.string.garbageinfoactivity_text1),
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
    fun SetImage(garbage: Garbage, showDialog: Boolean) {


    }

    private fun showImageDialog(drawableId1: Int) {

    }

    @Composable
    private fun SetTitleAndType(garbage: Garbage) {
        val imageId = when (garbage.type) {
            resources.getString(R.string.type1) -> R.drawable.kehuishouwu_xiao
            resources.getString(R.string.type2) -> R.drawable.chuyulaji_xiao
            resources.getString(R.string.type3) -> R.drawable.qitalaji_xiao
            resources.getString(R.string.type4) -> R.drawable.youhailaji_xiao
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
                        Toast.makeText(
                            context,
                            resources.getString(R.string.garbageinfoactivity_text2),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        coroutineScope.launch {
                            withContext(Dispatchers.IO) {
                                dao.updateGarbageLikeIndexAll(garbage.id, 1)
                                withContext(Dispatchers.Main) {

                                    Toast.makeText(
                                        context,
                                        resources.getString(R.string.garbageinfoactivity_text3),
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
                    Text(text = resources.getString(R.string.garbageinfoactivity_text4))
                }

            }

        }

    }


    @Composable
    fun ImageWithZoomableBackground(
        image: Painter,
        modifier: Modifier = Modifier
    ) {


        // Define the scale animation
        val scale = animateFloatAsState(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000),

            )

        Box(
            modifier = modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .scale(scale.value)
            )
            Text(
                text = resources.getString(R.string.garbageinfoactivity_image_hint),
                modifier = Modifier.align(
                    Alignment.BottomCenter
                )
            )

        }
    }


    @Composable
    private fun SetBar(garbage: Garbage) {
        TopAppBar(
            title = {
                Text(
                    text = resources.getString(R.string.garbageinfoactivity_bartitle),
                    color = Color.White
                )
            },
            navigationIcon = {
                IconButton(onClick = { onBackPressed() }) {
                    Icon(Icons.Filled.ArrowBack, "Back")
                }
            },
            backgroundColor = MaterialTheme.colors.secondary,
            actions = {
                IconButton(onClick = { sendEmail(garbage) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_report_problem_24),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(end = 20.dp)
                    )

                }
            }
        )
    }

    private fun sendEmail(garbage: Garbage) {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("2443595035@qq.com"))
            putExtra(Intent.EXTRA_SUBJECT, "邮件主题:报错")
            putExtra(Intent.EXTRA_TEXT, "${garbage.name}, id: ${garbage.id}, type${garbage.type}")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(emailIntent)
    }

    override fun onBackPressed() {
        Log.d("Tag of GarbageInfoActivity", "onBackPressed: ")
        val resultIntent = Intent()
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
        super.onBackPressed()
    }
}



