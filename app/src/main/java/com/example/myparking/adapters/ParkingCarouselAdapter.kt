package com.example.myparking.adapters

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.example.myparking.R
import com.example.myparking.databinding.ParkingCarouselItem2Binding
import com.example.myparking.databinding.ParkingCarouselItemBinding
import com.example.myparking.fragements.NavAppBottomDialog
import com.example.myparking.models.Parking

import com.example.myparking.models.ParkingModel
import com.example.myparking.models.RouteDetail
import com.google.android.gms.dynamic.SupportFragmentWrapper
import kotlinx.android.synthetic.main.parking_carousel_item2.view.*

class ParkingCarouselAdapter(val routeDetailsList:ArrayList<RouteDetail>,
                             val listener:ItemAdapterListener<RouteDetail>) :
    MyAdapter<RouteDetail, ParkingCarouselItem2Binding>(routeDetailsList,
        R.layout.parking_carousel_item2,listener) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val routeDetail = routeDetailsList[position]
        holder.binding.routedetail = routeDetail
        //holder.binding.root.parking_img.setImageResource(R.drawable.parking_p)
        holder.binding.root.setOnClickListener {
            listener.onItemClicked(routeDetailsList[position])
        }
        holder.binding.root.go_to.setOnClickListener {
            NavAppBottomDialog(routeDetail.map, routeDetail.route).show(frgManager, "CHOOSE_NAV_APP")
        }
    }

    lateinit var frgManager: FragmentManager


}