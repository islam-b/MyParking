package com.example.myparking.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myparking.R
import com.example.myparking.fragements.SignUpFragment
import com.example.myparking.fragements.SignInFragment

class LoginActivity2 : AppCompatActivity(), SignInFragment.OnFragmentInteractionListener, SignUpFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)
        val fragment = SignInFragment.newInstance("","")
        supportFragmentManager?.beginTransaction()?.
            replace(R.id.login_container,fragment)?.commit()
    }
}
