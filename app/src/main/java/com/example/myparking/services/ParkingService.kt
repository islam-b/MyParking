package com.example.myparking.services

import com.example.myparking.models.Parking
import retrofit2.Call
import retrofit2.http.GET


interface ParkingService {
    @GET("parking/?start=36.734473%2C3.152525")
    fun findParkings(): Call<List<Parking>>
}
