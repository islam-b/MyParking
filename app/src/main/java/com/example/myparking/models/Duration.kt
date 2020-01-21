package com.example.myparking.models

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
class Duration(val text1:String, var text2:String, @DrawableRes val icon:Int, val TAG: String, val clickable: Boolean) : Parcelable
