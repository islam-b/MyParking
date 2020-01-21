package com.example.myparking

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.myparking.activities.FavoriteParkingsActivity
import com.example.myparking.activities.MesReservationsActivity
import com.example.myparking.activities.ReservationDetailsActivity


import com.example.myparking.fragements.FilterDialogFragment
import com.example.myparking.fragements.ParkingsList
import com.example.myparking.fragements.ParkingsMap
import com.example.myparking.utils.MapsUtils
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.luseen.spacenavigation.SpaceItem
import com.luseen.spacenavigation.SpaceNavigationView
import com.luseen.spacenavigation.SpaceOnClickListener
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.libraries.places.widget.AutocompleteActivity



class Main2Activity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener , SpaceOnClickListener{



    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        MapsUtils.initLocationProvider(this)
        Places.initialize(applicationContext, "AIzaSyCDbn_Le90eo8Ry1UEb5GFYIz80Dv4INdY")

        // Create a new Places client instance
        val placesClient = Places.createClient(this)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        drawer= findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)

        val navigationView: NavigationView = findViewById(R.id.side_view)
        navigationView.setNavigationItemSelectedListener(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        toolbar.setNavigationIcon(R.drawable.ic_menu)

        val spaceNavigationView = nav_view as SpaceNavigationView
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState)
        spaceNavigationView.addSpaceItem(SpaceItem(0,"Liste", R.drawable.list_view))
        spaceNavigationView.addSpaceItem(SpaceItem(1,"Carte", R.drawable.map_view))
        spaceNavigationView.setCentreButtonIconColorFilterEnabled(false)
        spaceNavigationView.setSpaceOnClickListener(this)
        onItemClick(0,"Liste")

    }


    override fun onCentreButtonClick() {
        val dialog = FilterDialogFragment()
        dialog.show(supportFragmentManager, dialog.TAG1)

    }

    override fun onItemClick(itemIndex: Int, itemName: String?) {
        var fragment: Fragment
        when (itemIndex) {
            0-> {
                fragment = ParkingsList()

            }
            1-> {
                fragment = ParkingsMap.newInstance()
            }
            else -> {
                fragment = ParkingsList()
            }
        }
        supportFragmentManager.beginTransaction().replace(R.id.nav_host,fragment).commit()
    }

    override fun onItemReselected(itemIndex: Int, itemName: String?) {

    }





    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_fav -> {
                val intent= FavoriteParkingsActivity.newIntent(this)
                startActivity(intent)
                finish()
            }
            R.id.nav_reservations ->{
                val intent = Intent(applicationContext, MesReservationsActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                Log.d("CLICK", "SEARCH BTN CLICKED")
                startAutoCompleteIntent()
                return true
            }
        }
        return false
    }

    val AUTOCOMPLETE_REQUEST_CODE = 1
    fun startAutoCompleteIntent() {

// Set the fields to specify which types of place data to
// return after the user has made a selection.
        val fields = listOf(Place.Field.ID, Place.Field.NAME);

// Start the autocomplete intent.
        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.OVERLAY, fields)
            .build(this)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }



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



    //    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }
}
