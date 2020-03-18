package com.example.myparking.fragements

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.myparking.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.here.android.mpa.guidance.NavigationManager
import com.here.android.mpa.mapping.Map
import com.here.android.mpa.routing.Route
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import com.here.android.mpa.common.GeoCoordinate


class NavAppBottomDialog(val map: Map, val route: Route) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.nav_app_botom_dialog,container,false)
        view.findViewById<LinearLayout>(R.id.gmaps_app).setOnClickListener {
            startGMAPSNavigation()
        }
        view.findViewById<LinearLayout>(R.id.myparking_app).setOnClickListener {
            startMyParkingNavigation()
        }
        return view
    }

    fun startGMAPSNavigation()  {
        val str = route.destination.latitude.toString()+","+route.destination.longitude.toString()
        val gmmIntentUri = Uri.parse("google.navigation:q=$str")
        val mapIntent = Intent(ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        context?.startActivity(mapIntent)

    }
    fun startMyParkingNavigation()  {
        val navigationManager = NavigationManager.getInstance()

        map.mapScheme = Map.Scheme.CARNAV_DAY
        map.addSchemeChangedListener {
            Log.d("nav","here schem nav")
            dismiss()
            navigationManager.setMap(map)
            val error = navigationManager.startNavigation(route)
            map.zoomLevel = 18.0
            map.setCenter(route.start, Map.Animation.BOW)
            NavigationManager.getInstance().mapUpdateMode = NavigationManager.MapUpdateMode.ROADVIEW
            Log.d("errorNav", error.toString())
        }


    }



}