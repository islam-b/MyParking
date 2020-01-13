package com.example.myparking.fragements

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myparking.models.Parking
import androidx.databinding.DataBindingUtil
import com.example.myparking.utils.DataSource
import com.example.myparking.activities.ParkingDetailsActivity
import com.example.myparking.R
import com.example.myparking.adapters.MyAdapter
import com.example.myparking.adapters.ParksListAdapter
import com.example.myparking.databinding.FragmentParkingsListBinding


class ParkingsList : Fragment(), MyAdapter.ItemAdapterListener<Parking> {

    private lateinit var binding: FragmentParkingsListBinding
    override fun onItemClicked(item: Parking) {
        val intent = ParkingDetailsActivity.newIntent(
            this.activity as Context,
            item
        )
        startActivity(intent)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_parkings_list, container, false)
        initParkings()
        return binding.root
    }

    fun initParkings() {
        val recyclerview = binding.parkingsList
        recyclerview.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val adapter = ParksListAdapter(DataSource.getParkings(), this)
        recyclerview.adapter = adapter
    }

}
