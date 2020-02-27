package com.example.myparking.services

import com.example.myparking.models.Parking
import com.example.myparking.models.SearchModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ParkingService {
    @GET("parking/?start=36.734473%2C3.152525")
    fun findParkings(@Query("minPrice") minPrice: Int?,
                     @Query("maxPrice") maxPrice: Int?,
                     @Query(value ="equipements", encoded = true) equipements: String?,
                     @Query("minDistance") minDistance: Int?,
                     @Query("maxDistance") maxDistance: Int?
    ): Call<List<Parking>>
    @GET("search")
    fun searchPlaces(@Query("query") keyword:String): Call<SearchModel>

}
