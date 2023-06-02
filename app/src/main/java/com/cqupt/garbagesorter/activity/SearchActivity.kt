package com.cqupt.garbagesorter.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
    lateinit var dao: GarbageDao
    lateinit var hdao: HistoryDao
    private var refreahIndex = 0
    private var searchIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initToolbar()

        initSearchView()


    }


    @OptIn(ExperimentalLayoutApi::class, ExperimentalMaterialApi::class)
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
            var showDialog by remember { mutableStateOf(false) }
            LaunchedEffect(key1 = refreahIndex, block = {
                withContext(Dispatchers.IO) {
                    historyList.clear()
                    historyList.addAll(hdao.getTop15History())
                    historyList.sortByDescending { it.searchtimes }
                    garbageHistoryList.clear()
                    historyList.map { i ->
                        dao.getByIdChooser(i.garbageId.toString(), this@SearchActivity)
                            ?.let { garbageHistoryList.add(it) }
                    }
                }

            })
            LaunchedEffect(key1 = searchIndex,key2 = refreahIndex, block = {
                withContext(Dispatchers.IO) {
                    if (searchText.isNotEmpty()) {
                        val result =
                            dao?.getGarbageListByNameChooser("%$searchText%", this@SearchActivity)
                        searchResult.clear()
                        if (result != null) {
                            searchResult.addAll(result)
                        }

                    } else {
                        searchResult = mutableStateListOf()
                    }
                }

            })
            Box(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(Color.White)
                ) {
                    TextField(
                        value = searchText,
                        onValueChange = { text ->
                            searchText = text
                            searchIndex++
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
                    if (searchText == "") {
                        if (garbageHistoryList.size != 0) {
                            FlowRow(modifier = Modifier.fillMaxWidth()) {
                                repeat(garbageHistoryList.size) { index ->

                                    Card(
                                        modifier = Modifier
                                            .wrapContentSize()
                                            .padding(horizontal = 4.dp, vertical = 5.dp)
                                            .clickable {
                                                searchText = garbageHistoryList[index].name!!
                                                refreahIndex++
                                            },
                                        elevation = 8.dp,
                                        shape = RoundedCornerShape(18.dp)

                                    ) {
                                        Row {
                                            Text(
                                                text = "●",
                                                modifier = Modifier
                                                    .padding(start = 9.dp)
                                                    .align(Alignment.CenterVertically),
                                                color = when (garbageHistoryList[index].type) {
                                                    resources.getString(R.string.type1) -> Color(
                                                        0xFF3162A7
                                                    )
                                                    resources.getString(R.string.type2) -> Color(
                                                        0xFF1C7070
                                                    )
                                                    resources.getString(R.string.type3) -> Color(
                                                        0xFF56686C
                                                    )
                                                    resources.getString(R.string.type4) -> Color(
                                                        0xFFA42B3E
                                                    )
                                                    else -> Color.DarkGray
                                                },
                                                textAlign = TextAlign.Center
                                            )

                                            Text(
                                                text = garbageHistoryList[index].name!!,
                                                modifier = Modifier.padding(
                                                    end = 15.dp,
                                                    start = 5.dp,
                                                    top = 5.dp,
                                                    bottom = 5.dp
                                                ),
                                                textAlign = TextAlign.Center
                                            )
                                        }

                                    }

                                }
                                Card(
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .padding(horizontal = 4.dp, vertical = 5.dp)
                                        .clickable { showDialog = true },
                                    elevation = 8.dp,
                                    shape = RoundedCornerShape(18.dp)

                                ) {
                                    Row {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = null,
                                            tint = Color.Gray,
                                            modifier = Modifier
                                                .padding(start = 9.dp)
                                                .align(
                                                    Alignment.CenterVertically
                                                )
                                        )
                                        Text(
                                            text = resources.getString(R.string.delete_history),
                                            modifier = Modifier.padding(
                                                end = 15.dp,
                                                start = 5.dp,
                                                top = 5.dp,
                                                bottom = 5.dp
                                            ), color = Color.Gray,
                                            textAlign = TextAlign.Center
                                        )
                                    }


                                }

                            }
                        }

                    } else {
                        if (searchResult.size == 0) {
                            Column() {
                                Row(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)) {
                                    Text(
                                        text = getString(R.string.search_no_results),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                            }
                                Divider(color = Color.Gray, thickness = 1.dp)

                            }
                        }else{
                            LazyColumn {
                                items(searchResult.size) { index ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                            .clickable {
                                                val intent =
                                                    Intent(context, GarbageInfoActivity::class.java)
                                                intent.putExtra(
                                                    "EXTRA_GARBAGE",
                                                    searchResult[index].id
                                                )
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
                                                    resources.getString(R.string.type1) -> Color(
                                                        0xFF3162A7
                                                    )
                                                    resources.getString(R.string.type2) -> Color(
                                                        0xFF1C7070
                                                    )
                                                    resources.getString(R.string.type3) -> Color(
                                                        0xFF56686C
                                                    )
                                                    resources.getString(R.string.type4) -> Color(
                                                        0xFFA42B3E
                                                    )
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
                if (showDialog) {
                    AlertDialog(onDismissRequest = { showDialog = false },
                        title = { Text(text = getString(R.string.delete_history_title)) },
                        text = { Text(text = getString(R.string.delete_history_text)) },
                        confirmButton = {
                            TextButton(onClick = {
                                showDialog = false
                                deleteHistory()

                                garbageHistoryList.clear()
                          //      refreahIndex++
                            }) {
                                Text(text = getString(com.google.android.material.R.string.mtrl_picker_confirm))
                            }
                        }, dismissButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text(text = getString(com.google.android.material.R.string.mtrl_picker_cancel))
                            }
                        }
                    )


                }

            }


        }


    }

    private fun deleteHistory() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                hdao.clearHistory()
            }

        }


    }

    private fun updateHistory(garbage: Garbage) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                var g = hdao.getSearchHistoryByGarbageId(garbage.id.toInt())
                if (g == null) {
                    hdao.insertSearchHistory(
                        SearchHistory(
                            searchtimes = 1,
                            time = getCurrentTimeString(),
                            garbageId = garbage.id.toInt()
                        )
                    )
                } else {
                    g.searchtimes++
                    hdao.updateSearchTimes(g)
                }
                withContext(Dispatchers.Main) {
                    refreahIndex++
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
