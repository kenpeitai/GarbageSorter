package com.cqupt.garbagesorter.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.cqupt.garbagesorter.R
import com.cqupt.garbagesorter.db.MyDatabase
import com.cqupt.garbagesorter.db.bean.Garbage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


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
        (activity as AppCompatActivity).supportActionBar?.title = "垃圾分类"
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
        var color :Long by remember { mutableStateOf(0) }

        Column {
            Column(
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
                                    .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
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
                0 -> "可回收物"
                1 -> "厨余垃圾"
                2 -> "其他垃圾"
                3 -> "有害垃圾"
                else -> "可回收物"
            }
            color = when(selectedImage) {
                0 -> 0xFF3162A7
                1 -> 0xFF1C7070
                2 -> 0xFF56686C
                3 -> 0xFFA42B3E
                else -> 0xFF3162A7
            }
            //根据图片值查询数据库

            Log.d("selectedImage TAG-------->", "selectedImage:$selectedImage")
            val database = Room.databaseBuilder(
                requireContext(),
                MyDatabase::class.java,
                "garbage2"
            ).createFromAsset("test.db").build()
            val dao = database.GarbageDao()
            LaunchedEffect(garbages, itemType) {  //可观察的可变对象全部放进去
                withContext(Dispatchers.IO) {
                    val result = dao?.getByType(itemType)
                    garbages.clear()
                    if (result != null) {
                        garbages.addAll(result)
                        Log.d("garbage's TAG----------->", "garbage's size: ${garbages.size}")
                    }
                }
            }


                    //根据数据库返回值填充列表
            LazyColumn {
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
                    ) {
                        var name = "a" + garbages[index].id + "_0"
                        var drawableId = requireContext().resources.getIdentifier(
                            name,
                            "drawable",
                            requireContext().packageName
                        )
                        Image(
                            painter = painterResource(id = drawableId),           //展示一张图片
                            contentDescription = "Image of $name",
                            modifier = Modifier
                                .height(70.dp)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(5.dp))
                        )
                        Column(modifier = Modifier.padding(end = 35.dp),) {
                            garbages[index].name?.let { Text(text = it,style = MaterialTheme.typography.subtitle1.copy(color = Color(color))) }        //标题
                            Column(
                                modifier = Modifier.fillMaxHeight(),
                                verticalArrangement = Arrangement.Center
                            ) {
                                garbages[index].description?.let {
                                    Text(                                                        //描述
                                        text = it,
                                        style = MaterialTheme.typography.subtitle2
                                    ) }
                            }

                        }

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