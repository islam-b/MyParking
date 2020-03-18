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
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myparking.models.Parking
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation

import com.example.myparking.R

import com.example.myparking.activities.ParkingsDetailsContainer


import com.example.myparking.adapters.MyAdapter
import com.example.myparking.adapters.ParkingsListAdapter
import com.example.myparking.repositories.ParkingListRepository
import com.example.myparking.viewmodels.ParkingListViewModel
import com.example.myparking.viewmodels.ParkingListViewModelFactory
import kotlinx.android.synthetic.main.fragment_parkings_list.*


class ParkingsList : Fragment(), MyAdapter.ItemAdapterListener<Parking> {

    private var parkingList: ArrayList<Parking> = ArrayList<Parking>()
    private lateinit var binding: FragmentParkingsListBinding
    private lateinit var recyclerview: RecyclerView
    private var mAdapter: ParkingsListAdapter? = null

    private lateinit var mParkingListViewModel: ParkingListViewModel


    override fun onItemClicked(item: Parking) {
        val navController = Navigation.findNavController(activity!!,R.id.my_nav_host_fragment)
        val list = mParkingListViewModel.getParkingsList().value
        val index = list?.indexOf(item)
        val bundle= bundleOf("parking" to item, "parkingIndex" to index)
        navController.navigate(R.id.action_mainActivity2_to_parkingsDetailsContainer, bundle)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("OnCreateViewList", "parkiings")
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_parkings_list, container, false)

        recyclerview = binding.parkingsList
        val factory = ParkingListViewModelFactory(ParkingListRepository.getInstance())
        mParkingListViewModel = ViewModelProviders.of(this.activity!!, factory)
            .get(ParkingListViewModel::class.java)
        showProgressBar()
        mParkingListViewModel.mParkingList.observe(this, Observer<ArrayList<Parking>>
        {
            Log.d("ViewModelChanged", it?.size?.toString()!!)
                mAdapter?.updateList(it)

            hideProgressBar()

        })
        Log.d("step","5")
        initParkings()
        Log.d("step","6")
        return binding.root
    }

    fun initParkings() {
        mAdapter = ParkingsListAdapter(parkingList, this)
        recyclerview.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerview.adapter = mAdapter

    }
    private fun showProgressBar() {
        progress_bar?.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progress_bar?.visibility = View.GONE
    }
}
