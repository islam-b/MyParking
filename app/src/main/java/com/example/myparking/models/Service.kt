package com.example.myparking.models

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Service(val title:String, @DrawableRes val icon:Int) : Parcelable