package com.cqupt.garbagesorter.activity.image

import android.content.Context
import android.graphics.Bitmap
import org.json.JSONException
import org.json.JSONObject
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.Charset

class ImagePredictor (private val context: Context){
    companion object {
        private const val IMAGE_SIZE = 224 // 图像的宽度和高度
        private const val IMAGE_MEAN = 127.5f // 在预处理中，从像素值中减去的平均值
        private const val IMAGE_STD = 127.5f // 在预处理中，像素值除以的标准差
        private const val NUM_CLASSES = 158 // 模型预测的类别数
    }
    
    fun predict(bitmap: Bitmap): Int? {
        val modelFile = FileUtil.loadMappedFile(context, "ResNet50.tflite")
        val tflite = Interpreter(modelFile)

        var mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val imgData = ByteBuffer.allocateDirect(1 * Companion.IMAGE_SIZE * Companion.IMAGE_SIZE * 3 * 4)
        imgData.order(ByteOrder.nativeOrder())
        val intValues = IntArray(mutableBitmap.width * mutableBitmap.height)
        //将图像缩放为
        mutableBitmap = Bitmap.createScaledBitmap(mutableBitmap,
            IMAGE_SIZE,
            IMAGE_SIZE, false)
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
        //图像归一化处理
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
        val assetManager = context.assets
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
        var id = ""
        val assetManager = context.assets
        try {
            val `is`: InputStream = assetManager.open("garbage_model_label_id.json")
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            val json = String(buffer, Charset.forName("UTF-8"))
            val jsonObject = JSONObject(json)
            id = jsonObject.get(int.toString()).toString()
            // 你现在可以使用jsonObject.get("0")来获取标签"0"对应的类别id
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return id
    }



}