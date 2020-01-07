package com.example.myparking.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.myparking.R

class PreferenceManager(val context: Context){


    private val sharedPreferences = context.getSharedPreferences(context.getString(R.string.my_preference), Context.MODE_PRIVATE)

    fun writePreference()
    {
        val editor = sharedPreferences.edit()
        editor.putString(context.getString(R.string.my_preference_key),"INIT_OK")
        editor.apply()
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