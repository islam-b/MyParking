package com.example.myparking.services

import com.example.myparking.models.UpdateLocationRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LocationService {
    @POST("driver/updateLocation")
    fun updateLocation(@Body model: UpdateLocationRequest): Call<Any>
}