package com.example.myparking.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HoraireStatus (
    val days: ArrayList<String>,
    val HeureOuverture: String,
    val HeureFermeture: String
): Parcelable
