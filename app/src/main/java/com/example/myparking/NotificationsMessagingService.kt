package com.example.myparking

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.myparking.models.NotificationModel
import com.example.myparking.repositories.NotificationsRepository
import com.google.firebase.messaging.RemoteMessage
import com.pusher.pushnotifications.fcm.MessagingService
import java.text.SimpleDateFormat
import java.util.*



class NotificationsMessagingService : MessagingService() {
    private val dateFormatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.FRANCE)
    private val CHANNEL_ID = "446"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("NOTIF_ARRIVED",remoteMessage.data.toString())
        val date = dateFormatter.format(Calendar.getInstance().time)
        val notif = NotificationModel(remoteMessage.data["title"]!!,remoteMessage.data["body"]!!,date)
        val idNotif = NotificationsRepository.getInstance(applicationContext).insertNotif(notif)
        createNotificationChannel()
        buildNotification(notif,idNotif.toInt())
    }

    private fun buildNotification(notif: NotificationModel, notificationId: Int) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.app_logo1)
            .setContentTitle(notif.title)
            .setContentText(notif.body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, builder.build())
        }
    }
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /*private fun isAppOnForeground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses ?: return false
        val packageName = context.getPackageName()
        for (appProcess in appProcesses) {
            if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName == packageName) {
                return true
            }
        }
        return false
    }*/
}