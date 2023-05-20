package com.cqupt.garbagesorter.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.core.app.NotificationCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import com.cqupt.garbagesorter.R
import com.cqupt.garbagesorter.activity.base.BaseActivity
import com.cqupt.garbagesorter.fragment.FragmentOne
import com.cqupt.garbagesorter.service.CheckNotifyPermissionUtils
import com.cqupt.garbagesorter.service.MyForegroundService
import com.cqupt.garbagesorter.viewpager.MyPagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView


/*
 * Copyright (c) 2023, harakuroizero@gmail.com All Rights Reserved.
 * #                                                   #
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #               佛祖保佑         永无BUG            #
 * #                                                   #
*/

class MainActivity : ImageUploadActivity(),FragmentOne.OnButtonClickListener {
    private lateinit var viewPager: ViewPager
    private lateinit var bottomNavView: BottomNavigationView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        check()
     //   initNotification()


    }

    private fun check() {
        if (!CheckNotifyPermissionUtils.checkNotifyPermission(this)){
            Log.d("checkNotifyPermission---------->", "initNotification:  false")
            val builder = AlertDialog.Builder(this)
            builder.setTitle(resources.getString(R.string.dialog_title))
            builder.setMessage(resources.getString(R.string.dialog_message))
            builder.setPositiveButton(resources.getString(R.string.dialog_btn1)) { dialog, which ->
                CheckNotifyPermissionUtils.tryJumpNotifyPage(this)
                initNotification1()
            }
            builder.setNegativeButton(resources.getString(R.string.dialog_btn2), null)
            builder.show()

        }else{initNotification1()}
    }

    private fun initNotification1() {
        val intent = Intent(this, MyForegroundService::class.java)
        intent.putExtra("cmd",0)
        startService(intent)

        Log.d("startService---------->", "initNotification1:  true")
    }


    private fun initNotification() {
        val channelId = "my_channel_id"
        val channelName = "My Channel"
        val channelDescription = "My Channel Description"

        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = channelDescription
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val intent = Intent(this, SearchActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

        val title = "应用运行中"
        val content = "点击搜索"
        val smallIcon = R.drawable.baseline_search_24
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(smallIcon)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)


        val notificationId = 1001
        notificationManager.notify(notificationId, builder.build())

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

    override fun onButtonClick() {
        check1(this)
    }
}