package com.example.myparking.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.myparking.DI.APIComponent
import com.example.myparking.MyParkingApplication
import com.example.myparking.models.Parking
import com.example.myparking.services.ParkingService
import com.example.myparking.utils.DataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class ParkingListRepository { // maybe add dao

    lateinit var apiComponent: APIComponent
    @Inject
    lateinit var retrofit: Retrofit

    private var dataSet = ArrayList<Parking>()
    var service: ParkingService

    init {
        apiComponent = MyParkingApplication.apiComponent
        apiComponent.inject(this)
        service = retrofit.create(ParkingService::class.java)

    }


    fun getParkingsList (): MutableLiveData<ArrayList<Parking>>{
        // dataSet = DataSource.getParkings()
        var data = MutableLiveData<ArrayList<Parking>>()
        Log.d("before service amira", "test")
        service.findParkings().enqueue(object: Callback<List<Parking>> {
            override fun onFailure(call: Call<List<Parking>>, t: Throwable) {
                Log.d("ERROR amira", t.printStackTrace().toString())
            }
            override fun onResponse(call: Call<List<Parking>>, response: Response<List<Parking>>) {
                Log.d("SUCCESS amira", response.body()!!.toString())

                dataSet = ArrayList(response.body()!!)
                data.value = dataSet
            }
        })
        return data
    }

    companion object {
        @Volatile private var instance: ParkingListRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: ParkingListRepository().also { instance = it }
            }
    }
}