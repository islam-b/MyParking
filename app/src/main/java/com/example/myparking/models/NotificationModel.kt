package com.example.myparking.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "notification")
data class NotificationModel(
    @ColumnInfo(name="title") val title:String,
    @ColumnInfo(name="body") val body:String,
    @ColumnInfo(name="date") val date: String,
    @ColumnInfo(name="read") val read: String= "false",
    @PrimaryKey(autoGenerate = true) val id_notif: Int=0)  : Comparable< NotificationModel >  {


    override fun compareTo(other: NotificationModel): Int {
        try {
            val dateFormatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.FRANCE)
            val thisDate = dateFormatter.parse(date)
            val otherDate = dateFormatter.parse(other.date)
            return otherDate!!.compareTo(thisDate)
        }catch (e: Exception) {
            return 0
        }
    }
}