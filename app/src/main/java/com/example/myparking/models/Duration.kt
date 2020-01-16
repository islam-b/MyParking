package com.example.myparking.models

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import kotlinx.android.parcel.Parcelize

@Parcelize
class Duration(val text1:String, val text2:String,@DrawableRes val icon:Int) : Parcelable