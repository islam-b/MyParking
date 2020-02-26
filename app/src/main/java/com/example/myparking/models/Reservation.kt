package com.example.myparking.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
class Reservation(
    val idReservation : Int,
    val qrUrl: String,
    val dateEntreePrevue : String,
    val dateSortiePrevue: String,
    val dateEntreeEffective: String,
    val dateSortieEffective: String,
    val parking : Parking,
    val automobiliste: Automobiliste,
    val paiment: Paiement,
    val paiementInstance: PaiementInstance,
    val codeReservation : String,
    val etageAttribue : String,
    val placeAttribue: String

) : Parcelable {
    fun getDateEntree(): String {
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.FRANCE)
        val newFormatter = SimpleDateFormat("EEE, dd MMM, HH:mm", Locale.FRANCE)
        return  newFormatter.format(df.parse(dateEntreePrevue.substring(0,19))!!)
    }
    fun getDateSortie(): String {
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.FRANCE)
        val newFormatter = SimpleDateFormat("EEE, dd MMM, HH:mm", Locale.FRANCE)
        return  newFormatter.format(df.parse(dateSortiePrevue.substring(0,19))!!)
    }
    fun getPlace(): String {
        return "E0%d-P%03d".format(etageAttribue.toInt(), placeAttribue.toInt())
    }
}