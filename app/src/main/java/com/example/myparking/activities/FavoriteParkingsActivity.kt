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
import com.example.myparking.repositories.FavoriteParkingRepository
import com.example.myparking.repositories.ParkingListRepository
import com.example.myparking.utils.DataSource
import com.example.myparking.utils.PreferenceManager
import com.example.myparking.viewmodels.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.debug.activity_favorite_parkings.*
import kotlinx.android.synthetic.debug.activity_favorite_parkings.view.*

class FavoriteParkingsActivity : Fragment(), MyAdapter.ItemAdapterListener<Parking>,
    FavortieParkingListener {


    private lateinit var binding : ActivityFavoriteParkingsBinding

    private lateinit var mFavoriteParkingViewModel: FavoriteParkingViewModel
    private lateinit var mFavoriteParkingsAdapter: FavoriteParkingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val lastLocation = PreferenceManager(context!!).getLastLocationStr()
        val idDriver = PreferenceManager(context!!).checkDriverProfile().toInt()

        binding = DataBindingUtil.inflate(inflater, R.layout.activity_favorite_parkings, container, false)
        binding.lifecycleOwner = activity

        val factoryParking = FavoriteParkingViewModelFactory(FavoriteParkingRepository.getInstance(),idDriver,lastLocation, null)
        mFavoriteParkingViewModel = ViewModelProviders.of(this, factoryParking)
            .get(FavoriteParkingViewModel::class.java)
                mFavoriteParkingsAdapter = FavoriteParkingAdapter(ArrayList<Parking>(),this)
        mFavoriteParkingViewModel.getFavoriteParkings().observe(this, Observer<ArrayList<Parking>>
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
        val list = mFavoriteParkingViewModel.getFavoriteParkings().value!!
        val index = list.indexOf(item)
        val bundle= bundleOf("parking" to item, "parkingIndex" to index)
        val navController = Navigation.findNavController(binding.root)
        navController.navigate(R.id.action_favoriteParkingsActivity_to_parkingsDetailsContainer, bundle)
    }

    override fun removeFromFavorite(parking: Parking) {
        mFavoriteParkingViewModel.removeFromFavorites(parking.idParking).observe(this, Observer<String>{
            if (it!==null && it!="") {
                val snackbar = Snackbar
                    .make(binding.root, it, Snackbar.LENGTH_LONG)
                snackbar.show()
            }
        })
    }

}
interface FavortieParkingListener : MyAdapter.ItemAdapterListener<Parking> {
    fun removeFromFavorite(parking: Parking)
}
