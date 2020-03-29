package com.example.myparking.fragements


import com.example.myparking.databinding.FragmentParkingsListBinding

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myparking.models.Parking
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.myparking.MainActivity

import com.example.myparking.R
import com.example.myparking.adapters.FavoriteParkingAdapter


import com.example.myparking.adapters.MyAdapter
import com.example.myparking.adapters.ParkingsListAdapter
import com.example.myparking.models.FilterParkingsModel
import com.example.myparking.repositories.FavoriteParkingRepository
import com.example.myparking.repositories.ParkingListRepository
import com.example.myparking.utils.InjectorUtils
import com.example.myparking.utils.MapAction
import com.example.myparking.utils.PreferenceManager
import com.example.myparking.viewmodels.*
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_parkings_list.*
import kotlinx.android.synthetic.main.fragment_parkings_list.view.*
import retrofit2.Callback
import com.google.android.material.snackbar.Snackbar




class ParkingsList : Fragment(), MyAdapter.ItemAdapterListener<Parking>, ParkingItemListener {


    private var parkingList: ArrayList<Parking> = ArrayList<Parking>()
    private lateinit var binding: FragmentParkingsListBinding
    private lateinit var recyclerview: RecyclerView
    private var mAdapter: ParkingsListAdapter? = null

    private lateinit var mParkingListViewModel: ParkingListViewModel
    private lateinit var mFavoriteParkingViewModel: FavoriteParkingViewModel


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
        mParkingListViewModel.getFilterState().observe(this, Observer<FilterParkingsModel> {
            fillFiltersInfoSection(it)
        })
        binding.root.clear_filters.setOnClickListener {
            upadateList(FilterParkingsModel())
        }
        Log.d("step","5")
        initParkings()
        Log.d("step","6")
        val id = PreferenceManager(context!!).checkDriverProfile().toInt()
        val factoryFav= FavoriteParkingViewModelFactory(FavoriteParkingRepository.getInstance(),id)
        mFavoriteParkingViewModel = ViewModelProviders.of(this, factoryFav)
            .get(FavoriteParkingViewModel::class.java)
        return binding.root
    }

    fun initParkings() {
        mAdapter = ParkingsListAdapter(parkingList, this)
        recyclerview.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerview.adapter = mAdapter

    }
    private fun showProgressBar() {
        recyclerview.visibility= GONE
        binding.root.shimmer_parking_list.visibility= VISIBLE
        binding.root.shimmer_parking_list.startShimmer()
    }

    private fun hideProgressBar() {
        binding.root.shimmer_parking_list.startShimmer()
        binding.root.shimmer_parking_list.visibility= GONE
        recyclerview.visibility= VISIBLE


    }

    override fun navigateToParking(parking: Parking) {
        val args = bundleOf("viewType" to MainActivity.MAP_VIEW, "actionType" to MapAction.NAVIGATION_ACTION,
            "data" to parking)

        val navController = Navigation.findNavController(activity!!,R.id.my_nav_host_fragment)
        navController.navigate(R.id.action_global_mainActivity2, args)
    }

    override fun addToFavorites(parking: Parking) {

        mFavoriteParkingViewModel.addToFavorite(parking.idParking).observe(this, Observer<String>{
            if (it!==null && it!="") {
                val snackbar = Snackbar
                    .make(binding.root, it, Snackbar.LENGTH_LONG)
                snackbar.show()
            }
        })

    }

    fun fillFiltersInfoSection(filterState: FilterParkingsModel) {
        var count = 0
        binding.root.chipGroup.removeAllViews()
        if (filterState.minPrice!==null || filterState.maxPrice!==null) {
            binding.root.chipGroup.addView(createFiterChip("Prix")  {
                filterState.maxPrice = null
                filterState.minPrice  =null
                upadateList(filterState)
            })
            count += 1
        }
        if (filterState.minDistance!==null || filterState.maxDistance!==null) {
            binding.root.chipGroup.addView(createFiterChip("Distance")  {
                filterState.minDistance = null
                filterState.maxDistance  =null
                upadateList(filterState)

            })
            count += 1
        }
        if (filterState.equipements !== null) {
            binding.root.chipGroup.addView(createFiterChip("Equipements")  {
                filterState.equipements = null
                upadateList(filterState)
            })
            count += 1
        }
        if (count>0) {
            binding.root.filter_count.text = "($count)"
            binding.root.filter_info.visibility = VISIBLE
        } else {
            binding.root.filter_info.visibility = GONE

        }
    }

    private fun createFiterChip(text:String, onClose: (View) -> Unit):Chip {
        val chip = Chip(context!!,null,R.style.Widget_MaterialComponents_Chip_Entry)
        chip.isClickable = true
        chip.isCloseIconVisible = true
        chip.text= text
        chip.setOnCloseIconClickListener(onClose)
        return chip
    }

    private fun upadateList(filterState: FilterParkingsModel) {
        mParkingListViewModel.receiveFilter(filterState).observe(this, Observer<ArrayList<Parking>>
        {
            mParkingListViewModel.postFilteredList(it)
        })
    }

}
interface ParkingItemListener : MyAdapter.ItemAdapterListener<Parking> {
    fun navigateToParking(parking: Parking)
    fun addToFavorites(parking: Parking)
}
