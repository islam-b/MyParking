package com.example.myparking.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myparking.repositories.ParkingListRepository

class ParkingItemViewModelFactory(
    private val index: Int,
    private val parkingListRepository: ParkingListRepository
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ParkingItemViewModel::class.java)) {
            return ParkingItemViewModel(index, parkingListRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}