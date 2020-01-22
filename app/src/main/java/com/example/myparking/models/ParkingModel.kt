package com.example.myparking.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ParkingModel(val id: String, val name: String, val lat: Double, val long: Double) : Parcelable{
}