package com.example.myparking

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CursorAdapter
import android.widget.ListAdapter
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.myparking.activities.FavoriteParkingsActivity
import com.example.myparking.activities.MesReservationsActivity
import com.example.myparking.adapters.OnSearchListener
import com.example.myparking.fragements.*
import com.example.myparking.models.ParkingModel


import com.example.myparking.models.SearchModel
import com.example.myparking.models.SearchResult
import com.example.myparking.utils.MapsUtils
import com.example.myparking.utils.NetworkReceiver
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.luseen.spacenavigation.SpaceItem
import com.luseen.spacenavigation.SpaceNavigationView
import com.luseen.spacenavigation.SpaceOnClickListener
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.libraries.places.widget.AutocompleteActivity
/**
 * Main activity
 * This activity contains two view: Map view and List view*
 * @author BOUAYACHE
 * @property drawer the drawer layout of tha main activity
 * @property toggle the Toggle action of the drawer layout
 * @property currentItem the current index of the view (0 or 1)
 * @property networkReceiver the Broadcast Receiver, it receives the connectivity state
 */

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener , SpaceOnClickListener,
    OnSearchListener {



    private lateinit var searchListener: OnSearchListener
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private var currentItem : Int? = 0
    val networkReceiver = NetworkReceiver()
    private lateinit var spaceNavigationView : SpaceNavigationView

    /**
     * This function initialize the main activity:
     * - Initializing Google Maps Configuratino
     * - Initializing Toolbars and navigation
     * - Starting the activity as List view
     */


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //NetworkUtil.registerConnectivityNetworkMonitor(this)
        val bundle = intent.extras
        bundle?.let {
            bundle.apply {
                currentItem = getInt("CURRENT_VIEW")
                Log.d("curreenTTT", currentItem.toString())
            }
        }

        // registerReceiver(networkReceiver, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))

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

        spaceNavigationView = nav_view as SpaceNavigationView
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState)
        spaceNavigationView.addSpaceItem(SpaceItem(0,"Liste", R.drawable.list_view))
        spaceNavigationView.addSpaceItem(SpaceItem(1,"Carte", R.drawable.map_view))
        spaceNavigationView.setCentreButtonIconColorFilterEnabled(false)
        spaceNavigationView.setSpaceOnClickListener(this)



            if (currentItem ==0 ) onItemClick(currentItem!!,"Liste")
            else spaceNavigationView.changeCurrentItem(currentItem!!)





    }



    /**
     * This function opens the filter dialog, it is triggered when the user click on the filter button
     */
    override fun onCentreButtonClick() {
        val dialog = FilterDialogFragment()
        dialog.show(supportFragmentManager, dialog.TAG1)

    }

    override fun onSearchClick(searchResult: SearchResult) {
        if (currentItem==0) {
            val spaceNavigationView = nav_view as SpaceNavigationView
            spaceNavigationView.changeCurrentItem(1)
        }
        searchListener.onSearchClick(searchResult)

    }

    /**
     * This function opens one of the views (list or map) depending on the clicked item
     * @param itemIndex the index of the view selected
     * @param itemName the name (tag) of the view selected
     */
    override fun onItemClick(itemIndex: Int, itemName: String?) {
        var fragment: Fragment
        currentItem = itemIndex
        Log.d("item ",itemIndex.toString())
        when (itemIndex) {
            0-> {
                Log.d("item clicked","item 0")
                fragment = ParkingsList()
            }
            1-> {
                fragment = ParkingsMap()
            }
            else -> {
                fragment = ParkingsList()
            }
        }
        supportFragmentManager.beginTransaction().replace(R.id.nav_host,fragment).commit()
    }



    override fun onItemReselected(itemIndex: Int, itemName: String?) {

    }



    /**
     * This function allows to return to previous activity
     */

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            val spaceNavigationView = nav_view as SpaceNavigationView
            if (currentItem == 1)
            spaceNavigationView.changeCurrentItem(0)
            else super.onBackPressed()
        }
    }

    /**
     * This function manage the drawer layout navigation, it perform actions according to selected items from the drawer navigation
     * @param item the item selected
     * @return boolean value representing success or failure
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_fav -> {
                val intent= FavoriteParkingsActivity.newIntent(this)
                startActivity(intent)
            }
            R.id.nav_reservations ->{
                val intent = Intent(applicationContext, MesReservationsActivity::class.java)
                intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }

        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    /**
     * This function create the options for the toolbar menu
     * @return boolean value representing success or failure
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main2, menu)
        return true
    }

    /**
     * This function manage menu items actions, it perform actions according to selected items from the toolbar menu
     * @return boolean value representing success or failure of the action
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                Log.d("CLICK", "SEARCH BTN CLICKED")
                val dialog = SearchDialogFragment(this)
                dialog.show(supportFragmentManager, dialog.TAG1)
                //onSearchRequested()
                //startAutoCompleteIntent()
                return true
            }
        }
        return false
    }


    override fun onAttachFragment(fragment: Fragment) {
        if (fragment is ParkingsMap) {
            searchListener = fragment.getSearchListner()
        }
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
            AutocompleteActivityMode.OVERLAY, fields)
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

    companion object {
        /**
         * Create new intent to start the main activity
         * @return new main activity intent
         */
        fun newIntent(context: Context, currentView: Int): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("CURRENT_VIEW", currentView)
            return intent
        }
    }

}
