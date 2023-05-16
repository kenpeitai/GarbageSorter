package com.cqupt.garbagesorter.activity
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.layout.ContentScale
import androidx.core.content.FileProvider
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.cqupt.garbagesorter.R
import com.cqupt.garbagesorter.activity.base.BaseActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class ImageUploadActivity : BaseActivity(),EasyPermissions.PermissionCallbacks{
    private val PICK_IMAGE = 1
    private val TAKE_PHOTO = 2
    private lateinit var photoFile1:File

    // 用于存储选取的图片的 Uri
    private var imageUri: MutableState<Uri?> = mutableStateOf(null)
    companion object {
        const val RC_CAMERA_AND_STORAGE = 123
    }

    @OptIn(ExperimentalCoilApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = { check1(context) }) {
                    Text("选择图片")
                }
                Spacer(modifier = Modifier.height(16.dp))

                imageUri.value?.let {
                    Image(
                        painter = rememberImagePainter(data = it),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentScale = ContentScale.Inside
                    )
                }
            }
        }
    }

    private fun check1(context: Context) {
        val perms = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (EasyPermissions.hasPermissions(context, *perms)) {
            // 已有权限，可以执行操作
            // ...
            openImageChooser()
        }else {
            // 没有权限，请求权限
            EasyPermissions.requestPermissions(
                this,
                "应用需要相机和读取外部存储权限来上传图片",
                RC_CAMERA_AND_STORAGE,
                *perms
            )
        }
    }

    private fun openImageChooser() {
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePhotoIntent ->
            takePhotoIntent.addCategory(Intent.CATEGORY_DEFAULT)
            takePhotoIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()

                } catch (ex: IOException) {
                    null
                }
                photoFile1 = photoFile!!
                Log.d("createImageFile()--------------------->", "openImageChooser: ${photoFile}+${photoFile!!.path}")
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.cqupt.garbagesorter.fileprovider",
                        it
                    )
                    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                }
            }
        }
        val pickPhotoFromGalleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        val intentChooser = Intent.createChooser(pickPhotoFromGalleryIntent, getString(R.string.image_chooser))
        intentChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(takePhotoIntent))
        startActivityForResult(intentChooser, PICK_IMAGE)
    }


    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.getExternalStorageState())
        if (storageDir != null) {
            if(!storageDir.exists()){

                storageDir.mkdir();

            }
        }
        return File.createTempFile("JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
            .apply {
         //   imageUri.value = Uri.fromFile(this)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            if (selectedImageUri == null) {
                imageUri.value = FileProvider.getUriForFile(this,"com.cqupt.garbagesorter.fileprovider",photoFile1)
            } else {
                imageUri.value = selectedImageUri // handle the case of image picked from gallery
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // 把结果转发到EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        // 权限已被用户授予，可以执行相应操作
        if (requestCode == RC_CAMERA_AND_STORAGE) {
            openImageChooser()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        // 权限被用户拒绝
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            // 如果有些权限被永久地拒绝了，显示一个对话框引导用户进入应用设置界面手动开启权限
            AppSettingsDialog.Builder(this).build().show()
        }
    }


}