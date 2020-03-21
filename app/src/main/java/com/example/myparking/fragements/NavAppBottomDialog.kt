package com.example.myparking.fragements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.myparking.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment



class NavAppBottomDialog(val listener: NavigationListener) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.nav_app_botom_dialog,container,false)
        view.findViewById<LinearLayout>(R.id.gmaps_app).setOnClickListener {
            listener.startGMAPSNavigation()
        }
        view.findViewById<LinearLayout>(R.id.myparking_app).setOnClickListener {
            listener.startMyParkingNavigation()
        }
        return view
    }
}