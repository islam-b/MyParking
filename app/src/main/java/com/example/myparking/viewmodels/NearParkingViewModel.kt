package com.example.myparking.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myparking.models.Parking
import com.example.myparking.repositories.ParkingListRepository

class NearParkingViewModel(
    private val index: Int,
    private val parkingListRepository: ParkingListRepository
) : ViewModel() {
    private var mParking: MutableLiveData<Parking> = MutableLiveData<Parking>()

    init {
        mParking.value = parkingListRepository.getParkings()[index]
    }

    fun getParkingItem(): LiveData<Parking> { // can't change it only observe
        return mParking;
    }


    fun getName(): String {
        return mParking.value?.nom!!
    }
    fun getTarif(): String {
        return mParking.value?.tarifs?.get(0)?.prix!!
    }
    fun getWalkTime(): String {
        return "5 min de marche";
    }



}