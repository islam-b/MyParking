package com.example.myparking.viewmodels

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.myparking.models.*
import com.example.myparking.repositories.ParkingListRepository

class ParkingListViewModel (private val parkingListRepository: ParkingListRepository) : ViewModel() {
    private var filterState : MutableLiveData<FilterParkingsModel>
    var mParkingList: MutableLiveData<ArrayList<Parking>>

    init {
        filterState = MutableLiveData()
        filterState.value = FilterParkingsModel()
        mParkingList = parkingListRepository.getParkingsList(filterState)

    }
    fun getParkingsList(): MutableLiveData<ArrayList<Parking>> { // why mutubale.

        return mParkingList;
    }

   fun receiveFilter(newFilterState : FilterParkingsModel): MutableLiveData<ArrayList<Parking>> {
       filterState.value = newFilterState
      return  parkingListRepository.getParkingsList(filterState)

    }
   fun postFilteredList(newFilteredList: ArrayList<Parking>) {
       mParkingList?.value = newFilteredList
   }
    fun getFilterState(): MutableLiveData<FilterParkingsModel> {
        return filterState
    }




}