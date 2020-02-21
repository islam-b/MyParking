package com.example.myparking.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.example.myparking.repositories.ParkingListRepository


class ParkingListViewModelFactory(
    private val parkingListRepository: ParkingListRepository
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ParkingListViewModel::class.java)) {
            return ParkingListViewModel(parkingListRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
