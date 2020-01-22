package com.example.myparking.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import com.example.myparking.models.Parking

class ParkingsPagerAdapter(fm: FragmentManager, fragments: ArrayList<Fragment>) : FragmentPagerAdapter(fm) {

    val mFragments = fragments

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    fun addFragment(fragment: Fragment) {
        mFragments.add(fragment)
        notifyDataSetChanged()
    }


}