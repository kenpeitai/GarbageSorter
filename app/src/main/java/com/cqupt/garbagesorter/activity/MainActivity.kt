package com.cqupt.garbagesorter.activity

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import com.cqupt.garbagesorter.R
import com.cqupt.garbagesorter.fragment.FragmentOne
import com.cqupt.garbagesorter.viewpager.MyPagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var fragmentManager: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()

    }



    private fun initView() {
        viewPager = findViewById(R.id.view_pager)
        bottomNavView = findViewById(R.id.bottom_nav)

        val pagerAdapter = MyPagerAdapter(supportFragmentManager)
        viewPager.adapter = pagerAdapter

        bottomNavView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_item_one -> viewPager.currentItem = 0
                R.id.menu_item_two -> viewPager.currentItem = 1
                R.id.menu_item_three -> viewPager.currentItem = 2
                R.id.menu_item_four -> viewPager.currentItem = 3
            }
            true
        }
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                // Ignore
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        bottomNavView.menu.findItem(R.id.menu_item_one).isChecked = true
                        bottomNavView.menu.findItem(R.id.menu_item_one).icon!!.setTintList(
                            ColorStateList.valueOf(Color.WHITE)
                        )
                    }
                    1 -> {
                        bottomNavView.menu.findItem(R.id.menu_item_two).isChecked = true
                        bottomNavView.menu.findItem(R.id.menu_item_two).icon!!.setTintList(
                            ColorStateList.valueOf(Color.WHITE)
                        )
                    }
                    2 -> {
                        bottomNavView.menu.findItem(R.id.menu_item_three).isChecked = true
                        bottomNavView.menu.findItem(R.id.menu_item_three).icon!!.setTintList(
                            ColorStateList.valueOf(Color.WHITE)
                        )
                    }
                    3 -> {
                        bottomNavView.menu.findItem(R.id.menu_item_four).isChecked = true
                        bottomNavView.menu.findItem(R.id.menu_item_four).icon!!.setTintList(
                            ColorStateList.valueOf(Color.WHITE)
                        )
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                // Ignore
            }
        })
    }
}