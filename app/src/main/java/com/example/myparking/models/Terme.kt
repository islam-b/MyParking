package com.example.myparking.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Terme (
    val idTerme : Int,
    val contenu: String
):Parcelable