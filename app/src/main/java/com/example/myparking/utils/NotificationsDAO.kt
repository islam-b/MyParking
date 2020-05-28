package com.example.myparking.utils

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Insert
import com.example.myparking.models.NotificationModel

@Dao
interface NotificationsDAO {
    @Query("SELECT * FROM notification")
    fun getAll(): List<NotificationModel>

    @Query("UPDATE notification SET read = 'true' WHERE id_notif = (:id_notif)")
    fun read(id_notif: Int)

    @Query("SELECT COUNT(id_notif) FROM notification WHERE read= 'false'")
    fun count(): Int

    @Insert
    fun insert(notification: NotificationModel): Long

    @Delete
    fun delete(notification: NotificationModel)
}