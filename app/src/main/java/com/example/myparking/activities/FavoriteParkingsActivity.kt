package com.example.myparking.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myparking.R
import com.example.myparking.adapters.FavoriteParkingAdapter
import com.example.myparking.adapters.HomeParkingAdapter
import com.example.myparking.adapters.MyAdapter
import com.example.myparking.databinding.ActivityFavoriteParkingsBinding
import com.example.myparking.models.Parking
import com.example.myparking.repositories.ParkingListRepository
import com.example.myparking.utils.DataSource
import com.example.myparking.viewmodels.ParkingListViewModel
import com.example.myparking.viewmodels.ParkingListViewModelFactory
import com.example.myparking.viewmodels.ReservationListViewModel
import kotlinx.android.synthetic.debug.activity_favorite_parkings.*
import kotlinx.android.synthetic.debug.activity_favorite_parkings.view.*

class FavoriteParkingsActivity : Fragment(), MyAdapter.ItemAdapterListener<Parking> {

private lateinit var binding : ActivityFavoriteParkingsBinding

    private lateinit var mParkingListViewModel: ParkingListViewModel
    private lateinit var mFavoriteParkingsAdapter: FavoriteParkingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_favorite_parkings, container, false)
        binding.lifecycleOwner = activity

        val factoryParking = ParkingListViewModelFactory(ParkingListRepository.getInstance())
        mParkingListViewModel = ViewModelProviders.of(this, factoryParking)
            .get(ParkingListViewModel::class.java)
        mFavoriteParkingsAdapter = FavoriteParkingAdapter(mParkingListViewModel.getParkingsList().value!!,this)
        mParkingListViewModel.getParkingsList().observe(this, Observer<ArrayList<Parking>>
        {list ->
            val newList = ArrayList(list.sortedWith(compareBy {it.routeInfo?.walkingDistance}))
            mFavoriteParkingsAdapter.updateList(ArrayList(newList))
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFavoriteParkingsList()
    }


    fun initFavoriteParkingsList() {
        val recyclerView = binding.root.fav_parking_list
        recyclerView.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL,false)
        recyclerView.adapter = mFavoriteParkingsAdapter
    }

    override fun onItemClicked(item: Parking) {
        Log.d("parking clicked", "cc")
        val list = mParkingListViewModel.getParkingsList().value!!
        val index = list.indexOf(item)
        val bundle= bundleOf("parking" to item, "parkingIndex" to index)
        val navController = Navigation.findNavController(binding.root)
        navController.navigate(R.id.action_favoriteParkingsActivity_to_parkingsDetailsContainer, bundle)
    }

}
