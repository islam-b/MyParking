package com.example.myparking.models

import com.example.myparking.models.PaiementInstance

class ReservationRequest (
    val dateEntreePrevue : String,
    val dateSortiePrevue : String,
    val parking_id : String,
    val automobiliste_id : String,
    val paiment_id : String,
    val paiementInstance : PaiementInstance
)