package com.example.myparking.fragements

import android.annotation.SuppressLint
import com.example.myparking.databinding.FragmentParkingsListBinding
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myparking.models.Parking
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.example.myparking.R

import com.example.myparking.activities.ParkingsDetailsContainer


import com.example.myparking.adapters.MyAdapter
import com.example.myparking.adapters.ParkingsListAdapter
import com.example.myparking.utils.InjectorUtils
import com.example.myparking.viewmodels.ParkingListViewModel
import com.example.myparking.viewmodels.ParkingListViewModelFactory


class ParkingsList : Fragment(), MyAdapter.ItemAdapterListener<Parking> {

    private var parkingList : ArrayList<Parking> = ArrayList<Parking>()
    private lateinit var binding: FragmentParkingsListBinding
    private lateinit var recyclerview: RecyclerView
    private  var mAdapter: ParkingsListAdapter? = null

    private lateinit var mParkingListViewModel: ParkingListViewModel


    override fun onItemClicked(item: Parking) {
        val list = mParkingListViewModel.getParkingsList().value
        val intent = ParkingsDetailsContainer.newIntent(
            this.activity as Context,
            list!!, list.indexOf(item)!!
        )
        startActivity(intent)
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_parkings_list, container, false)
        recyclerview = binding.parkingsList
        val factory = ParkingListViewModelFactory()

        mParkingListViewModel = ViewModelProviders.of(this, factory)
            .get(ParkingListViewModel::class.java)

        mParkingListViewModel.getParkingsList().observe(this, Observer<ArrayList<Parking>>
        {
            mAdapter?.updateList(it)

        })
        initParkings()
        return binding.root
    }

    fun initParkings() {
        if (mAdapter == null) {
            mAdapter = ParkingsListAdapter(parkingList, this)
            recyclerview.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            recyclerview.adapter = mAdapter
        }else {
             mAdapter?.notifyDataSetChanged()
        }

    }
}
