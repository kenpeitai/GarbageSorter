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
    private var flag = 0
    private var imageBitmap: ImageBitmap? = null

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var imageUri = intent.getStringExtra("imageUri")!!
        // 使用 imageUri 进行相应的操作，如加载、显示或处理图像
        // ...

        var onWaitngBitmap = loadImageUri(uri = Uri.parse(imageUri))
        var tag = predict(onWaitngBitmap!!)
        var resultId = getIdByTag(tag!!)
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
                        //   var imagePainter: Painter = rememberImagePainter(Uri.parse(imageUri))


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
                        // var result by remember { mutableStateOf("NaN") }

                        //  result = "the prob classNum is $tag  name: ${getNameByTag(tag)}"
                        //  Text(text = result)
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

    fun loadImageUri(uri: Uri?): Bitmap? {
        val opts = BitmapFactory.Options()
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888
        val bitmap =
            BitmapFactory.decodeStream(uri?.let { contentResolver.openInputStream(it) }, null, opts)
//        /*var bitmap1: Bitmap? = null
//        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            uri?.let {
//                ImageDecoder.createSource(
//                    this@ImageRecognitionActivity.contentResolver,
//                    it
//                )
//            }?.let {
//                ImageDecoder.decodeBitmap(
//                    it
//                )
//            }
//        } else {
//            MediaStore.Images.Media.getBitmap(
//                this@ImageRecognitionActivity.contentResolver,
//                uri
//            )
//        }
//        bitmap1 = bitmap*/
        return bitmap
    }

    fun predict(bitmap: Bitmap): Int? {
        val modelFile = FileUtil.loadMappedFile(this, "ResNet50.tflite")
        val tflite = Interpreter(modelFile)
        val IMAGE_SIZE = 224 // 图像的宽度和高度
        val IMAGE_MEAN = 127.5f // 在预处理中，从像素值中减去的平均值
        val IMAGE_STD = 127.5f // 在预处理中，像素值除以的标准差
        val NUM_CLASSES = 158 // 这应该是你的模型预测的类别数
        var mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

        val imgData = ByteBuffer.allocateDirect(1 * IMAGE_SIZE * IMAGE_SIZE * 3 * 4)
        imgData.order(ByteOrder.nativeOrder())
        val intValues = IntArray(mutableBitmap.width * mutableBitmap.height)
        //将图像缩放为
        mutableBitmap = Bitmap.createScaledBitmap(mutableBitmap, IMAGE_SIZE, IMAGE_SIZE, false)
        //val intValues = IntArray(IMAGE_SIZE * IMAGE_SIZE)
        mutableBitmap.getPixels(
            intValues,
            0,
            mutableBitmap.width,
            0,
            0,
            mutableBitmap.width,
            mutableBitmap.height
        )
        imgData.rewind()
        for (i in 0 until IMAGE_SIZE) {
            for (j in 0 until IMAGE_SIZE) {
                val pixelValue = intValues[i * IMAGE_SIZE + j]
                imgData.putFloat(((pixelValue shr 16 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                imgData.putFloat(((pixelValue shr 8 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                imgData.putFloat(((pixelValue and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
            }
        }
        val output = Array(1) { FloatArray(NUM_CLASSES) }
        tflite.run(imgData, output)
// 获取最可能的类别
        val outputClass = output[0].withIndex().maxByOrNull { it.value }?.index

        return outputClass
    }

    fun getNameByTag(int: Int): String {
        var name = ""
        val assetManager = assets
        try {
            val `is`: InputStream = assetManager.open("garbage_classification_name.json")
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            val json = String(buffer, Charset.forName("UTF-8"))
            val jsonObject = JSONObject(json)
            name = jsonObject.get(int.toString()).toString()
            // 你现在可以使用jsonObject.get("0")来获取标签"0"对应的类别名称
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return name
    }

    fun getIdByTag(int: Int): String {
        var name = ""
        val assetManager = assets
        try {
            val `is`: InputStream = assetManager.open("garbage_model_label_id.json")
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            val json = String(buffer, Charset.forName("UTF-8"))
            val jsonObject = JSONObject(json)
            name = jsonObject.get(int.toString()).toString()
            // 你现在可以使用jsonObject.get("0")来获取标签"0"对应的类别名称
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return name
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