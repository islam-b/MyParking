/*
 * Copyright (c) 2011-2020 HERE Europe B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.myparking.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder

import androidx.core.app.NotificationCompat

import com.example.myparking.MainActivity
import com.example.myparking.R


class ForegroundService : Service() {

    override fun onCreate() {
        super.onCreate()
        initChannels(this.applicationContext)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent.action == START_ACTION) {
            val notificationIntent = Intent(this, MainActivity::class.java)
            notificationIntent.action = Intent.ACTION_MAIN
            notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER)

            val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

            val notification = NotificationCompat.Builder(this.applicationContext, CHANNEL)
                .setContentTitle("Navigation MyParking")
                .setContentText("Navigation en cours...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setLocalOnly(true)
                .build()

            startForeground(FOREGROUND_SERVICE_ID, notification)
        } else if (intent.action == STOP_ACTION) {
            stopForeground(true)
            stopSelf()
        }

        return Service.START_NOT_STICKY
    }

    fun initChannels(context: Context) {
        if (Build.VERSION.SDK_INT < 26) {
            return
        }
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            CHANNEL, "Foreground channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Channel for foreground service"
        notificationManager.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        // Used only in case of bound services.
        return null
    }

    companion object {

        var FOREGROUND_SERVICE_ID = 101

        var START_ACTION = "com.here.android.example.guidance.fs.action.start"
        var STOP_ACTION = "com.here.android.example.guidance.fs.action.stop"

        private val CHANNEL = "default"
    }
}