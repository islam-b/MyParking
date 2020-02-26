package com.example.myparking.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myparking.R
import com.example.myparking.models.*
import com.example.myparking.repositories.ParkingListRepository
import kotlin.math.roundToInt

class ParkingItemViewModel(
    private val index: Int,
    private val parkingListRepository: ParkingListRepository
) : ViewModel() {
    private var mParking: MutableLiveData<Parking> = MutableLiveData<Parking>()

    init {
        mParking.value = parkingListRepository.getParkings()[index]
    }

    fun getParkingItem(): LiveData<Parking> { // can't change it only observe
        return mParking;
    }
    fun getParking(): Parking {
        return mParking.value!!
    }

    fun getTarifs(): ArrayList<Tarif> {
        var tarifList = ArrayList<Tarif>()
         mParking.value?.tarifs!!.forEach{
             val heures = it.duree.toFloat().div(60).roundToInt()
             tarifList.add(Tarif(it.idTarif, "$heures heure(s)", it.prix + " DZD"))
         }
        return tarifList
    }

    fun getName(): String {
        return mParking.value?.nom!!
    }

    fun getAddress(): String {
        return mParking.value?.adresse!!
    }

    fun getNbPlaces(): String {
        return mParking.value?.nbPlaces!!
    }

    fun getOuvert(): String {
        return mParking.value?.ouvert!!
    }

    fun getImageUrl(): String {
        return mParking.value?.imageUrl!!
    }

    fun getNbEtage(): String {
        return mParking.value?.nbEtages!!
    }

    fun getEtages(): ArrayList<Etage> {
        return mParking.value?.etages!!
    }

    fun getTermes(): ArrayList<Terme> {
        return mParking.value?.termes!!
    }

    fun getEquipements(): ArrayList<Equipement> {
        return mParking.value?.equipements!!
    }

    fun getHoraires(): ArrayList<Horaire> {
        var horaires = ArrayList<Horaire>()
        val horaireStatus = mParking.value?.horairesStatus!!
        horaireStatus.forEach {
            var days = it.days[0]
            if(it.days.size >1)it.days.subList(1, it.days.size).forEach{dayEnd ->
                days+= " - $dayEnd"
            }
            val horaire = Horaire(days, it.HeureOuverture.substring(0,5), it.HeureFermeture.substring(0,5))
            horaires.add(horaire)
        }
        return horaires
    }

    fun getPaiments(): ArrayList<Paiement> {
        return mParking.value?.paiments!!
    }

    fun isOuvert(): Boolean {
        if (mParking.value?.ouvert == "Ouvert") return true
        return false
    }


}