package com.example.myparking.models

data class DriverProfile(
    val idAutomobiliste: Int?,
    val nom:  String,
    val numTel: String,
    val prenom: String,
    val compte: String?,
    val idCompte: String?
)