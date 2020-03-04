package com.example.myparking.models

data class SignUpModelRequest(
    val email: String,
    val password: String,
    val username: String,
    val driverProfile: DriverProfile
)