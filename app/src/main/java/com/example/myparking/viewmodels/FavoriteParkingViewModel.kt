package com.example.myparking.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myparking.models.Parking
import com.example.myparking.repositories.FavoriteParkingRepository
import com.example.myparking.repositories.ParkingListRepository

class FavoriteParkingViewModel(private val favroriteParkingsRepository: FavoriteParkingRepository,val idAutomobiliste: Int, var lastLocation: String?,
                                var destination: String?) : ViewModel() {

    var mFavoriteList: MutableLiveData<ArrayList<Parking>>

   init {
       mFavoriteList = favroriteParkingsRepository.getFavoriteParkings(idAutomobiliste, lastLocation, destination)
   }

    fun getFavoriteParkings(): MutableLiveData<ArrayList<Parking>> {
        return favroriteParkingsRepository.getFavoriteParkings(idAutomobiliste, lastLocation, destination)
    }

    fun addToFavorite(idParking:Int): MutableLiveData<String> {
        return favroriteParkingsRepository.addToFavorites(idAutomobiliste,idParking, lastLocation, destination)
    }

    fun removeFromFavorites(idParking:Int): MutableLiveData<String> {
        return favroriteParkingsRepository.removeFromFavorites(idAutomobiliste,idParking, lastLocation, destination)
    }

}