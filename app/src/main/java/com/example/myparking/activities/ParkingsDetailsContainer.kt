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
import com.example.myparking.repositories.ParkingListRepository
import com.example.myparking.viewmodels.ParkingListViewModel
import javax.crypto.AEADBadTagException


class ParkingsDetailsContainer : AppCompatActivity() {

    private var currentParking : Parking? = null
    private var currentId :Int? =0
    private  var parkingsList  = ParkingListRepository.getInstance().getParkings()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parkings_details_container)
        val bundle = intent.extras
        bundle?.let {
            bundle.apply {
                currentId = getInt("POSITION")
                currentParking = getParcelable<Parking>("PARKING") as Parking

            }
        }
        initPaging()
    }

    override fun onBackPressed() {
        // if main not opened open it and go to it not to home
        super.onBackPressed()
    }

    private fun initPaging() {
        val fragmentsList= ArrayList<Fragment>()
        parkingsList?.forEach {
            fragmentsList.add(ParkingDetailFragment.newInstance(parkingsList.indexOf(it)))
        }

        val pagerAdapter = ParkingsPagerAdapter(supportFragmentManager,fragmentsList)

        val viewPager = findViewById<ViewPager>(R.id.viewPagerParkings)
        viewPager.adapter = pagerAdapter
        viewPager.currentItem = currentId!!
    }

    companion object {
        fun newIntent(context: Context, list:ArrayList<Parking>, idParking: Int): Intent {
            val intent = Intent(context, ParkingsDetailsContainer::class.java)
            val parking = list.find{parking -> parking.idParking == idParking}
            intent.putExtra("PARKING", parking!!)
            intent.putExtra("POSITION", list.indexOf(parking!!))
            return intent
        }
    }
}
