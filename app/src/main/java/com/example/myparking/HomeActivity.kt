package com.example.myparking

import android.R.attr.*
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.libraries.places.api.Places
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.myparking.activities.FavoriteParkingsActivity
import com.example.myparking.activities.MesReservationsActivity
import com.example.myparking.adapters.*
import com.example.myparking.fragements.SearchDialogFragment
import com.example.myparking.models.SearchResult
import com.example.myparking.utils.MapsUtils
import com.example.myparking.utils.NetworkReceiver
import com.google.android.material.navigation.NavigationView
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.home_nav_layout.*
import kotlin.math.abs
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.location.Location
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.myparking.activities.LoginActivity
import com.example.myparking.fragements.OnLocationListener
import com.example.myparking.utils.AnimationUtils.convertDpToPixel
import com.example.myparking.utils.PreferenceManager
import com.google.android.gms.location.LocationCallback
import kotlinx.android.synthetic.main.activity_parking_details.*


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    AppBarLayout.OnOffsetChangedListener,
    NavController.OnDestinationChangedListener {


    private lateinit var navController: NavController
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private var showSearch = false
    val networkReceiver = NetworkReceiver()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        MapsUtils.initLocationProvider(this)
        MapsUtils.getLastLocation(this,object:LocationCallback(){

        } ,object :OnLocationListener{
            override fun onLocationReady(location: Location) {

            }

        })
        Places.initialize(applicationContext, "AIzaSyCDbn_Le90eo8Ry1UEb5GFYIz80Dv4INdY")
        // Create a new Places client instance
        val placesClient = Places.createClient(this)


        setContentView(R.layout.home_nav_layout)


        drawer= findViewById(R.id.drawer_layout)



//        if (SDK_INT >= KITKAT) {
//            val w = window
//            w.setFlags(
//                FLAG_LAYOUT_NO_LIMITS,
//                FLAG_LAYOUT_NO_LIMITS
//            )
//
//        }

        val navigationView: NavigationView = findViewById(R.id.side_view)
        navigationView.setNavigationItemSelectedListener(this)



        search_input.setOnClickListener {
            val dialog = SearchDialogFragment()
            dialog.show(supportFragmentManager, dialog.TAG1)
        }

        navController = Navigation.findNavController(this,R.id.my_nav_host_fragment)
        navController.addOnDestinationChangedListener(this)
        setupActionBar()


    }

    override fun onOffsetChanged(p0: AppBarLayout?, p1: Int) {
        search_card.alpha = ((p0?.totalScrollRange!! + p1).toFloat() / p0.totalScrollRange)
        if (abs(p1) == p0.totalScrollRange) {
            showSearch = true
        } else if (p1==0) {
            showSearch = false
        }
        invalidateOptionsMenu()
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.homeFragment -> {
                customizeToolbar("",110f,true)
            }
            R.id.parkingsDetailsContainer -> {
                customizeToolbar("",0f,false)
            }
            R.id.mesReservationsActivity -> {
                customizeToolbar("Mes réservations",56f,false)
            }
            R.id.favoriteParkingsActivity -> {
                customizeToolbar("Mes parkings favoris",56f,false)
            }
            R.id.reservationActivity -> {
                customizeToolbar("Réserver un parking",56f,false)
            }
            R.id.reservationDetailsActivity -> {
                customizeToolbar("Détails réservations",56f,false)
            }
            else ->{
                customizeToolbar("",56f,false)
            }
        }
    }

    fun customizeToolbar(title: String, height:Float, isExpanded: Boolean) {
        val layouParams = CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT,convertDpToPixel(height))
        collapsing_app_bar.layoutParams = layouParams
        search_card.visibility = if (isExpanded) {VISIBLE} else {GONE}
        collapsing_app_bar.setExpanded(isExpanded)
        if (title=="") {
            logo.visibility = VISIBLE
            frag_title.visibility = GONE
        } else {
            logo.visibility = GONE
            frag_title.visibility = VISIBLE
            frag_title.text = title
        }


    }



    private fun setupActionBar() {
        val toolbar : Toolbar
        toolbar = toolbar_home
        setSupportActionBar(toolbar)
        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toolbar.setupWithNavController(navController, drawer)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""
        toolbar.setNavigationIcon(R.drawable.ic_menu)
        collapsing_app_bar.addOnOffsetChangedListener(this)

    }




    /**
     * This function create the options for the toolbar menu
     * @return boolean value representing success or failure
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home_menu, menu)
        val item = menu.findItem(R.id.action_search)
        item.isVisible = showSearch
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
                val dialog = SearchDialogFragment()
                dialog.show(supportFragmentManager, dialog.TAG1)
                //onSearchRequested()
                //startAutoCompleteIntent()
                return true
            }
        }
        return false
    }



    /**
     * This function manage the drawer layout navigation, it perform actions according to selected items from the drawer navigation
     * @param item the item selected
     * @return boolean value representing success or failure
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_fav -> {
                navController.navigate(R.id.action_homeFragment_to_favoriteParkingsActivity)
            }
            R.id.nav_reservations ->{
                navController.navigate(R.id.action_homeFragment_to_mesReservationsActivity)
            }
            R.id.nav_logout -> {
                PreferenceManager(this).destroyProfile()
                signOut()
            }

        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun signOut() {
        val loginAct = Intent(this, LoginActivity::class.java)
        loginAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(loginAct)
    }


    /**
     * This function allows to return to previous activity
     */

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            /*val spaceNavigationView = nav_view as SpaceNavigationView
            if (currentItem == 1)
                spaceNavigationView.changeCurrentItem(0)
            else */super.onBackPressed()
        }
    }


}
