package com.example.myparking.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RouteInfo
    (
    val travelDistance: Int,
    val travelTime: Int,
    val walkingDistance: Int,
    val walkingTime: Int,
    val canWalk: Boolean
): Parcelable