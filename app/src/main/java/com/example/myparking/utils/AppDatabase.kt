package com.example.myparking.utils

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myparking.models.NotificationModel

@Database(entities = [NotificationModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationsDAO
}
