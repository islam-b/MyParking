package com.example.myparking.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myparking.repositories.NotificationsRepository

class NotificationViewModelFactory(
    private val notificationsRepository: NotificationsRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            return NotificationViewModel(notificationsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}