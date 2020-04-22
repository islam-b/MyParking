package com.example.myparking.fragements

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myparking.MainActivity
import com.example.myparking.MainActivity.Companion.LIST_VIEW
import com.example.myparking.MainActivity.Companion.MAP_VIEW
import com.example.myparking.R
import com.example.myparking.activities.MesReservationsActivity
import com.example.myparking.activities.ParkingsDetailsContainer
import com.example.myparking.activities.ReservationDetailsActivity
import com.example.myparking.adapters.*
import com.example.myparking.databinding.ActivityHomeBinding
import com.example.myparking.models.Parking
import com.example.myparking.models.Reservation
import com.example.myparking.repositories.FavoriteParkingRepository
import com.example.myparking.repositories.ParkingListRepository
import com.example.myparking.repositories.ReservationListRepository
import com.example.myparking.utils.PreferenceManager
import com.example.myparking.viewmodels.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityHomeBinding
    private lateinit var mParkingListViewModel: ParkingListViewModel
    private lateinit var mReservationListViewModel: ReservationListViewModel
    private lateinit var mFavoriteParkingViewModel: FavoriteParkingViewModel
    private lateinit var mParkingListAdapter: HomeParkingAdapter
    private lateinit var mReservationListAdapter: HomeReservationAdapter
    private var nearParkingList = ArrayList<Parking>()
    private var favoriteParkings = ArrayList<Parking>()
    private var recentReservationList = ArrayList<Reservation>()
    val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.FRANCE)

    private lateinit var  mFavoriteParkings: HomeFavoriteParkingAdapter

    fun initNavigation(view: View) {
        navController = Navigation.findNavController(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //for navigation
        initNavigation(view)
        val lastLocation = PreferenceManager(context!!).getLastLocationStr()
        val idDriver = PreferenceManager(context!!).checkDriverProfile().toInt()

        val factoryParking = ParkingListViewModelFactory(ParkingListRepository.getInstance(),idDriver,lastLocation,null)

        mParkingListViewModel = ViewModelProviders.of(this, factoryParking)
            .get(ParkingListViewModel::class.java)
        mParkingListViewModel.getParkingsList().observe(this, Observer<ArrayList<Parking>>
        {list ->
            hideLoading()
            val newList = ArrayList(list.sortedWith(compareBy {it.routeInfo?.walkingDistance}))
            mParkingListAdapter?.updateList(ArrayList(newList.takeLast(3)))


        })
        val factoryReservation = ReservationListViewModelFactory(idDriver, ReservationListRepository.getInstance())

        mReservationListViewModel = ViewModelProviders.of(this, factoryReservation)
            .get(ReservationListViewModel::class.java)
        mReservationListViewModel.getReservationsList().observe(this, Observer<ArrayList<Reservation>>
        {list->
            hideLoading()
            val newList = list.sortedWith(compareBy{df.parse(it.dateEntreePrevue)})
            mReservationListAdapter?.updateList(ArrayList(newList.take(3)))
        })


        val factoryFav = FavoriteParkingViewModelFactory(FavoriteParkingRepository.getInstance(),idDriver,lastLocation,null)
        mFavoriteParkingViewModel = ViewModelProviders.of(this, factoryFav)
            .get(FavoriteParkingViewModel::class.java)

        mFavoriteParkingViewModel.getFavoriteParkings().observe(this, Observer<ArrayList<Parking>>
        {list ->
            val newList = ArrayList(list.sortedWith(compareBy {it.routeInfo?.walkingDistance}))
            mFavoriteParkings.updateList(ArrayList(newList.take(3)))
        })


        showLoading()
        initNearParkings()
        initReservations()
        initFavoriteParkings()

        // on click listeners

        binding.root.see_more_parkings.setOnClickListener {
            goToMainActivity(LIST_VIEW)
        }
        binding.root.see_more_reservations.setOnClickListener {
            goToReservationsListActivity()
        }
        binding.root.see_more_favorites.setOnClickListener {
            goToFavoriteParkings()
        }
        binding.root.vue_carte_btn.setOnClickListener {
            goToMainActivity(MAP_VIEW)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.activity_home, container, false)
        return binding.root
    }

    fun goToParkingDetails( idParking: Int) {
        val list = mParkingListViewModel.getParkingsList().value!!
        val parking = list.find { p -> idParking == p.idParking }
        val index = list.indexOf(parking)
        val bundle= bundleOf("parking" to parking, "parkingIndex" to index)
        navController.navigate(R.id.action_homeFragment_to_parkingsDetailsContainer, bundle)

    }
    fun goToReservationDetails( reservation: Reservation) {
        val bundle= bundleOf("reservation" to reservation)
        navController.navigate(R.id.action_homeFragment_to_reservationDetailsActivity, bundle)
    }
    fun goToFavoriteParkings() {
        navController.navigate(R.id.action_homeFragment_to_favoriteParkingsActivity)
    }


    fun initNearParkings() {
        near_parkings.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        mParkingListAdapter = HomeParkingAdapter(nearParkingList,object: MyAdapter.ItemAdapterListener<Parking> {
            override fun onItemClicked(item: Parking) {
                Log.d("itemClicked", item.nom + " " + item.idParking+ " " + nearParkingList.indexOf(item))
                goToParkingDetails(item.idParking)
            }

        })
        near_parkings.adapter = mParkingListAdapter
    }
    fun initReservations() {
        home_reservations_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        mReservationListAdapter = HomeReservationAdapter(recentReservationList,object: MyAdapter.ItemAdapterListener<Reservation> {
            override fun onItemClicked(item: Reservation) {
                goToReservationDetails(item)

            }

        })
        home_reservations_list.adapter = mReservationListAdapter
    }
    fun initFavoriteParkings() {
        home_favorites_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        mFavoriteParkings = HomeFavoriteParkingAdapter(favoriteParkings ,object:
            MyAdapter.ItemAdapterListener<Parking> {
            override fun onItemClicked(item: Parking) {
                goToParkingDetails(item.idParking)

            }

        })
        home_favorites_list.adapter = mFavoriteParkings

    }



    fun goToMainActivity(viewType: Int) {
        val bundle= bundleOf("viewType" to viewType)
        navController.navigate(R.id.action_homeFragment_to_mainActivity2, bundle)

    }

    fun goToReservationsListActivity() {
        navController.navigate(R.id.action_homeFragment_to_mesReservationsActivity)
    }

    fun showLoading() {
        binding.root.home_activity_content.visibility = GONE
        binding.root.shimmer_home_activity.visibility = VISIBLE
        binding.root.shimmer_home_activity.startShimmer()
    }

    fun hideLoading() {
        binding.root.shimmer_home_activity.stopShimmer()
        binding.root.shimmer_home_activity.visibility = GONE
        binding.root.home_activity_content.visibility = VISIBLE

    }



}
