package com.example.myparking.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PaiementInstance (
    val idPaiementInstance:String?,
    val montant: String,
    val date: String
) : Parcelable
