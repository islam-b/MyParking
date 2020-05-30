package com.example.myparking.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myparking.models.Parking
import com.example.myparking.repositories.ParkingListRepository

class ParkingItemViewModelFactory(
    private val index: Int,
    private val list: ArrayList<Parking>
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ParkingItemViewModel::class.java)) {
            return ParkingItemViewModel(index, list) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}