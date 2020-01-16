package com.example.myparking.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myparking.R
import com.example.myparking.fragements.SignInFragment

class LoginActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)
        val fragment = SignInFragment()
        supportFragmentManager?.beginTransaction()?.
            replace(R.id.login_container,fragment)?.commit()
    }
}