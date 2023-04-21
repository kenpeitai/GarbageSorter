package com.cqupt.garbagesorter.activity

import android.content.Context
import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView

import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.room.Room
import com.cqupt.garbagesorter.R
import com.cqupt.garbagesorter.db.MyDatabase
import com.cqupt.garbagesorter.db.bean.Garbage
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SearchActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var composeView: ComposeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initToolbar()

        initSearchView()

    }

    private fun initSearchView() {

        composeView = findViewById(R.id.searchactivity_composeView)
        val context = applicationContext
        val database = MyDatabase.getDatabase(this)
        val dao = database.GarbageDao()

        composeView.setContent {

            var searchText by remember { mutableStateOf("") }
            var searchResult = remember { mutableStateListOf<Garbage>() }
            val scope = rememberCoroutineScope()


            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(Color.White)
            ) {
                TextField(
                    value = searchText,
                    onValueChange = { text ->
                        searchText = text
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                if (searchText.isNotEmpty()) {
                                    val result = dao?.getGarbageListByName("%$searchText%")
                                    searchResult.clear()
                                    if (result != null) {
                                        searchResult.addAll(result)
                                    }

                                } else {
                                    searchResult = mutableStateListOf()
                                }
                            }
                        }
                    },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.surface),
                    placeholder = { Text(text = "Search") },
                    trailingIcon = {
                        if (searchText.isNotEmpty()) {
                            IconButton(onClick = { searchText = "" }) {
                                Icon(Icons.Filled.Clear, contentDescription = null)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 40.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            // 执行搜索操作
                        }
                    )
                )

                LazyColumn {
                    items(searchResult.size) { index ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .clickable {
                                    val intent = Intent(context, GarbageInfoActivity::class.java)
                                    intent.putExtra("EXTRA_GARBAGE", searchResult[index].id)
                                    startActivity(intent)

                                }
                        ) {
                            searchResult[index].name?.let {
                                Text(
                                    text = it,
                                    modifier = Modifier.weight(1f).padding(start = 16.dp)
                                )
                            }
                            searchResult[index].type?.let {
                                Text(
                                    text = it,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 16.dp),
                                    color = when (searchResult[index].type) {
                                        "可回收物" -> Color(0xFF3162A7)
                                        "厨余垃圾" -> Color(0xFF1C7070)
                                        "其他垃圾" -> Color(0xFF56686C)
                                        "有害垃圾" -> Color(0xFFA42B3E)
                                        else -> Color.DarkGray
                                    }
                                )
                            }

                        }
                        Divider(color = Color.Gray, thickness = 1.dp)
                    }
                }


            }


        }


    }


    private fun initToolbar() {
        toolbar = findViewById(R.id.searchactivity_toolbar)
        setSupportActionBar(toolbar)
        val title1 = "搜索"
        supportActionBar?.title = title1

    }
}
