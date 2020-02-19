package com.example.myparking.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myparking.DI.APIComponent
import com.example.myparking.MyParkingApplication
import com.example.myparking.repositories.ParkingListRepository
import javax.inject.Inject


class ParkingListViewModelFactory(/*private val parkingListRepository: ParkingListRepository*/)
    : ViewModelProvider.Factory /*NewInstanceFactory()*/{
    lateinit var apiComponent: APIComponent
    @Inject
    lateinit var parkingListRepository: ParkingListRepository

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var apiComponent :APIComponent =  MyParkingApplication.apiComponent
        apiComponent.inject(this)
        if (modelClass.isAssignableFrom(ParkingListViewModel::class.java)) {
            return ParkingListViewModel(parkingListRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}