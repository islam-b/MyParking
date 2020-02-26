package com.example.myparking.services

import com.example.myparking.models.SearchModel
import com.example.myparking.models.SignInModelRequest
import com.example.myparking.models.SignInModelResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    @POST("api/driver/login")
    fun signIn(@Body model: SignInModelRequest): Call<SignInModelResponse>
}