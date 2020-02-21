package com.example.myparking.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class Tarif (val idTarif: Int,
                  val duree: String,
                  val prix: String)  : Parcelable{

}