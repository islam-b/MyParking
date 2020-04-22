package com.example.myparking.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myparking.models.Automobiliste
import com.example.myparking.repositories.FavoriteParkingRepository

class FavoriteParkingViewModelFactory(
    private val favoriteParkingRepository: FavoriteParkingRepository,
    private val idAutomobiliste: Int,
    private var lastLocation: String?,
    private var destination: String?
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteParkingViewModel::class.java)) {
            return FavoriteParkingViewModel(favoriteParkingRepository,idAutomobiliste, lastLocation, destination) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}