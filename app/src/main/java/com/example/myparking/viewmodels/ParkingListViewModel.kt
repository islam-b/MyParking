package com.example.myparking.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myparking.R
import com.example.myparking.models.*
import com.example.myparking.repositories.ParkingListRepository

class ParkingListViewModel(private val parkingListRepository: ParkingListRepository) : ViewModel() {

    private var mParkingList: MutableLiveData<ArrayList<Parking>> = parkingListRepository.getParkingsList()

    fun getParkingsList(): LiveData<ArrayList<Parking>> { // can't change it only observe
        return mParkingList;
    }

    fun getEtages(parking: Parking): ArrayList<Etage> {
        return mParkingList.value?.get(mParkingList.value!!.indexOf(parking))?.etages!!
    }

    fun getTermes(parking: Parking): ArrayList<Terme> {
        return mParkingList.value?.get(mParkingList.value!!.indexOf(parking))?.termes!!
    }

    fun getEquipements(parking: Parking): ArrayList<Equipement> {
        return mParkingList.value?.get(mParkingList.value!!.indexOf(parking))?.equipements!!
    }

    fun getTarifs(parking: Parking): ArrayList<Tarif> {
        return mParkingList.value?.get(mParkingList.value!!.indexOf(parking))?.tarifs!!
    }

    fun getHoraires(parking: Parking): ArrayList<Horaire> {
        return mParkingList.value?.get(mParkingList.value!!.indexOf(parking))?.horaire!!
    }

    // fake
    fun getPaiements(parking: Parking?): ArrayList<Paiement> {
        var newList = ArrayList<Paiement>()
        val paiements = ArrayList<Int>()
        paiements.add(1)
        paiements.add(2)
        paiements.let {
            for (i in paiements) {
                when (i) {
                    1 -> newList.add(Paiement("Cheques", R.drawable.ic_check))
                    2 -> newList.add(Paiement("Liquide", R.drawable.ic_liquide))
                    3 -> newList.add(
                        Paiement(
                            "Carte Bancaire",
                            R.drawable.ic_debitcard
                        )
                    )
                }
            }
        }
        return newList
    }
}