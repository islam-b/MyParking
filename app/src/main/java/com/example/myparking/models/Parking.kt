package com.example.myparking.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlin.math.roundToInt

@Parcelize
data class Parking
    (
    val idParking: Int,
    val nbEtages: String,
    val nbPlaces: String,
    val nbPlacesLibres: String,
    val nom: String,
    val adresse: String,
    val imageUrl: String,
    val lattitude: Double,
    val longitude: Double,
    val ouvert: String,
    val horairesStatus: ArrayList<HoraireStatus>,
    val paiments : ArrayList<Paiement>,
    val etages :  ArrayList<Etage>,
    val tarifs :  ArrayList<Tarif>,
    val equipements: ArrayList<Equipement>,
    val termes: ArrayList<Terme>,
    val routeInfo : RouteInfo?
    /*val opened: String = "",
    val capacity: String = "",
    val name: String = "",
    val address: String = "",
    val distance: String = "",
    val walk_time: String = "",
    val image: String = "",
    val horaire: @RawValue ArrayList<Horaire>? = null,
    val tarifs: @RawValue ArrayList<Tarif>? = null,
    val paiements: ArrayList<Int>? = null,
    val equipements: ArrayList<Int>? = null,
    val termes: ArrayList<String>? = null*/
):Parcelable {
    fun getDistance(): String {
        if( routeInfo!=null) {
            if (routeInfo?.canWalk!!) {
                return "%.2f Km -".format(routeInfo.walkingDistance.toFloat().div(1000)!!)
            }
            return "%.2f Km -".format(routeInfo.travelDistance.toFloat().div(1000)!!)
        } else return ""


    }

    fun getTime(): String {
        if( routeInfo!=null) {
            if (routeInfo?.canWalk!!) {
                return " %d min.".format(routeInfo.walkingTime.div(60))
            }
            return " %d min.".format(routeInfo.travelTime.div(60)!!)
        }else return ""

    }
    fun isWalkable() : Boolean {
        if( routeInfo!=null) {
            return  routeInfo.canWalk
        } else {
            return  false
        }

    }

    fun getCapacity() : String {
        val percentage =  nbPlacesLibres.toFloat().div(nbPlaces.toFloat()) * 100
        return " - "+percentage.roundToInt().toString() + "%"
    }
}