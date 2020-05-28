package com.example.myparking.services

import com.example.myparking.models.ContactUsModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ContactUsService {
    @POST("driver/contact")
    fun sendMessage(@Body model: ContactUsModel): Call<Any>
}