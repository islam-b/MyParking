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
import com.example.myparking.utils.PreferenceManager

class ParkingListViewModel (private val parkingListRepository: ParkingListRepository, val idAutomobiliste: Int, val  prfManager: PreferenceManager) : ViewModel() {
    private var filterState : MutableLiveData<FilterParkingsModel>
    var mParkingList: MutableLiveData<ArrayList<Parking>>
    var filteredParkings : MutableLiveData<ArrayList<Parking>>
    init {
        filterState = MutableLiveData()
        filterState.value = FilterParkingsModel()
        val start = prfManager.getLastLocationStr()
        val destination = prfManager.getDestinationLocation()
        var dest :String? = null
        if (destination.size>0) {
            dest = destination[0].toString()+','+destination[1].toString()
        }
        mParkingList = parkingListRepository.getParkingsList(filterState, idAutomobiliste, start, dest)
        filteredParkings = parkingListRepository.getParkingsList(filterState, idAutomobiliste, start, dest)
    }

    fun getAllParkings(): MutableLiveData<ArrayList<Parking>> { // why mutubale.
        val start = prfManager.getLastLocationStr()
        val destination = prfManager.getDestinationLocation()
        var dest :String? = null
        if (destination.size>0) {
            dest = destination[0].toString()+','+destination[1].toString()
        }
        return  parkingListRepository.getNotFilteredParkingsList(idAutomobiliste, start, dest)
    }

    fun getFilteredParkings(newFilterState : FilterParkingsModel): MutableLiveData<ArrayList<Parking>> {
        val start = prfManager.getLastLocationStr()
        val destination = prfManager.getDestinationLocation()
        var dest :String? = null
        if (destination.size>0) {
            dest = destination[0].toString()+','+destination[1].toString()
        }
        filterState.value = newFilterState
        return  parkingListRepository.getParkingsList(filterState,idAutomobiliste, start, dest)
    }
    fun refrshFilteredParkings(): MutableLiveData<ArrayList<Parking>> {
        val start = prfManager.getLastLocationStr()
        val destination = prfManager.getDestinationLocation()
        var dest :String? = null
        if (destination.size>0) {
            dest = destination[0].toString()+','+destination[1].toString()
        }
        return  parkingListRepository.getParkingsList(filterState,idAutomobiliste, start, dest)
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