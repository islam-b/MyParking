package com.example.myparking.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FilterParkingViewModelFactory(
    val idAutomobiliste: Int,
    var start: String?
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilterParkingsViewModel::class.java)) {
            return FilterParkingsViewModel(idAutomobiliste,start) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}