package com.example.myparking.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Etage (val idEtage: Int,
                  val nbPlaces: Int): Parcelable