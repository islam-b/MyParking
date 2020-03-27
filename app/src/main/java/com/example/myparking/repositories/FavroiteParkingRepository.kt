package com.example.myparking.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.myparking.models.AddFavRequest
import com.example.myparking.models.Automobiliste
import com.example.myparking.models.FavoritesResponse
import com.example.myparking.models.Parking
import com.example.myparking.services.ParkingService
import com.example.myparking.utils.InjectorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteParkingRepository{



    var service: ParkingService
    var dataSet = MutableLiveData<ArrayList<Parking>>()

    init {
        service = InjectorUtils.provideParkingService()
    }

    fun getFavoriteParkings(idAutomobiliste: Int): MutableLiveData<ArrayList<Parking>> {

        service.getFavorites(idAutomobiliste).enqueue(object : Callback<FavoritesResponse> {
            override fun onFailure(call: Call<FavoritesResponse>, t: Throwable) {
                Log.d("idAut",idAutomobiliste.toString())
                Log.d("fav error",t.message)
            }
            override fun onResponse(
                call: Call<FavoritesResponse>,
                response: Response<FavoritesResponse>
            ) {

                //dataSet = ArrayList(response.body()!!)
                dataSet.value = response.body()!!.favoris
            }
        })
        return dataSet
    }

    fun addToFavorites(idAutomobiliste: Int, idParking:Int): MutableLiveData<String> {
        var data = MutableLiveData<String>()
        service.addFavorite(idAutomobiliste, AddFavRequest(arrayListOf(idParking))).enqueue(object : Callback<Parking>{
            override fun onFailure(call: Call<Parking>, t: Throwable) {
                Log.d("add fav error", t.message.toString())
               data.value = "Echec! Veuillez réessayez."
            }

            override fun onResponse(call: Call<Parking>, response: Response<Parking>) {
                Log.d("fav body",response.body().toString())
               if (response.code()==200 || response.code()==201) {
                   data.value = "Parking ajouté à vos favoris."
                   getFavoriteParkings(idAutomobiliste)
               } else {
                   data.value = "Echec! Veuillez réessayez."
               }
            }

        })
        return data
    }

    fun removeFromFavorites(idAutomobiliste: Int, idParking:Int): MutableLiveData<String> {
        var data = MutableLiveData<String>()
        service.deleteFavorite(idAutomobiliste, AddFavRequest(arrayListOf(idParking))).enqueue(object : Callback<Parking>{
            override fun onFailure(call: Call<Parking>, t: Throwable) {
                Log.d("add fav error", t.message.toString())
                data.value = "Echec! Veuillez réessayez."
            }

            override fun onResponse(call: Call<Parking>, response: Response<Parking>) {
                Log.d("fav body",response.body().toString())
                if (response.code()==200 || response.code()==201) {
                    data.value = "Parking supprimé de vos favoris."
                    getFavoriteParkings(idAutomobiliste)
                } else {
                    data.value = "Echec! Veuillez réessayez."
                }
            }

        })
        return data
    }


    companion object {
        @Volatile
        private var instance: FavoriteParkingRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: FavoriteParkingRepository().also { instance = it }
            }
    }
}