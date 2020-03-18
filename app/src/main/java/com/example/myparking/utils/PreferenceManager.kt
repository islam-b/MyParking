package com.example.myparking.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.myparking.R
import com.example.myparking.models.DriverProfile
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

    fun destroyProfile() {

        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.my_preference), Context.MODE_PRIVATE)

        sharedPreferences.edit().remove("profile").apply()
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