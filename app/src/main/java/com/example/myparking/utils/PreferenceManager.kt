package com.example.myparking.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.myparking.R
import com.example.myparking.models.DriverProfile
import com.example.myparking.models.FilterParkingsModel
import com.example.myparking.models.SignInModelResponse

class PreferenceManager(val context: Context){


    private val sharedPreferences = context.getSharedPreferences(context.getString(R.string.my_preference), Context.MODE_PRIVATE)

    fun writePreference()
    {
        val editor = sharedPreferences.edit()
        editor.putString(context.getString(R.string.my_preference_key),"INIT_OK")
        editor.apply()
    }

    fun writeDriverProfile(profile: DriverProfile) {

        val editor = sharedPreferences.edit()
        /*editor.putStringSet("profile", setOf(
            profile.idAutomobiliste.toString(),profile.nom, profile.numTel,profile.prenom
        ))*/
        Log.d("writin to pref",profile.idAutomobiliste.toString())
        editor.putString("profile",profile.idAutomobiliste.toString())
        editor.apply()
        val v = sharedPreferences.getString("profile","null  nulll")!!
        Log.d("critten to pref",v)
    }
    fun writeFilterInfo(filter: FilterParkingsModel) {
        val editor = sharedPreferences.edit()
        editor.putInt("filter_sort",filter.sort).apply()
        editor.putString("filter_distance_min",filter.minDistance?.toString()).apply()
        editor.putString("filter_distance_max",filter.maxDistance?.toString()).apply()
        editor.putString("filter_price_min",filter.minPrice?.toString()).apply()
        editor.putString("filter_price_max",filter.maxPrice?.toString()).apply()
        editor.putString("filter_equipements",filter.equipements).apply() // check for null
        editor.putBoolean("filter_distance_checked", filter.checkedDistance).apply()
        editor.putBoolean("filter_price_checked", filter.checkedPrice).apply()
        editor.putBoolean("filter_equ_checked", filter.checkedEquipements).apply()
        /*editor.apply()*/ // check here
    }

    fun getFilterInitialInfo(): FilterParkingsModel{
        val filter_sort = sharedPreferences.getInt("filter_sort", 1)
        Log.d("filter_sort", filter_sort.toString())
        val filter_distance_min = sharedPreferences.getString("filter_distance_min","null")
        Log.d("filter_sort", filter_sort.toString())
        val filter_distance_max = sharedPreferences.getString("filter_distance_max","null")
        val filter_price_min = sharedPreferences.getString("filter_price_min","null")
        val filter_price_max = sharedPreferences.getString("filter_price_max","null")
        val filter_price_checked = sharedPreferences.getBoolean("filter_price_checked",false)
        val filter_distance_checked = sharedPreferences.getBoolean("filter_distance_checked",false)
        val filter_equ_checked = sharedPreferences.getBoolean("filter_equ_checked",false)
        val filter_equipements = sharedPreferences.getString("filter_equipements",null)


        return FilterParkingsModel(filter_price_min?.toIntOrNull() , filter_price_max?.toIntOrNull(),filter_equipements, filter_distance_min?.toIntOrNull(), filter_distance_max?.toIntOrNull(),
            filter_sort, filter_distance_checked, filter_price_checked, filter_equ_checked)
    }


    fun destroyProfile() {

        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.my_preference), Context.MODE_PRIVATE)

        sharedPreferences.edit().remove("profile").apply()
    }

    fun destroyFiltersInit() {

        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.my_preference), Context.MODE_PRIVATE)

        sharedPreferences.edit().remove("filter_sort").apply()
        sharedPreferences.edit().remove("filter_distance_min").apply()
        sharedPreferences.edit().remove("filter_distance_max").apply()
        sharedPreferences.edit().remove("filter_price_min").apply()
        sharedPreferences.edit().remove("filter_price_max").apply()
        sharedPreferences.edit().remove("filter_equipements").apply()
        sharedPreferences.edit().remove("filter_distance_checked").apply()
        sharedPreferences.edit().remove("filter_price_checked").apply()
        sharedPreferences.edit().remove("filter_equ_checked").apply()
    }
    fun checkDriverProfile():String {


        val str = sharedPreferences.getString("profile","null")!!
        Log.d("getting pref", str)
        return str

    }

    fun checkPreference(): Boolean {
        var status = true
        if (sharedPreferences.getString(context.getString(R.string.my_preference_key), "null").equals("null")) {
            status = false
        }
        return status
    }

    fun clearreference() {
        sharedPreferences.edit().clear().apply()
    }


}