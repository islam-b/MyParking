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
import com.google.android.material.navigation.NavigationView
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.home_nav_layout.*
import kotlin.math.abs
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.location.Location
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.os.bundleOf
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.myparking.activities.LoginActivity
import com.example.myparking.fragements.OnLocationListener
import com.example.myparking.repositories.NotificationsRepository
import com.example.myparking.utils.*
import com.example.myparking.utils.AnimationUtils.convertDpToPixel
import com.example.myparking.viewmodels.NotificationViewModel
import com.example.myparking.viewmodels.NotificationViewModelFactory
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.firebase.messaging.RemoteMessage
import com.here.sdk.analytics.internal.HttpClient
import com.pusher.pushnotifications.BeamsCallback
import com.pusher.pushnotifications.PushNotificationReceivedListener
import com.pusher.pushnotifications.PushNotifications
import com.pusher.pushnotifications.PusherCallbackError
import kotlinx.android.synthetic.main.activity_parking_details.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    AppBarLayout.OnOffsetChangedListener,
    NavController.OnDestinationChangedListener {



    private lateinit var navController: NavController
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private var showSearch = false
    val networkReceiver = NetworkReceiver()
    private lateinit var notificationViewModel: NotificationViewModel
    private lateinit var prfMgr : PreferenceManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        prfMgr = PreferenceManager(this)
        prfMgr.clearDestinationLocation()
        val automobilisteId = prfMgr.checkDriverProfile()


        MapsUtils.initLocationProvider(this)

        MapsUtils.getLastLocation(this,object:OnLocationListener{
            override fun onLocationReady(location: Location) {
                prfMgr.writeLastLocation(location)
            }

        })
        //Places.initialize(applicationContext, "AIzaSyCDbn_Le90eo8Ry1UEb5GFYIz80Dv4INdY")
        // Create a new Places client instance
        //val placesClient = Places.createClient(this)
        InjectorUtils.provideAuthService().authBrainTree(automobilisteId.toInt()).enqueue(object: Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("token_brain_error", t.message.toString())
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
               if (response.code()==200) {
                   Log.d("token_brain",response.body()!!)
                   prfMgr.writeBrainTreeToken(response.body()!!)
               }
            }

        })

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
        initPusher()
        initViewModel()
        showDriverProfile()


    }


    private fun initViewModel() {
        val factory = NotificationViewModelFactory(NotificationsRepository.getInstance(this))
        notificationViewModel = ViewModelProviders.of(this, factory)
            .get(NotificationViewModel::class.java)
    }

    override fun onOffsetChanged(p0: AppBarLayout?, p1: Int) {
        search_card.alpha = ((p0?.totalScrollRange!! + p1).toFloat() / p0.totalScrollRange)
        if (abs(p1) == p0.totalScrollRange) {
            showSearch = true
            search_input.visibility =GONE
        } else if (p1==0) {
            showSearch = false
            search_input.visibility =VISIBLE
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
            R.id.notificationsFragment -> {
                customizeToolbar("Notifications",56f,false)
            }
            R.id.settingsFragment -> {
                customizeToolbar("Paramètres",56f,false)
            }
            R.id.contactUsFragment -> {
                customizeToolbar("Contactez-nous",56f,false)

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
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.isDrawerIndicatorEnabled = false

        toggle.setToolbarNavigationClickListener {
            drawer.openDrawer(GravityCompat.START)
        }
        drawer.addDrawerListener(toggle)

        //toolbar.setupWithNavController(navController, drawer)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""
        toolbar.setNavigationIcon(R.drawable.ic_menu)

//        toolbar.setNavigationOnClickListener {
//
//        }
        toggle.syncState()
        collapsing_app_bar.addOnOffsetChangedListener(this)
        invalidateOptionsMenu()
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

        notificationViewModel.getCount().observe(this, Observer<Int> {

        })

        return super.onCreateOptionsMenu(menu)
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
            R.id.notif_btn -> {
                navController.navigate(R.id.action_global_notificationsFragment)
                Log.d("NOTIF_BTN", "trying to click")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }



    /**
     * This function manage the drawer layout navigation, it perform actions according to selected items from the drawer navigation
     * @param item the item selected
     * @return boolean value representing success or failure
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_home -> {
                navController.navigate(R.id.action_global_homeFragment)
            }
            R.id.nav_fav -> {
                navController.navigate(R.id.action_global_favoriteParkingsActivity)
            }
            R.id.nav_reservations ->{
                navController.navigate(R.id.action_global_mesReservationsActivity)
            }
            R.id.nav_notifs -> {
                navController.navigate(R.id.action_global_notificationsFragment)
            }
//            R.id.nav_find_my_car -> {
//                val args = bundleOf("viewType" to MainActivity.MAP_VIEW, "actionType" to MapAction.MARK_CAR)
//                navController.navigate(R.id.action_global_mainActivity2, args)
//            }
            R.id.nav_settings -> {
                navController.navigate(R.id.action_global_settingsFragment)
            }
            R.id.nav_contact -> {
                navController.navigate(R.id.action_global_contactUsFragment)
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
            else */ super.onBackPressed()

        }
    }

    private fun showDriverProfile() {
        val infosList = prfMgr.getInfoDriver()
        val navView =  findViewById<NavigationView>(R.id.side_view)
        val headerView = navView.getHeaderView(0)
        val nomPrenom =  headerView.findViewById<TextView>(R.id.nom_prenom)
        nomPrenom.text = infosList[0] + " "+ infosList[1]
        val emailFb =  headerView.findViewById<TextView>(R.id.email_fb)
        emailFb.text = infosList[2]
    }


    override fun onResume() {
        super.onResume()
        MapsUtils.requestNewLocationData(this)

        if (PreferenceManager(this).isNotifActivated()) {
            PushNotifications.setOnMessageReceivedListenerForVisibleActivity(
                this,
                object : PushNotificationReceivedListener {
                    override fun onMessageReceived(remoteMessage: RemoteMessage) {
                        val messagePayload = remoteMessage.data["data"]
                        //notificationViewModel.insertNotif(remoteMessage.data)
                        if (messagePayload == null) {
                            // Message payload was not set for this notification
                            Log.i("MyActivity", "Payload was missing")
                        } else {
                            Log.i("MyActivity", messagePayload)

                            // Now update the UI based on your message payload!
                        }
                    }
                })
        }
    }

    fun initPusher() {


        val driverId = PreferenceManager(this).checkDriverProfile()
        try {
            PushNotifications.stop()
        } catch (e:Exception) {

        }

        PushNotifications.start(applicationContext, "68987f6c-1b73-4e06-8515-b8db77033090")
        PushNotifications.clearAllState()
        PushNotifications.addDeviceInterest("driver_notifs")
        PushNotifications.addDeviceInterest("debug-testdriver")
        PushNotifications.addDeviceInterest("drivers")
        PushNotifications.setUserId(
            "driver$driverId",
            BeamsTokenProvider.getTokenProvider(driverId),
            object : BeamsCallback<Void, PusherCallbackError> {
                override fun onFailure(error: PusherCallbackError) {
                    Log.e("BeamsAuth", "Could not login to Beams: ${error.message}")
                }

                override fun onSuccess(vararg values: Void) {
                    Log.i("BeamsAuth", "Beams login success")
                }
            }
        )

    }


}
