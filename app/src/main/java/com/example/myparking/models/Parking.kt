package com.example.myparking.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Parking(val opened: String = "",val  capacity: String ="",  val name: String ="" ,val address: String ="",
                   val distance: String ="",val walk_time: String ="", val image: String ="", val horaire :  @RawValue ArrayList<Horaire>? = null,
                   val tarifs :  @RawValue ArrayList<Tarif>? = null,
                   val paiements: ArrayList<Int>? = null,
                   val equipements: ArrayList<Int>? = null)
    : Parcelable {

}