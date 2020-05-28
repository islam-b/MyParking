package com.example.myparking.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myparking.models.Parking
import com.example.myparking.repositories.ContactUsRepository
import com.example.myparking.repositories.FavoriteParkingRepository
import com.example.myparking.repositories.ParkingListRepository

class ContactUsViewModel(val automobilisteId: Int, val contactUsRepository: ContactUsRepository) : ViewModel() {

    var objetTxt = MutableLiveData<String>("")
    var msgTxt = MutableLiveData<String>("")


    fun sendMessage(): MutableLiveData<Pair<Boolean,String>> {
        return contactUsRepository.sendMessage(automobilisteId, objetTxt.value!!, msgTxt.value!!)
    }
}