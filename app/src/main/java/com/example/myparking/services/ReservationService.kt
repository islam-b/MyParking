package com.example.myparking.services

import com.example.myparking.models.Parking
import com.example.myparking.models.Reservation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ReservationService {
    @GET("reservation")
        fun findReservations(@Query("automobiliste_id") automobiliste_id : Int): Call<List<Reservation>>
    @POST("reservation/")
    fun createReservation(@Body reservationRequest : ReservationRequest) : Call<Reservation>

 /*   , success Callback<Boolean>*/
}