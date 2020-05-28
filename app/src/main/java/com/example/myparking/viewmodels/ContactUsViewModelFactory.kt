package com.example.myparking.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myparking.repositories.ContactUsRepository

class ContactUsViewModelFactory  (
    private val automobilisteId: Int,
        private val contactUsRepository: ContactUsRepository
    ) :
        ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ContactUsViewModel::class.java)) {
                return ContactUsViewModel(automobilisteId, contactUsRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}