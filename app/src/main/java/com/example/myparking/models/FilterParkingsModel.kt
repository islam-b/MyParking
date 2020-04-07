package com.example.myparking.models

import android.util.Log
import com.example.myparking.R

class FilterParkingsModel(
    var minPrice: Int? = null,
    var maxPrice: Int? = null,
    var equipements: String? = null,
    var minDistance: Int? = null,
    var maxDistance: Int? = null,
    var sort: Int = 1, // 1 pour ditance, 2 pour prix
    var checkedDistance: Boolean = false,
    var checkedPrice: Boolean = false,
    var checkedEquipements: Boolean = false
) {
    override fun toString(): String {
        return minPrice.toString() + "-" + maxPrice.toString() + "-" + minDistance.toString() + "-" + maxDistance.toString() + "-" + equipements + "-" + sort.toString() +
                "-" + checkedDistance.toString() + "-" + checkedPrice.toString() + "-" + checkedEquipements.toString()
    }

}
