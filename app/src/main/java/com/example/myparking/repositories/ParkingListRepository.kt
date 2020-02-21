package com.example.myparking.repositories

import androidx.lifecycle.MutableLiveData
import com.example.myparking.models.Parking
import com.example.myparking.services.ParkingService
import com.example.myparking.utils.InjectorUtils

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class ParkingListRepository { // maybe add dao

    private var dataSet = ArrayList<Parking>()
    var service: ParkingService

    init {
        service = InjectorUtils.provideParkingService()
    }


    fun getParkingsList (): MutableLiveData<ArrayList<Parking>>{
        var data = MutableLiveData<ArrayList<Parking>>()
        service.findParkings().enqueue(object: Callback<List<Parking>> {
            override fun onFailure(call: Call<List<Parking>>, t: Throwable) {
            }
            override fun onResponse(call: Call<List<Parking>>, response: Response<List<Parking>>) {
                dataSet = ArrayList(response.body()!!)
                data.value = dataSet
            }
        })
        return data
    }
    fun getParkings(): ArrayList<Parking> {
        return dataSet
    }
    companion object {
        @Volatile private var instance: ParkingListRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: ParkingListRepository().also { instance = it }
            }
    }
}