package com.example.myparking.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.example.myparking.R
import com.example.myparking.adapters.SlideViewPagerAdapter
import com.example.myparking.utils.SlideObject
import kotlinx.android.synthetic.main.activity_login.*
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isEmpty
import androidx.viewpager.widget.ViewPager
import com.example.myparking.MainActivity
import com.example.myparking.utils.PreferenceManager


class SliderActivity : AppCompatActivity(){

    private val layouts = SlideObject.values()
    private var dots = arrayOfNulls<ImageView>(layouts.size)
    //private var dots= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //PreferenceManager(this).clearreference()  // remove this to show slider again
        if(PreferenceManager(this).checkPreference())
        {
            goToLoginActivity()
        }
        setContentView(R.layout.activity_login)
        slider_viewPager?.adapter = SlideViewPagerAdapter(layouts, this)
        createDots(0)
        slider_viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                Log.d("scroll state", state.toString())
            }
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            /*    Log.d("positionoffset", positionOffset.toString())
                Log.d("positionoffsetPixels", positionOffsetPixels.toString())
                if (position == layouts.size-1)   goToLoginActivity()*/

            }
            override fun onPageSelected(position: Int) {
                createDots(position)
                if (position == layouts.size-1) {
                    skip_slider.visibility=View.INVISIBLE
                } else {
                    skip_slider.visibility=View.VISIBLE
                }
            }
        })
        skip_slider?.setOnClickListener {
            //goToMainActivity()
            goToLoginActivity() //real login
            PreferenceManager(this).writePreference()
        }
        next_slider?.setOnClickListener{
            goToNextSlide()
        }


    }

    fun createDots(currentPosition: Int) {
        if (!dotslayout.isEmpty()) dotslayout.removeAllViews()
        for( i in layouts.indices) {
            dots[i] = ImageView(this)
            if (i==currentPosition) dots[i]?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dot))
            else  dots[i]?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.inactive_dot))

           val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(4,0,4,0)
            dotslayout.addView(dots[i], params)
        }
    }


    private fun goToNextSlide(){
       var nextSlide = 0
        slider_viewPager?.let{
            nextSlide = slider_viewPager.currentItem + 1
        }

        if (nextSlide<layouts.size)
        {
            slider_viewPager.currentItem = nextSlide
        }else {
            //goToMainActivity()
            goToLoginActivity()
            PreferenceManager(this).writePreference()
        }

    }

    private fun goToLoginActivity() {
        val loginActivityIntent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(loginActivityIntent)
        finish()
    }
}

