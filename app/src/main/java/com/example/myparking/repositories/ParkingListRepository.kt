package com.example.myparking.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myparking.models.FilterParkingsModel
import com.example.myparking.models.Parking
import com.example.myparking.services.ParkingService
import com.example.myparking.utils.InjectorUtils

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.logging.Filter


class ParkingListRepository { // maybe add dao

    private var dataSet = ArrayList<Parking>()

    var service: ParkingService

    init {
        service = InjectorUtils.provideParkingService()
    }


    fun getParkingsList(filterStateLiveData: MutableLiveData<FilterParkingsModel>): MutableLiveData<ArrayList<Parking>> {
        var filterState = FilterParkingsModel()
        filterStateLiveData?.let { filterState = filterStateLiveData?.value!! }
        if (filterState.maxDistance != null && filterState.maxPrice != null && filterState.equipements != null) {
            Log.d("filterStateValueInRepoo", filterState.maxDistance.toString())
            Log.d("filterStateValueInPrice", filterState.maxPrice.toString())
            Log.d("filterStateValueIn2", filterState.equipements)
        }
        var data = MutableLiveData<ArrayList<Parking>>()

        /*if (dataSet.size == 0 || dataSet.isEmpty()) {*/
        service.findParkings(
            filterState.minPrice,
            filterState.maxPrice,
            filterState.equipements,
            filterState.minDistance,
            filterState.maxDistance
        ).enqueue(object : Callback<List<Parking>> {
            override fun onFailure(call: Call<List<Parking>>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<List<Parking>>,
                response: Response<List<Parking>>
            ) {

                dataSet = ArrayList(response.body()!!)
                Log.d("ViewModelReRequest", dataSet.size.toString())
                data.value = dataSet

            }
        })
        return data
        /*  }
          data.value= dataSet
          return data*/
    }

    /* fun getParkingItem(index: Int) : Parking {
         if (dataSet.find { parking -> parking.idParking == index })!=null) {
             getParkingsList()
         }
     }*/
    fun getParkings(): ArrayList<Parking> {
        return dataSet
    }

    companion object {
        @Volatile
        private var instance: ParkingListRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: ParkingListRepository().also { instance = it }
            }
    }
}