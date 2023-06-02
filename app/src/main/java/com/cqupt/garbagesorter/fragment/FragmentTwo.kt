package com.cqupt.garbagesorter.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.provider.FontsContractCompat.Columns.RESULT_CODE
import androidx.core.provider.FontsContractCompat.FontRequestCallback.RESULT_OK
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.cqupt.garbagesorter.R
import com.cqupt.garbagesorter.activity.GarbageInfoActivity
import com.cqupt.garbagesorter.db.MyDatabase
import com.cqupt.garbagesorter.db.bean.Garbage
import kotlinx.coroutines.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentTwo.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentTwo : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var toolbar: Toolbar
    private lateinit var composeView: ComposeView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_two, container, false)
        toolbar = view.findViewById(R.id.fragment2_toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title =
            resources.getString(R.string.page_2)
        composeView = view.findViewById(R.id.fragment2_composeView)
        val imageList = listOf(
            R.drawable.kehuishouwu_xiao,
            R.drawable.chuyulaji_xiao,
            R.drawable.qitalaji_xiao,
            R.drawable.youhailaji_xiao
        )
        composeView.setContent {


            SetList1(imageList)

        }


        return view
    }

    @Composable
    private fun SetList1(imageList: List<Int>) {
        var selectedImage by remember { mutableStateOf(0) }
        val garbages = remember { mutableStateListOf<Garbage>() }
        var color: Long by remember { mutableStateOf(0) }
        var isCollected by remember { mutableStateOf(R.drawable.baseline_note_add_24) }
        var isClickable by remember { mutableStateOf(true) }
        var showProgress by remember { mutableStateOf(false) }
        var state by remember { mutableStateOf(0) }
        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                state++
                Log.d("TAG state is ", "the state now is $state")
            }
        }

        //  var progressVisibleTime by remember { mutableStateOf(0L) }
        Column {                        //总页面布局
            Column(                                               //第一列，种类选择栏
                modifier = Modifier.height(80.dp)
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.Center,

                    ) {
                    items(imageList.size) { index ->
                        Box(
                            modifier = Modifier.size(80.dp)
                        ) {
                            Image(
                                painter = painterResource(id = imageList[index]),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
                                    .offset(y = if (selectedImage == index) 8.dp else 0.dp)
                                    .clickable { selectedImage = index },
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                }


            }


            var itemType by remember {
                mutableStateOf("default")
            }
            itemType = when (selectedImage) {
                0 -> resources.getString(R.string.type1)
                1 -> resources.getString(R.string.type2)
                2 -> resources.getString(R.string.type3)
                3 -> resources.getString(R.string.type4)
                else -> resources.getString(R.string.type1)
            }
            color = when (selectedImage) {
                0 -> 0xFF3162A7
                1 -> 0xFF1C7070
                2 -> 0xFF56686C
                3 -> 0xFFA42B3E
                else -> 0xFF3162A7
            }
            //根据图片值查询数据库

            Log.d("selectedImage TAG-------->", "selectedImage:$selectedImage")
            val database = MyDatabase.getDatabase(LocalContext.current)
            val dao = database.GarbageDao()
            LaunchedEffect(garbages, itemType, state) {  //传入的对象改变时执行语句
                withContext(Dispatchers.IO) {
                    val result = dao?.getByTypeChooser(itemType, requireContext())
                    garbages.clear()

                    if (result != null) {
                        garbages.addAll(result)

                        Log.d("garbage's TAG----------->", "garbage's size: ${garbages.size}")

                    }
                }
            }

            Box() {
                LazyColumn(
                    modifier = Modifier.scrollable(
                        rememberScrollState(),
                        enabled = isClickable,
                        orientation = Orientation.Vertical
                    )
                ) {    //第2列，具体物品
                    items(garbages.size) { index ->

                        Row(
                            modifier = Modifier
                                .padding(horizontal = 20.dp, vertical = 1.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .border(
                                    width = 1.dp,
                                    color = Color.Gray,
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .fillMaxWidth()
                                .clickable {
                                    val intent = Intent(context, GarbageInfoActivity::class.java)
                                    intent.putExtra("EXTRA_GARBAGE", garbages[index].id)
                                    launcher.launch(intent)

                                },
                            horizontalArrangement = Arrangement.Center
                        ) {
                            var name = "a" + garbages[index].id + "_0"
                            var drawableId = requireContext().resources.getIdentifier(
                                name,
                                "drawable",
                                requireContext().packageName
                            )
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .weight(2f)
                                    .align(alignment = Alignment.CenterVertically)
                            ) {
                                Image(
                                    painter = painterResource(id = drawableId),           //展示一张图片
                                    contentDescription = "Image of $name",
                                    modifier = Modifier
                                        .height(70.dp)
                                        .clip(
                                            RoundedCornerShape(8.dp)
                                        )
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .weight(4.2f)
                                    .align(Alignment.CenterVertically)
                            ) {
                                garbages[index].name?.let {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.subtitle1.copy(
                                            color = Color(
                                                color
                                            )
                                        )
                                    )
                                }        //标题
                                Column(
                                    modifier = Modifier.fillMaxHeight(),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    garbages[index].description?.let {
                                        Text(                                                        //描述
                                            text = it,
                                            style = MaterialTheme.typography.subtitle2
                                        )
                                    }
                                }

                            }
                            isCollected = if (garbages[index].likeIndex == 1) {
                                R.drawable.baseline_check_circle_24
                            } else {
                                R.drawable.baseline_note_add_24
                            }
                            val coroutineScope = rememberCoroutineScope()
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(0.8f)
                                    .align(Alignment.CenterVertically)
                                    .clickable(enabled = isClickable) {
                                        showProgress = true
                                        isClickable = false
                                        garbages[index].likeIndex =
                                            if (garbages[index].likeIndex == 1) 0 else 1
                                        coroutineScope.launch {
                                            withContext(Dispatchers.IO) {
                                                if (dao != null) {
                                                    dao.updateGarbageLikeIndexAll(
                                                        garbages[index].id,
                                                        if (garbages[index].likeIndex == 1) 1 else 0
                                                    )
                                                    //   Log.d("TAG likeIndex ******:", "value: ${dao.getById(garbages[index].id)?.likeIndex}")

                                                }
                                            }
                                            withContext(Dispatchers.Main) {
                                                Toast
                                                    .makeText(
                                                        requireContext(),
                                                        getString(R.string.fg_toast),
                                                        Toast.LENGTH_SHORT
                                                    )
                                                    .show()
                                            }
                                        }

                                        // 1.5秒后再次启用点击
                                        lifecycleScope.launch {
                                            delay(1500)
                                            isClickable = true
                                            showProgress = false
                                        }
                                    }, contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = isCollected),
                                    contentDescription = "add to collection"
                                )


                            }


                        }

                    }
                }
                if (showProgress) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center)
                            .background(Color.Gray.copy(alpha = 0.4f))
                    ) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                }


            }


        }


    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentTwo.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentTwo().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}


