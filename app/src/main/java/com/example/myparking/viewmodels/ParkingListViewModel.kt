package com.example.myparking.viewmodels

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.ActivityNavigator
import com.example.myparking.models.*
import com.example.myparking.repositories.ParkingListRepository

class ParkingListViewModel (private val parkingListRepository: ParkingListRepository, val idAutomobiliste: Int, var start: String?, var destination: String?) : ViewModel() {
    private var filterState : MutableLiveData<FilterParkingsModel>
    var mParkingList: MutableLiveData<ArrayList<Parking>>
    var filteredParkings : MutableLiveData<ArrayList<Parking>>
    init {
        filterState = MutableLiveData()
        filterState.value = FilterParkingsModel()
        mParkingList = parkingListRepository.getParkingsList(filterState, idAutomobiliste, start, destination)
        filteredParkings = parkingListRepository.getParkingsList(filterState, idAutomobiliste, start, destination)
    }

    fun getAllParkings(): MutableLiveData<ArrayList<Parking>> { // why mutubale.
        return  parkingListRepository.getNotFilteredParkingsList(idAutomobiliste, start, destination)
    }

    fun getFilteredParkings(newFilterState : FilterParkingsModel): MutableLiveData<ArrayList<Parking>> {
        filterState.value = newFilterState
        return  parkingListRepository.getParkingsList(filterState,idAutomobiliste, start, destination)
    }
    fun refrshFilteredParkings(): MutableLiveData<ArrayList<Parking>> {
        return  parkingListRepository.getParkingsList(filterState,idAutomobiliste, start, destination)
    }

   /*fun postFilteredList(newFilteredList: ArrayList<Parking>) {
       mParkingList?.value = newFilteredList
   }*/
    fun getFilterState(): MutableLiveData<FilterParkingsModel> {
        return filterState
    }

    fun sortByDistance () {
        val parkings = mParkingList.value!!
        val newList = ArrayList(parkings?.sortedWith(compareBy {it.routeInfo?.walkingDistance}))
        Log.d("list sortedD", newList.toString())
        mParkingList.value=newList
    }
    fun sortByPrice () {
        val parkings = mParkingList.value!!
        val newList = ArrayList(parkings?.sortedWith(compareBy {it.tarifs?.get(0).prix}))
        Log.d("list sorted", newList.toString())
        mParkingList.value=newList
    }




}