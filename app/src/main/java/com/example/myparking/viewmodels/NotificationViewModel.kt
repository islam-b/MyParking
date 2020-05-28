package com.example.myparking.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myparking.models.NotificationModel
import com.example.myparking.repositories.NotificationsRepository
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NotificationViewModel(
    private val notificationRepository: NotificationsRepository
) : ViewModel() {
    private var notificationsList : MutableLiveData<ArrayList<NotificationModel>>
    private var notificationsCount : MutableLiveData<Int>
    val dateFormatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.FRANCE)

    init {
        notificationsList = notificationRepository.getAllNotifs()
        notificationsCount = notificationRepository.updateCount()
    }

    fun readNotif(notif: NotificationModel): MutableLiveData<ArrayList<NotificationModel>>  {
       return notificationRepository.readNotif(notif)
    }

    fun getAllNotifs(): MutableLiveData<ArrayList<NotificationModel>>{
        return notificationRepository.getAllNotifs()
    }

    fun insertNotif(data: Map<String,String>): Long {
        val date = dateFormatter.format(Calendar.getInstance().time)
        val notif = NotificationModel(data["title"]!!,data["body"]!!,date)
        return notificationRepository.insertNotif(notif)
    }

    fun deleteNotif(notif: NotificationModel): MutableLiveData<ArrayList<NotificationModel>> {
        return notificationRepository.deleteNotif(notif)
    }
    fun undoDelete(notif: NotificationModel): Long {
        return notificationRepository.insertNotif(notif)
    }

    fun getCount(): MutableLiveData<Int> {
        return notificationsCount
    }

}