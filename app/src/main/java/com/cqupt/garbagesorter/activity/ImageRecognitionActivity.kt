package com.cqupt.garbagesorter.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import com.cqupt.garbagesorter.R
import com.cqupt.garbagesorter.activity.base.BaseActivity
import com.cqupt.garbagesorter.activity.image.ImagePredictor
import com.cqupt.garbagesorter.activity.ui.theme.GarbageSorterTheme
import com.cqupt.garbagesorter.db.MyDatabase
import com.cqupt.garbagesorter.db.bean.Garbage
import com.cqupt.garbagesorter.db.dao.GarbageDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.Charset


class ImageRecognitionActivity : BaseActivity() {
    lateinit var database: MyDatabase
    lateinit var dao: GarbageDao
    lateinit var imagePredictor: ImagePredictor

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var imageUri = intent.getStringExtra("imageUri")!!
        // 使用 imageUri 进行相应的操作，如加载、显示或处理图像
        // ...
        // 初始化 imagePredictor
        imagePredictor = ImagePredictor(this)
        var onWaitngBitmap = loadImageUri(uri = Uri.parse(imageUri))
        var tag = imagePredictor.predict(onWaitngBitmap!!)
        var resultId = imagePredictor.getIdByTag(tag!!)
        database = MyDatabase.getDatabase(this)
        dao = database.GarbageDao()!!

        setContent {
            var showText by remember { mutableStateOf("UnKnown") }
            var garbage: Garbage? by remember {
                mutableStateOf(Garbage("-1", null, null, null, null, null))
            }
            val localContext = LocalContext.current

            GarbageSorterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        TopAppBar(title = {
                            Text(text = getString(R.string.recognize_result))

                        }, navigationIcon = {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                        },
                            actions = {}
                        )


                        Image(
                            bitmap = onWaitngBitmap!!.asImageBitmap(),
                            //         painter = imagePainter,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(10.dp)
                                .aspectRatio(1f)
                                .fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        LaunchedEffect(key1 = resultId, block = {
                            withContext(Dispatchers.IO) {
                                garbage = dao.getByIdChooser(resultId, localContext)
                            }
                        })
                        Row(modifier = Modifier.padding(vertical = 10.dp)) {
                            Text(
                                text = getString(R.string.predict_text) + garbage?.type + "--" + garbage?.name,
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .align(alignment = Alignment.CenterVertically), fontSize = 18.sp
                            )
                            Button(onClick = { garbage?.let { startInfoActivity(it.id) } }) {
                                Row() {
                                    Text(text = "查看详细", modifier = Modifier.align(Alignment.CenterVertically))
                                    Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null)
                                }

                            }
                        }



                    }


                }
            }
        }
    }

    private fun startInfoActivity(id: String) {
        val intent = Intent(this, GarbageInfoActivity::class.java)
        intent.putExtra("EXTRA_GARBAGE", id)
        startActivity(intent)
    }

    private fun loadImageUri(uri: Uri?): Bitmap? {
        val opts = BitmapFactory.Options()
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888
        val bitmap =
            BitmapFactory.decodeStream(uri?.let { contentResolver.openInputStream(it) }, null, opts)
        return bitmap
    }


}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GarbageSorterTheme {
        Greeting("Android")
    }
}