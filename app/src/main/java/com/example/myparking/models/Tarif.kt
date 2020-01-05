package com.example.myparking.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Tarif (val tarif_time : String ="", val tarif_sum : String ="") : Parcelable {

}