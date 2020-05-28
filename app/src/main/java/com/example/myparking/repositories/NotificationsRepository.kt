package com.example.myparking.repositories

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.myparking.models.NotificationModel
import com.example.myparking.utils.AppDatabase


class NotificationsRepository(appContext: Context) {

    private var db: AppDatabase
    private var notificationsList = MutableLiveData<ArrayList<NotificationModel>>()
    private var countNotifs = MutableLiveData<Int>()

    init {
        db = Room.databaseBuilder(
            appContext,
            AppDatabase::class.java, "myparking_app_db"
        ).allowMainThreadQueries().build() //for now allow main thread queries but transform it to asynchronous later
    }

    fun readNotif(notif: NotificationModel):MutableLiveData<ArrayList<NotificationModel>>  {
        db.notificationDao().read(notif.id_notif)
        updateCount()
        return getAllNotifs()
    }

    fun getAllNotifs(): MutableLiveData<ArrayList<NotificationModel>>{
        notificationsList.postValue(ArrayList(db.notificationDao().getAll()))
        return notificationsList
    }

    fun insertNotif(notif: NotificationModel): Long {
        val id= db.notificationDao().insert(notif)
        getAllNotifs()
        updateCount()
        return id
    }

    fun deleteNotif(notif: NotificationModel):MutableLiveData<ArrayList<NotificationModel>> {
        db.notificationDao().delete(notif)
        updateCount()
        return getAllNotifs()
    }

    fun updateCount():MutableLiveData<Int> {
        countNotifs.postValue(db.notificationDao().count())
        return countNotifs
    }

    companion object {
        @Volatile
        private var instance: NotificationsRepository? = null
        fun getInstance(applicationContext: Context) =
            instance ?: synchronized(this) {
                instance ?: NotificationsRepository(applicationContext).also { instance = it }
            }
    }


}