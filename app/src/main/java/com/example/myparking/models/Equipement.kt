package com.example.myparking.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class Equipement(
    val idEquipement: String,
    val designation: String,
    val iconUrl: String,
    var checked: Boolean =false
): Parcelable