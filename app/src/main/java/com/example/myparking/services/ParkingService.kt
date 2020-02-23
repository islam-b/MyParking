package com.example.myparking.services

import com.example.myparking.models.Parking
import com.example.myparking.models.SearchModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ParkingService {
    @GET("parking/?start=36.705039%2C3.173912")
    fun findParkings(): Call<List<Parking>>

    @GET("search")
    fun searchPlaces(@Query("query") keyword:String): Call<SearchModel>
}
