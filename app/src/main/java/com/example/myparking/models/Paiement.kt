package com.example.myparking.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Paiement(val idPaiement: Int, val type: String, val iconUrl: String) : Parcelable {

}