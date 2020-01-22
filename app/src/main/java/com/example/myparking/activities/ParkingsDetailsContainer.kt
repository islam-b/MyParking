package com.example.myparking.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.myparking.R
import com.example.myparking.models.Parking
import com.example.myparking.utils.DataSource
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.PagerAdapter
import com.example.myparking.adapters.ParkingsPagerAdapter
import com.example.myparking.fragements.ParkingDetailFragment


class ParkingsDetailsContainer : AppCompatActivity() {

    private var currentParking : Parking? = null
    private var currentId :Int? =0
    private var parkingsList = DataSource.getParkings()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parkings_details_container)
        val bundle = intent.extras
        bundle?.let {
            bundle.apply {
                currentParking = getParcelable<Parking>("PARKING") as Parking
                currentId = getInt("POSITION")
            }
        }
        initPaging()
    }

    private fun initPaging() {

        val fragmentsList= ArrayList<Fragment>()
        parkingsList.forEach {
            fragmentsList.add(ParkingDetailFragment.newInstance(it))
        }

        val pagerAdapter = ParkingsPagerAdapter(supportFragmentManager,fragmentsList)

        val viewPager = findViewById<ViewPager>(R.id.viewPagerParkings)
        viewPager.adapter = pagerAdapter
        viewPager.currentItem = currentId!!
    }

    companion object {
        fun newIntent(context: Context, list:ArrayList<Parking>, position: Int): Intent {
            val intent = Intent(context, ParkingsDetailsContainer::class.java)
            val parking = list[position]
            Log.d("Parking details", parking.name)
            intent.putExtra("PARKING", parking)
            intent.putExtra("POSITION", position)
            //intent.putExtra("NAME", parking.name)
            //intent.putExtra("NEWS", news)
            //intent.putExtra("TITLE",news.description)
            return intent
        }
    }
}
