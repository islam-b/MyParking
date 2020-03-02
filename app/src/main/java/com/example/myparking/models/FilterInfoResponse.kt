package com.example.myparking.models

import java.util.*
import kotlin.collections.ArrayList

data class MinMax (
    val min: String ,
    val max: String
)

data class FilterInfoResponse (
    val equipements: ArrayList<Equipement>? = arrayListOf(),
    val prix: MinMax? = null,
    val distance :MinMax? =null
) {
    fun getDistanceMax(): Float {
        if (distance?.max !=null)
        return distance?.max?.toFloat()!!
        return 5000.0F
    }
    fun getDistanceMin(): Float {
        if(distance?.min!=null)
        return distance?.min?.toFloat()!!
        return 5000.0F
    }
    fun getPriceMax(): Float {
        if(prix?.max !=null)
        return prix?.max?.toFloat()!!
        return 5000.0F
    }
    fun getPriceMin(): Float{
        if(prix?.min !=null)
            return prix?.min?.toFloat()!!
        return 50.0F
    }
}