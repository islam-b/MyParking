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




class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    OnSearchListener, AppBarLayout.OnOffsetChangedListener {


    private lateinit var navController: NavController
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private var showSearch = false
    val networkReceiver = NetworkReceiver()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        MapsUtils.initLocationProvider(this)
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
            val dialog = SearchDialogFragment(this)
            dialog.show(supportFragmentManager, dialog.TAG1)
        }

        navController = Navigation.findNavController(this,R.id.my_nav_host_fragment)

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
                val dialog = SearchDialogFragment(this)
                dialog.show(supportFragmentManager, dialog.TAG1)
                //onSearchRequested()
                //startAutoCompleteIntent()
                return true
            }
        }
        return false
    }

    override fun onSearchClick(searchResult: SearchResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }

        }
        drawer.closeDrawer(GravityCompat.START)
        return true
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
