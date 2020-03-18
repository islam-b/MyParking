package com.example.myparking.models

data class FbSignInModelRequest(
    val fromFb: Boolean = true,
    val email: String = "",
    val password: String = "*",
    val username: String,
    val driverProfile: DriverProfile
)