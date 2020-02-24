package com.example.myparking.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Automobiliste (
    val idAutomobiliste : Int,
    val nom: String,
    val prenom : String,
    val compte: String,
    val idCompte: String
): Parcelable