package com.example.myparking.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myparking.models.Reservation
import com.example.myparking.repositories.ReservationListRepository
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReservationListViewModel(
    private val id: Int,
    private val reservationListRepository: ReservationListRepository
) : ViewModel() {

    private var mReservationList: MutableLiveData<ArrayList<Reservation>> =
        reservationListRepository.getReservationsList(id)



    fun getReservationsList(): LiveData<ArrayList<Reservation>> { // can't change it only observe
        return mReservationList
    }





}