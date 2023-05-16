package com.cqupt.garbagesorter.fragment

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.cqupt.garbagesorter.R
import com.cqupt.garbagesorter.activity.DetailActivity
import com.cqupt.garbagesorter.activity.ImageUploadActivity
import com.cqupt.garbagesorter.activity.SearchActivity


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentOne.newInstance] factory method to
 * create an instance of this fragment.
 */

class FragmentOne : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var toolbar: Toolbar
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

        val view = inflater.inflate(R.layout.fragment_one, container, false)

        // 查找 ImageView 控件并设置单击事件
        view.findViewById<ImageView>(R.id.image_other_garbage).setOnClickListener {
            startDetailActivity(resources.getString(R.string.type3))
        }

        view.findViewById<ImageView>(R.id.image_harmful_garbage).setOnClickListener {
            startDetailActivity(resources.getString(R.string.type4))
        }

        view.findViewById<ImageView>(R.id.image_recyclable_garbage).setOnClickListener {
            startDetailActivity(resources.getString(R.string.type1))
        }

        view.findViewById<ImageView>(R.id.image_wet_garbage).setOnClickListener {
            startDetailActivity(resources.getString(R.string.type2))
        }

        //设置toolbar
        val btn = view.findViewById<ImageView>(R.id.btn_add_photo)
        btn.setOnClickListener { startImageUploadActivity() }



        var editText = view.findViewById<EditText>(R.id.fragment_one_edit)
        editText.setOnClickListener { startSearchActivity() }

        return view
    }

    private fun startImageUploadActivity() {
        val intent = Intent(activity, ImageUploadActivity::class.java)
        startActivity(intent)
    }

    private fun startSearchActivity() {
        val intent = Intent(activity,SearchActivity::class.java)
        startActivity(intent)
    }

    private fun startDetailActivity(garbageType: String) {
        val intent = Intent(activity, DetailActivity::class.java)
        intent.putExtra("garbage_type", garbageType)
        startActivity(intent)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentOne.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentOne().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun updateData(data: Any) {
        // 处理数据更新的逻辑
    }
}