package com.example.myparking.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

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
    val paiementInstance: PaiementInstance

) : Parcelable