package com.cqupt.garbagesorter.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.cqupt.garbagesorter.R
import com.cqupt.garbagesorter.activity.SearchActivity

class MyForegroundService : Service() {
    private val NOTIFICATION_ID = 1001 // 通知的唯一标识符
    private var isRemove = false //是否需要移除

    override fun onCreate() {


        val channelId = "channel_id"
        val channelName = "My Channel"
        val importance = NotificationManager.IMPORTANCE_HIGH

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, importance)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        super.onCreate()
        // 创建通知对象

    }

    private fun getPendingIntent(): PendingIntent {
        // 点击通知栏时打开 SearchActivity
        val intent = Intent(this, SearchActivity::class.java)
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val i = intent!!.extras!!.getInt("cmd")
        isRemove = if (i == 0) {
            if (!isRemove) {
                createNotification()
            }
            true
        } else {
            //移除前台服务
            if (isRemove) {
                stopForeground(true)
            }
            false
        }


        return super.onStartCommand(intent, flags, startId)


    }

    private fun createNotification() {
        val notification = NotificationCompat.Builder(this, "channel_id")
            .setContentTitle(resources.getString(
                R.string.service_title
            ))
            .setContentText(resources.getString(R.string.service_text))
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.baseline_search_24)
            .setContentIntent(getPendingIntent())
            .build()
        // 将服务设置为前台服务
        startForeground(NOTIFICATION_ID, notification)    }

    override fun onDestroy() {
        if (isRemove) {
            stopForeground(true);
        }
        isRemove=false;
        super.onDestroy()
    }
    override fun onBind(intent: Intent): IBinder? {
       return null
    }
}