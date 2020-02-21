package com.example.myparking.services

import com.example.myparking.models.Parking
import retrofit2.Call
import retrofit2.http.GET


interface ParkingService {
    @GET("parking/?start=36.705039%2C3.173912")
    fun findParkings(): Call<List<Parking>>
}
