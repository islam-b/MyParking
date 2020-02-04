package com.example.myparking.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Reservation(val code_res : String, val entree : String, val sortie: String, val parking: Parking? =null, val state: String = "En cours", val place : String = "E04P83"):Parcelable