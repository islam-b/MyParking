package com.example.myparking.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class Equipement(
    val idEquipement: Int,
    val designation: String
): Parcelable