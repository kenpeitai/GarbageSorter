package com.cqupt.garbagesorter.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.cqupt.garbagesorter.R
import com.cqupt.garbagesorter.activity.GarbageInfoActivity
import com.cqupt.garbagesorter.db.MyDatabase
import com.cqupt.garbagesorter.db.bean.Garbage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentThree.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentThree : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_three, container, false)
        toolbar = view.findViewById(R.id.fragment3_toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = "我的收藏"
        composeView = view.findViewById(R.id.fragment3_composeView)
        composeView.setContent {
            SetComposeView()

        }

        return view
    }


    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun SetComposeView() {
        val datebase = MyDatabase.getDatabase(LocalContext.current)
        var garbages = remember { mutableStateListOf<Garbage>() }
        var count by remember { mutableStateOf(0) }
        var showProgress by remember { mutableStateOf(false) }
        var showDialog by remember { mutableStateOf(false) }
        var currentItem by remember { mutableStateOf(-1) }
        LaunchedEffect(key1 = garbages, key2 = count, block = {
            withContext(Dispatchers.IO) {

                val result = datebase.GarbageDao()?.getCollection(1)
                garbages.clear()
                if (result != null) {
                    garbages.addAll(result)
                }
                delay(500)
                showProgress = false
            }
        })

        Column(modifier = Modifier.padding(15.dp)) {
            Row(
                modifier = Modifier
                    .height(0.dp)
                    .fillMaxWidth()
                    .background(color = Color.DarkGray)
            ) {
            }
            val refreshState = rememberPullRefreshState(refreshing = false, onRefresh = {
                count++
            //    Toast.makeText(requireContext(), "刷新中 count:$count", Toast.LENGTH_SHORT).show()
                showProgress = true
            })
            Box(modifier = Modifier.pullRefresh(state = refreshState, enabled = true)) {
                LazyColumn() {
                    items(garbages.size) { index: Int ->
                        var expanded by remember { mutableStateOf(false) }
                        var selectedIndex by remember { mutableStateOf(0) }
                        val items = listOf("取消收藏", "分享", "Option 3")

                        Row(
                            modifier = Modifier
                                .wrapContentSize()
                                .fillMaxWidth()
                                .shadow(
                                    elevation = 4.dp,
                                    shape = RoundedCornerShape(8.dp),
                                    clip = true
                                )
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color.White)
                                .clickable {
                                    val intent = Intent(context, GarbageInfoActivity::class.java)
                                    intent.putExtra("EXTRA_GARBAGE", garbages[index].id)
                                    startActivity(intent)
                                }
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                val name = "a" + garbages[index].id + "_1"
                                val drawableId = requireContext().resources.getIdentifier(
                                    name,
                                    "drawable",
                                    requireContext().packageName
                                )
                                Image(
                                    painter = painterResource(id = drawableId),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .padding(horizontal = 5.dp, vertical = 8.dp)
                                        .align(Alignment.CenterHorizontally)
                                        .clip(shape = RoundedCornerShape(8.dp))
                                )
                                garbages[index].name?.let {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = it,
                                            modifier = Modifier
                                                .align(Alignment.Center)
                                                .padding(bottom = 10.dp),
                                            style = MaterialTheme.typography.subtitle1
                                        )
                                    }

                                }
                            }
                            Column(modifier = Modifier.weight(1.5f)) {
                                Row(modifier = Modifier.padding(5.dp)) {

                                    Icon(
                                        imageVector = Icons.Filled.ArrowDropDown,
                                        contentDescription = "",
                                        modifier = Modifier
                                            .size(30.dp)
                                            .clickable { expanded = true }
                                    )

                                        DropdownMenu(
                                            expanded = expanded,
                                            onDismissRequest = { expanded = false },
                                            modifier = Modifier
                                                .wrapContentSize()
                                                .padding(0.dp)
                                        ) {
                                            items.forEachIndexed { index1, item ->
                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 0.dp)
                                                ) {
                                                    DropdownMenuItem(
                                                        modifier = Modifier
                                                            .height(35.dp)
                                                            .padding(vertical = 0.dp), onClick = {
                                                            selectedIndex = index1
                                                            expanded = false
                                                            when (selectedIndex) {
                                                                0 -> {
                                                                    showDialog = true
                                                                    currentItem = index
                                                                }
                                                                1 -> {Share(garbages[index])}
                                                                2 -> {}
                                                                else -> {}
                                                            }
                                                        }) {
                                                        Row(horizontalArrangement = Arrangement.Center) {
                                                            when(index1){
                                                                0-> Icon(imageVector = Icons.Filled.Clear, contentDescription = "", modifier = Modifier.align(Alignment.CenterVertically))
                                                                1-> Icon(imageVector = Icons.Filled.Share, contentDescription = "", modifier = Modifier.align(Alignment.CenterVertically))
                                                                2-> Icon(imageVector = Icons.Filled.Warning, contentDescription = "", modifier = Modifier.align(Alignment.CenterVertically))
                                                            }
                                                            Text(text = item, modifier = Modifier.padding(start = 5.dp), textAlign = TextAlign.Center)
                                                        }
                                                                                                        }


                                                }

                                            }


                                        }


                                    garbages[index].type?.let {
                                        Text(
                                            text = it,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .align(Alignment.CenterVertically),
                                            style = TextStyle(
                                                fontStyle = FontStyle.Italic,
                                                fontWeight = FontWeight.Light
                                            )
                                        )
                                    }

                                }
                                garbages[index].description?.let {
                                    Text(
                                        text = it, modifier = Modifier
                                            .padding(5.dp)
                                            .align(Alignment.CenterHorizontally)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
                PullRefreshIndicator(
                    refreshing = false,
                    state = refreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
                if (showProgress) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                if (showDialog){
                    AlertDialog(onDismissRequest = { showDialog = false },
                        title = {Text(text = "Dialog Title")},
                        text = { Text(text = "Dialog Message") },
                        confirmButton = {
                            TextButton(onClick = {
                                showDialog = false
                                lifecycleScope.launch{
                                    withContext(Dispatchers.IO){
                                        datebase.GarbageDao()?.updateGarbageLikeIndex(id = garbages[currentItem].id, likeIndex = 0)
                                        withContext(Dispatchers.Main){
                                            currentItem = -1
                                            Toast.makeText(requireContext(),"删除成功",Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                }
                            }) {
                                Text(text = "OK")
                            }
                        }


                    )
                        

                }
            }
        }
    }

    private fun Share(garbage: Garbage) {
        var name = "a" + garbage.id + "_2"
        var drawableId = requireContext().resources.getIdentifier(
            name,
            "drawable",
            requireContext().packageName
        )
        val inputStream = resources.openRawResource(drawableId)
        val imageFile = File(requireContext().cacheDir,name)
        imageFile.outputStream().use { out->
            inputStream.copyTo(out)
        }
        val imageUri = Uri.parse("android.resource://${requireContext().packageName}/$drawableId")
        val permission = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "垃圾分类，人人有责： ${garbage.name} 属于 ${garbage.type}, ${garbage.description}")
            putExtra(Intent.EXTRA_STREAM, imageUri)
            type = "image/jpeg"
        }
        val shareIntent = Intent.createChooser(sendIntent, "分享到...")
        startActivity(shareIntent)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentThree.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentThree().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}