package com.example.myparking.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.example.myparking.repositories.ParkingListRepository
import com.example.myparking.utils.PreferenceManager


class ParkingListViewModelFactory(
    private val parkingListRepository: ParkingListRepository,
    val idAutomobiliste: Int, val  prfManager: PreferenceManager
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ParkingListViewModel::class.java)) {
            return ParkingListViewModel(parkingListRepository,idAutomobiliste, prfManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
