package com.example.myparking.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RouteInfo
    (
    val distance: Int,
    val travelTime: Int
): Parcelable