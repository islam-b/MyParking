package com.example.myparking.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myparking.models.Parking
import com.example.myparking.repositories.FavoriteParkingRepository
import com.example.myparking.repositories.ParkingListRepository
import com.example.myparking.utils.PreferenceManager

class FavoriteParkingViewModel(private val favroriteParkingsRepository: FavoriteParkingRepository,val idAutomobiliste: Int, val  prfManager: PreferenceManager) : ViewModel() {

    var mFavoriteList: MutableLiveData<ArrayList<Parking>>

   init {
       val lastLocation = prfManager.getLastLocationStr()
       val dest = prfManager.getDestinationLocation()
       var destination :String? = null
       if (dest.size>0) {
           destination = dest[0].toString()+','+dest[1].toString()
       }
       mFavoriteList = favroriteParkingsRepository.getFavoriteParkings(idAutomobiliste, lastLocation, destination)
   }

    fun getFavoriteParkings(): MutableLiveData<ArrayList<Parking>> {
        val lastLocation = prfManager.getLastLocationStr()
        val dest = prfManager.getDestinationLocation()
        var destination :String? = null
        if (dest.size>0) {
            destination = dest[0].toString()+','+dest[1].toString()
        }
        return favroriteParkingsRepository.getFavoriteParkings(idAutomobiliste, lastLocation, destination)
    }

    fun addToFavorite(idParking:Int): MutableLiveData<String> {
        val lastLocation = prfManager.getLastLocationStr()
        val dest = prfManager.getDestinationLocation()
        var destination :String? = null
        if (dest.size>0) {
            destination = dest[0].toString()+','+dest[1].toString()
        }
        return favroriteParkingsRepository.addToFavorites(idAutomobiliste,idParking, lastLocation, destination)
    }

    fun removeFromFavorites(idParking:Int): MutableLiveData<String> {
        val lastLocation = prfManager.getLastLocationStr()
        val dest = prfManager.getDestinationLocation()
        var destination :String? = null
        if (dest.size>0) {
            destination = dest[0].toString()+','+dest[1].toString()
        }
        return favroriteParkingsRepository.removeFromFavorites(idAutomobiliste,idParking, lastLocation, destination)
    }

}