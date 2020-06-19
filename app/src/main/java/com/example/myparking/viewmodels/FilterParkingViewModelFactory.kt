package com.example.myparking.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myparking.utils.PreferenceManager

class FilterParkingViewModelFactory(
    val idAutomobiliste: Int,
    val  prfManager: PreferenceManager
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilterParkingsViewModel::class.java)) {
            return FilterParkingsViewModel(idAutomobiliste,prfManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}