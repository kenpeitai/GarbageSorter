package com.cqupt.garbagesorter.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.cqupt.garbagesorter.fragment.FragmentFour
import com.cqupt.garbagesorter.fragment.FragmentOne
import com.cqupt.garbagesorter.fragment.FragmentThree
import com.cqupt.garbagesorter.fragment.FragmentTwo


class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> FragmentOne()
                1 -> FragmentTwo()
                2 -> FragmentThree()
                3 -> FragmentFour()
                else -> FragmentOne()
            }
        }

        override fun getCount(): Int {
            return 4
        }
    }
