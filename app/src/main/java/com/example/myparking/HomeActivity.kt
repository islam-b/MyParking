package com.example.myparking

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



class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Places.initialize(applicationContext, "AIzaSyCDbn_Le90eo8Ry1UEb5GFYIz80Dv4INdY")

        // Create a new Places client instance
        val placesClient = Places.createClient(this)


        setContentView(R.layout.activity_home)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        setSupportActionBar(toolbar_home)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""
        toolbar_home.setNavigationIcon(R.drawable.ic_menu)

        binding.searchInput.setOnClickListener {
            startAutoCompleteIntent()
        }

        initNearParkings()
        initReservations()
        initFavoriteParkings()


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
        near_parkings.adapter = HomeParkingAdapter(DataSource.getParkings(),object:MyAdapter.ItemAdapterListener<Parking> {
            override fun onItemClicked(item: Parking) {

            }

        })
    }
    fun initReservations() {
        home_reservations_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        home_reservations_list.adapter = HomeReservationAdapter(DataSource.getReservations(),object:MyAdapter.ItemAdapterListener<Reservation> {
            override fun onItemClicked(item: Reservation) {

            }

        })
    }
    fun initFavoriteParkings() {
        home_favorites_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        home_favorites_list.adapter = HomeFavoriteParkingAdapter(DataSource.getParkings(),object:MyAdapter.ItemAdapterListener<Parking> {
            override fun onItemClicked(item: Parking) {

            }

        })

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
