package com.example.myparking.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myparking.models.Reservation
import com.example.myparking.repositories.ReservationListRepository
import java.text.SimpleDateFormat
import java.util.*

class ReservationItemViewModel(
    private val index: Int,
    private val id: Int,
    private val reservationListRepository: ReservationListRepository
) : ViewModel() {

    private var mReservationItem = MutableLiveData<Reservation>()
    private val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.FRANCE)
    private val newFormatter = SimpleDateFormat("EEE, dd MMM, HH:mm", Locale.FRANCE)

    init {
        mReservationItem.value = reservationListRepository.getReservations()[index]
    }

    fun getReservationItem(): LiveData<Reservation> { // can't change it only observe
        return mReservationItem
    }

    fun getOuvert(): String {
        return mReservationItem.value?.parking?.ouvert!!
    }

    fun getDateEntreePrevue() : String {
        return newFormatter.format(df.parse(mReservationItem.value?.dateEntreePrevue?.substring(0,19)!!)!!)
    }
    fun getDateSortiePrevue() : String {
        return newFormatter.format(df.parse(mReservationItem.value?.dateSortiePrevue?.substring(0,19)!!)!!)
    }
    fun getParkingNom(): String {
        return mReservationItem.value?.parking?.nom!!
    }

}