package com.example.myparking.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Equipement (val type : String ="", val icon : Int) : Parcelable {

}