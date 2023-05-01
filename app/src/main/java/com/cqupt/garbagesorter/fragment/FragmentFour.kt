package com.cqupt.garbagesorter.fragment

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cqupt.garbagesorter.R
import com.cqupt.garbagesorter.activity.locale.LocaleManager
import com.cqupt.garbagesorter.activity.ui.theme.Teal700
import java.util.*
import kotlin.system.exitProcess

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentFour.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentFour : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        return ComposeView(requireContext()).apply {
            setContent {
                val sharedPreferences = requireContext().applicationContext.getSharedPreferences(
                    "MyAppPreferences",
                    Context.MODE_PRIVATE
                )

                var selected by remember {
                    mutableStateOf(sharedPreferences.getString("language", "default"))
                }
                var showDialog by remember { mutableStateOf(false) }
                val selectedLocale = remember { mutableStateOf(Locale.getDefault()) }
                val locales = listOf(
                    Locale.CHINA,
                    Locale.ENGLISH
                )
                Box (modifier = Modifier.fillMaxSize()){
                    Column {
                        SetBar()
                        Spacer(modifier = Modifier.heightIn(20.dp))
                        Divider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = Color.Gray,
                            thickness = 0.5.dp
                        )
                        Row(modifier = Modifier.fillMaxWidth().clickable {
                            showDialog = true
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_public_24),
                                contentDescription = "",
                                modifier = Modifier.padding(start = 15.dp)
                            )
                            Text(
                                text = resources.getString(R.string.set_language),
                                modifier = Modifier.padding(start = 10.dp),
                                style = MaterialTheme.typography.h6
                            )
                        }
                        Divider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = Color.Gray,
                            thickness = 0.5.dp
                        )

                    }
                    if (showDialog) {
                        AlertDialog(onDismissRequest = { showDialog = false },
                            title = { Text(text = resources.getString(R.string.fragment_four_dialogtitle)) },
                            confirmButton = {
                                TextButton(onClick = {
                                    showDialog = false
                                    selected?.let { Locale(it) }?.let { setAppLocale(it) }
                                    // Toast.makeText(requireContext(),"语言选择",Toast.LENGTH_SHORT).show()
                                    //  onLanguageSelected(selectedLocale.value)
                                }) {
                                    Text(text = resources.getString(R.string.fragment_four_dialogbtn))
                                }
                            },
                            text = {
                                Column {
                                    locales.forEach { locale ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(48.dp)
                                                .selectable(
                                                    selected = (selected == locale.language),
                                                    onClick = {
                                                        selected = locale.language
                                                    }
                                                )
                                                .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                                        ) {
                                            RadioButton(
                                                selected = (
                                                        selected == locale.language
                                                        ),
                                                onClick = {


                                                    selected = locale.language
                                                },
                                                modifier = Modifier
                                                    .align(Alignment.CenterVertically)
                                            )
                                            Text(
                                                text = locale.displayLanguage,
                                                style = MaterialTheme.typography.subtitle1,
                                                modifier = Modifier
                                                    .padding(start = 16.dp)
                                                    .align(Alignment.CenterVertically)
                                            )
                                        }
                                    }
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showDialog = false }) {
                                    Text(text = resources.getString(R.string.fragment_four_dialogbtn2))
                                }
                            }
                        )
                    }
                }


            }
        }
    }

    private fun setAppLocale(value: Locale) {

        val sharedPref = requireContext().applicationContext.getSharedPreferences(
            "MyAppPreferences",
            Context.MODE_PRIVATE
        )
        with(sharedPref.edit()) {
            putString("language", value.language)
            commit()
        }
        val packageManager = context?.packageManager
        val intent = context?.let { packageManager?.getLaunchIntentForPackage(it.packageName) }
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context?.startActivity(intent)
        exitProcess(0)

    }


    @Composable
    private fun SetBar() {

        TopAppBar(
            title = {
                Row() {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier.align(CenterVertically)
                    )
                    Text(
                        text = resources.getString(R.string.fragment_four_bartitle),
                        color = Color.White,
                        modifier = Modifier.align(CenterVertically)
                    )
                }

            },
            backgroundColor = Teal700
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentFour.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentFour().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}