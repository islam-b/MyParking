package com.example.myparking.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myparking.repositories.ReservationListRepository

class ReservationItemViewModelFactory(
    private val index : Int,
    private val id: Int,
    private val reservationListRepository: ReservationListRepository
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReservationItemViewModel::class.java)) {
            return ReservationItemViewModel(index,id, reservationListRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}