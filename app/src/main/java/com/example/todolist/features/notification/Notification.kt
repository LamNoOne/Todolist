package com.example.todolist.features.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.todolist.MainActivity
import com.example.todolist.R

class MyNotification (private val context: Context, private val title: String, private val message: String) {
    private val channelID: String = "FCM100"
    private val channelName: String = "FCMessage"
    private val notificationManager = context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var notificationBuilder: NotificationCompat.Builder

    @RequiresApi(Build.VERSION_CODES.O)
    fun fireNotification() {
        notificationChannel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
            enableVibration(true)
            enableLights(true)
        }
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        notificationManager.createNotificationChannel(notificationChannel)
        notificationBuilder = NotificationCompat.Builder(context, channelID).apply {
            setSmallIcon(R.drawable.ic_launcher_background)
            addAction(R.drawable.ic_launcher_background, "Open Message", pendingIntent)
            setContentIntent(pendingIntent)
            setContentTitle(title)
            setContentText(message.toString())
            setAutoCancel(true)
            priority = NotificationCompat.PRIORITY_HIGH
        }
        notificationManager.notify(100, notificationBuilder.build())
    }
}