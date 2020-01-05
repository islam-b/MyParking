package com.example.myparking.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myparking.R
import com.example.myparking.adapters.SlideViewPagerAdapter
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        slider_viewPager.adapter = SlideViewPagerAdapter(this)
        setContentView(R.layout.activity_login)
    }
}
