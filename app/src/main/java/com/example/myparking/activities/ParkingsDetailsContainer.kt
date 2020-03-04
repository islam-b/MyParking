package com.example.myparking.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


class ParkingsDetailsContainer : Fragment() {

    private var currentParking : Parking? = null
    private var currentId :Int? =0
    private  var parkingsList  = ParkingListRepository.getInstance().getParkings()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentId =  arguments?.getInt("parkingIndex")
        currentParking = arguments?.getParcelable<Parking>("parking") as Parking


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_parkings_details_container,container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPaging(view)
    }

    private fun initPaging(view: View) {
        val fragmentsList= ArrayList<Fragment>()
        parkingsList?.forEach {
            fragmentsList.add(ParkingDetailFragment.newInstance(parkingsList.indexOf(it)))
        }

        val pagerAdapter = ParkingsPagerAdapter(childFragmentManager,fragmentsList)

        val viewPager = view.findViewById<ViewPager>(R.id.viewPagerParkings)
        viewPager.adapter = pagerAdapter
        viewPager.currentItem = currentId!!
    }

}
