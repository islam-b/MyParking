package com.example.myparking.services

import com.example.myparking.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    @POST("api/driver/login")
    fun signIn(@Body model: SignInModelRequest): Call<SignInModelResponse>

    @POST("register/driver/")
    fun signUp(@Body model: SignUpModelRequest): Call<Any>

    @POST("api/driver/login")
    fun signInWithFb(@Body model: FbSignInModelRequest): Call<SignInModelResponse>

    @GET("braintree/auth")
    fun authBrainTree(@Query("customer_id") customer_id:Int): Call<String>
}