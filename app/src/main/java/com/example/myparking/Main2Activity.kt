package com.example.myparking

import android.os.Bundle
import androidx.navigation.ui.AppBarConfiguration
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.myparking.adapters.ListAdapter
import com.example.myparking.fragements.ParkingsList
import com.example.myparking.fragements.ParkingsMap
import com.example.myparking.utils.MapsUtils
import com.luseen.spacenavigation.SpaceItem
import com.luseen.spacenavigation.SpaceNavigationView
import com.luseen.spacenavigation.SpaceOnClickListener
import kotlinx.android.synthetic.main.activity_main.*

class Main2Activity : AppCompatActivity(), ListAdapter.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener , SpaceOnClickListener{



    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        MapsUtils.initLocationProvider(this)

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
        spaceNavigationView.setSpaceOnClickListener(this)
        onItemClick(0,"Liste")

    }


    override fun onCentreButtonClick() {

    }

    override fun onItemClick(itemIndex: Int, itemName: String?) {
        var fragment: Fragment
        when (itemIndex) {
            0-> {
                fragment = ParkingsList.newInstance("","")

            }
            1-> {
                fragment = ParkingsMap.newInstance()
            }
            else -> {
                fragment = ParkingsList.newInstance("","")
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
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main2, menu)
        return true
    }

    override fun OnItemClick(item: Any) {
    }


//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }
}
