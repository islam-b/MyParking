package com.example.myparking.fragements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.myparking.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NavAppBottomDialog : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.nav_app_botom_dialog,container,false)
        view.findViewById<LinearLayout>(R.id.gmaps_app).setOnClickListener {
            startGMAPSNavigation()
        }
        view.findViewById<LinearLayout>(R.id.gmaps_app).setOnClickListener {
            startMyParkingNavigation()
        }
        return view
    }

    fun startGMAPSNavigation()  {

    }
    fun startMyParkingNavigation()  {

    }



}