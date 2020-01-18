package com.example.myparking.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.myparking.Main2Activity
import com.example.myparking.R

class SplashActivity : AppCompatActivity() {
    private val splashTime = 2000L
    private lateinit var myHandler : Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        myHandler = Handler()
        myHandler.postDelayed({
           // goToMainActivity()
            goToSliderActivity()
        }, splashTime)
    }

    private fun goToMainActivity() {
        val mainActivityIntent = Intent(applicationContext, Main2Activity::class.java)
        startActivity(mainActivityIntent)
        finish()
    }

    private fun goToSliderActivity(){
        val loginActivityIntent = Intent(applicationContext, SliderActivity::class.java)
        startActivity(loginActivityIntent)
        finish()

    }
}

