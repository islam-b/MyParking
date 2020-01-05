package com.example.myparking.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Horaire (val horaire_days : String ="", val horaire_time : String ="") : Parcelable  {

}