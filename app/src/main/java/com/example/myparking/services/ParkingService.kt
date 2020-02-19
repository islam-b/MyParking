package com.example.myparking.services

import com.example.myparking.models.Parking
import retrofit2.Call
import retrofit2.http.GET


interface ParkingService {
    @GET("parking")
    fun findParkings(): Call<List<Parking>>
}
