package com.example.myparking.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myparking.models.*
import com.example.myparking.repositories.ParkingListRepository

class ParkingListViewModel (private val parkingListRepository: ParkingListRepository) : ViewModel() {
/*    private var filterState = MutableLiveData<FilterParkingsModel>()*/
    private var mParkingList: MutableLiveData<ArrayList<Parking>> = parkingListRepository.getParkingsList(/*filterState*/)

    fun getParkingsList(): LiveData<ArrayList<Parking>> { // can't change it only observe

        return mParkingList;
    }

/*    fun receiveFilter(newFilterState : FilterParkingsModel){
        filterState.postValue(newFilterState)
    }*/



}