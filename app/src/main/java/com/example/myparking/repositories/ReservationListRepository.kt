package com.example.myparking.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.myparking.models.Reservation
import com.example.myparking.models.ReservationRequest
import com.example.myparking.services.ReservationService
import com.example.myparking.utils.InjectorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReservationListRepository { // maybe add dao

    private var dataSet = ArrayList<Reservation>()
    var mReservationList = MutableLiveData<ArrayList<Reservation>>()
    private lateinit var createdReservation : MutableLiveData<Reservation>
    var service: ReservationService

    init {
        service = InjectorUtils.provideReservationService()
    }


    fun getReservationsList(id: Int): MutableLiveData<ArrayList<Reservation>> {


        service.findReservations(id).enqueue(object : Callback<List<Reservation>> {
            override fun onFailure(call: Call<List<Reservation>>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<List<Reservation>>,
                response: Response<List<Reservation>>
            ) {
                Log.d("reservations if", id.toString())
                dataSet = ArrayList(response.body()!!)
                mReservationList.value = dataSet
            }
        })
        return mReservationList
    }

    fun getCreatedReservation(reservationRequest: ReservationRequest) : MutableLiveData<Reservation> {
        var newRes = MutableLiveData<Reservation>()
        service.createReservation(reservationRequest).enqueue(object : Callback<Reservation> {
            override fun onFailure(call: Call<Reservation>, t: Throwable) {
                Log.d("ITS A FAIIIL", t.message!!)
            }

            override fun onResponse(
                call: Call<Reservation>,
                response: Response<Reservation>
            ) {
                Log.d("RESPONSEBODY",response.body()?.idReservation.toString()!! )
             newRes.value = response.body()
                createdReservation = newRes
            }
        })
        return newRes
    }
    fun getReservations(): ArrayList<Reservation> {
        return dataSet
    }

    fun getReservationsCustom(): ArrayList<Reservation> {

        var reservationsNew = ArrayList<Reservation>()
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.FRANCE)

        dataSet.forEach {
            var newRes = it
            val newFormatter = SimpleDateFormat("EEE, dd MMM, HH:mm", Locale.FRANCE)
            newRes = Reservation(
                it.idReservation,
                it.qrUrl,
                null,
                newFormatter.format(df.parse(it.dateEntreePrevue.substring(0,19))!!),
                newFormatter.format(df.parse(it.dateSortiePrevue.substring(0,19))!!),
                newFormatter.format(df.parse(it.dateEntreeEffective.substring(0,19))!!),
                newFormatter.format(df.parse(it.dateSortieEffective.substring(0,19))!!),
                it.parking,
                it.automobiliste,
                it.paiment,
                it.paiementInstance,
                it.codeReservation,
                it.etageAttribue,
                it.placeAttribue
            )
            reservationsNew.add(newRes)
        }
        return reservationsNew
    }

    companion object {
        @Volatile
        private var instance: ReservationListRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: ReservationListRepository().also { instance = it }
            }
    }
}