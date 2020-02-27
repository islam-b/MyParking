package com.example.myparking.models

data class SignInModelResponse(
    val refresh: String,
    val access : String,
    val email: String,
    val username: String,
    val driverProfile: DriverProfile
)