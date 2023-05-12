package com.cqupt.garbagesorter.activity

import android.content.Intent
import android.os.Bundle
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
import androidx.lifecycle.lifecycleScope
import com.cqupt.garbagesorter.R
import com.cqupt.garbagesorter.activity.base.BaseActivity
import com.cqupt.garbagesorter.db.MyDatabase
import com.cqupt.garbagesorter.db.bean.Garbage
import com.cqupt.garbagesorter.db.bean.SearchHistory
import com.cqupt.garbagesorter.db.dao.GarbageDao
import com.cqupt.garbagesorter.db.dao.HistoryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class SearchActivity : BaseActivity() {
    lateinit var toolbar: Toolbar
    lateinit var composeView: ComposeView
    lateinit var database: MyDatabase
    lateinit var dao:GarbageDao
    lateinit var hdao:HistoryDao
    private var refreahIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initToolbar()

        initSearchView()


    }


    private fun initSearchView() {

        composeView = findViewById(R.id.searchactivity_composeView)
        val context = applicationContext
         database = MyDatabase.getDatabase(this)
         dao = database.GarbageDao()!!
         hdao = database.historyDao()!!

        composeView.setContent {

            var searchText by remember { mutableStateOf("") }
            var searchResult = remember { mutableStateListOf<Garbage>() }
            val scope = rememberCoroutineScope()
            var historyList = remember {
                mutableStateListOf<SearchHistory>()
            }
            var garbageHistoryList = remember {
                mutableStateListOf<Garbage>()
            }

            LaunchedEffect(key1 = refreahIndex, block = {
                withContext(Dispatchers.IO){
                    historyList.clear()
                    historyList.addAll(hdao.getAllSearchHistory())
                    historyList.sortByDescending { it.searchtimes }
                    garbageHistoryList.clear()
                    historyList.map { i ->
                        dao.getByIdChooser(i.garbageId.toString(),this@SearchActivity)
                            ?.let { garbageHistoryList.add(it) }
                    }
                }

            })

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
                                    val result = dao?.getGarbageListByNameChooser("%$searchText%",this@SearchActivity)
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
                if(searchText == ""){
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(garbageHistoryList.size) { index ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .clickable { /* 处理点击事件 */ },
                                elevation = 4.dp
                            ) {
                                Text(
                                    text = garbageHistoryList[index].name!!,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }

                }
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
                                    updateHistory(searchResult[index])
                                }
                        ) {
                            searchResult[index].name?.let {
                                Text(
                                    text = it,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 16.dp)
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
                                        resources.getString(R.string.type1) -> Color(0xFF3162A7)
                                        resources.getString(R.string.type2) -> Color(0xFF1C7070)
                                        resources.getString(R.string.type3) -> Color(0xFF56686C)
                                        resources.getString(R.string.type4) -> Color(0xFFA42B3E)
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

    private fun updateHistory(garbage: Garbage) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO){
                var g = hdao.getSearchHistoryByGarbageId(garbage.id.toInt())
                if (g == null) {
                    hdao.insertSearchHistory(SearchHistory(searchtimes = 1, time = getCurrentTimeString(), garbageId = garbage.id.toInt()))
                }else{
                    g.searchtimes++
                    hdao.updateSearchTimes(g)
                }
            withContext(Dispatchers.Main){
                refreahIndex ++
            }
            }
        }

    }
    fun getCurrentTimeString(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return currentDateTime.format(formatter)
    }

    private fun initToolbar() {
        toolbar = findViewById(R.id.searchactivity_toolbar)
        setSupportActionBar(toolbar)
        val title1 = resources.getString(R.string.searchactivity_bartitle)
        supportActionBar?.title = title1

    }
}
