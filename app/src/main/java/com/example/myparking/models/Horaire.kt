package com.example.myparking.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Horaire (val idHoraire: Int,
                    val Jours : String,
                    val HeureOuverture: String,
                    val HeureFermeture: String) : Parcelable