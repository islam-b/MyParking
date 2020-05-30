package com.example.myparking.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myparking.HomeActivity
import com.example.myparking.R
import com.example.myparking.fragements.SignInFragment
import com.example.myparking.utils.PreferenceManager

class LoginActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val profile = PreferenceManager(this).checkDriverProfile()
        if (profile != "null") {
            Log.d("precious profile", profile)
            startHomeActivity()
            return
        }
        Log.d("profile null", "null")

        setContentView(R.layout.activity_login2)
        val fragment = SignInFragment()
        supportFragmentManager?.beginTransaction()?.
            replace(R.id.login_container,fragment)?.commit()
    }

    fun startHomeActivity() {
        val mainActivityIntent = Intent(this, HomeActivity::class.java)
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(mainActivityIntent)
        finish()
    }
}