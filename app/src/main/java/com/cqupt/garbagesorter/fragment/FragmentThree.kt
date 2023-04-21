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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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

    @Composable
    private fun SetComposeView() {
        val datebase = MyDatabase.getDatabase(LocalContext.current)
        val garbages = remember { mutableStateListOf<Garbage>() }
        LaunchedEffect(key1 = garbages, block = {
            withContext(Dispatchers.IO) {
                val result = datebase.GarbageDao()?.getCollection(1)
                garbages.clear()
                if (result != null) {
                    garbages.addAll(result)
                }
            }
        })

        Column(modifier = Modifier.padding(15.dp)) {
            Row(
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth()
                    .background(color = Color.DarkGray)
            ) {
            }
            LazyColumn() {
                items(garbages.size) { index: Int ->
                    Row(
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
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
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            garbages[index].name?.let {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){ Text(
                                    text = it, modifier = Modifier
                                        .fillMaxSize()
                                        .align(Alignment.Center),
                                )}

                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Row(horizontalArrangement = Arrangement.End,modifier = Modifier.padding(5.dp)) {
                                Image(
                                    painter = painterResource(id = R.drawable.baseline_expand_more_24),
                                    contentDescription ="",
                                )
                                garbages[index].type?.let { Text(text = it, modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterVertically)) }
                            }
                            garbages[index].description?.let { Text(text = it, modifier = Modifier
                                .padding(5.dp)
                                .align(Alignment.CenterHorizontally)) }
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