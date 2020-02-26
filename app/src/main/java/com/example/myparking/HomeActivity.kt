package com.example.myparking

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myparking.adapters.HomeFavoriteParkingAdapter
import com.example.myparking.adapters.HomeParkingAdapter
import com.example.myparking.adapters.HomeReservationAdapter
import com.example.myparking.adapters.MyAdapter
import com.example.myparking.databinding.ActivityHomeBinding
import com.example.myparking.models.Parking
import com.example.myparking.models.Reservation
import com.example.myparking.utils.DataSource
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.android.synthetic.main.activity_home.*
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import android.os.Build.VERSION_CODES.KITKAT
import android.os.Build.VERSION.SDK_INT
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.myparking.activities.MesReservationsActivity
import com.example.myparking.activities.ParkingsDetailsContainer
import com.example.myparking.activities.ReservationDetailsActivity
import com.example.myparking.repositories.ParkingListRepository
import com.example.myparking.repositories.ReservationListRepository
import com.example.myparking.viewmodels.ParkingListViewModel
import com.example.myparking.viewmodels.ParkingListViewModelFactory
import com.example.myparking.viewmodels.ReservationListViewModel
import com.example.myparking.viewmodels.ReservationListViewModelFactory
import kotlinx.android.synthetic.main.activity_home.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var mParkingListViewModel: ParkingListViewModel
    private lateinit var mReservationListViewModel: ReservationListViewModel
    private lateinit var mParkingListAdapter: HomeParkingAdapter
    private lateinit var mReservationListAdapter: HomeReservationAdapter
    private var nearParkingList = ArrayList<Parking>()
    private var recentReservationList = ArrayList<Reservation>()
    val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.FRANCE)

    private lateinit var  mFavoriteParkings: HomeFavoriteParkingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Places.initialize(applicationContext, "AIzaSyCDbn_Le90eo8Ry1UEb5GFYIz80Dv4INdY")

        // Create a new Places client instance
        val placesClient = Places.createClient(this)


        setContentView(R.layout.activity_home)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        if (SDK_INT >= KITKAT) {
            val w = window
            w.setFlags(
                FLAG_LAYOUT_NO_LIMITS,
                FLAG_LAYOUT_NO_LIMITS
            )

        }

        setSupportActionBar(toolbar_home)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""
        toolbar_home.setNavigationIcon(R.drawable.ic_menu)

        binding.searchInput.setOnClickListener {
            startAutoCompleteIntent()
        }
        val factoryParking = ParkingListViewModelFactory(ParkingListRepository.getInstance())

        mParkingListViewModel = ViewModelProviders.of(this, factoryParking)
            .get(ParkingListViewModel::class.java)
        mParkingListViewModel.getParkingsList().observe(this, Observer<ArrayList<Parking>>
        {list ->
            val newList = ArrayList(list.sortedWith(compareBy {it.routeInfo?.walkingDistance}))
            mParkingListAdapter?.updateList(ArrayList(newList.takeLast(3)))
            mFavoriteParkings?.updateList(ArrayList(newList.take(3)))
        })
        val factoryReservation = ReservationListViewModelFactory(1, ReservationListRepository.getInstance())

        mReservationListViewModel = ViewModelProviders.of(this, factoryReservation)
            .get(ReservationListViewModel::class.java)
        mReservationListViewModel.getReservationsList().observe(this, Observer<ArrayList<Reservation>>
        {list->
            val newList = list.sortedWith(compareBy{df.parse(it.dateEntreePrevue)})
            mReservationListAdapter?.updateList(ArrayList(list.take(3)))
        })
        initNearParkings()
        initReservations()
        initFavoriteParkings()

        // on click listeners

        binding.root.see_more_parkings.setOnClickListener {
            goToMainActivity(0)
        }
        binding.root.see_more_reservations.setOnClickListener {
            goToReservationsListActivity() // find a way to go to right activity on backpress
        }
        binding.root.see_more_favorites.setOnClickListener {
            goToMainActivity(0) // change to favorites
        }
        binding.root.vue_carte_btn.setOnClickListener {
            goToMainActivity(1)
        }


    }


    fun goToMainActivity(viewType: Int) {
        val mainActivityIntent = MainActivity.newIntent(this, viewType)
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(mainActivityIntent)
    }

    fun goToReservationsListActivity() {
        val intent = Intent(applicationContext, MesReservationsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    fun goToParkingDetails( idParking: Int) {
        startActivity(ParkingsDetailsContainer.newIntent(this,  mParkingListViewModel.getParkingsList().value!!, idParking))
        finish()
    }
    fun goToReservationDetails( reservation: Reservation) {
        startActivity(ReservationDetailsActivity.newIntent(this, reservation))
        finish()
    }
    /**
     * This function create the options for the toolbar menu
     * @return boolean value representing success or failure
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    fun initNearParkings() {
        near_parkings.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        mParkingListAdapter = HomeParkingAdapter(nearParkingList,object:MyAdapter.ItemAdapterListener<Parking> {
            override fun onItemClicked(item: Parking) {
                Log.d("itemClicked", item.nom + " " + item.idParking+ " " + nearParkingList.indexOf(item))
                goToParkingDetails(item.idParking)
            }

        })
        near_parkings.adapter = mParkingListAdapter
    }
    fun initReservations() {
        home_reservations_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        mReservationListAdapter = HomeReservationAdapter(recentReservationList,object:MyAdapter.ItemAdapterListener<Reservation> {
            override fun onItemClicked(item: Reservation) {
                goToReservationDetails(item)

            }

        })
        home_reservations_list.adapter = mReservationListAdapter
    }
    fun initFavoriteParkings() {
        home_favorites_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        mFavoriteParkings = HomeFavoriteParkingAdapter(nearParkingList /* to chnage */,object:MyAdapter.ItemAdapterListener<Parking> {
            override fun onItemClicked(item: Parking) {
                goToParkingDetails(item.idParking)

            }

        })
        home_favorites_list.adapter = mFavoriteParkings

    }


    val AUTOCOMPLETE_REQUEST_CODE = 1
    /**
     * Request an intent to show the search bar from googla maps SDK
     */
    fun startAutoCompleteIntent() {

        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        val fields = listOf(Place.Field.ID, Place.Field.NAME)

        // Start the autocomplete intent.
        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN, fields)
            .build(this)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }


    /**
     * Handle the result of an intent request
     * @param requestCode the code of the request
     * @param resultCode the code of the result of the request
     * @param data the data attached to the result
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                Log.d( "Place: " , place.name + ", " + place.id)
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                val status = Autocomplete.getStatusFromIntent(data!!)
                //Log.i(FragmentActivity.TAG, status.statusMessage)
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}
