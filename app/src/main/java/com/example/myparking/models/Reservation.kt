package com.example.myparking.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Reservation(val code_res : String, val entree : String, val sortie: String ):Parcelable