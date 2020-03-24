package com.example.myparking.adapters

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.example.myparking.R
import com.example.myparking.databinding.ParkingCarouselItem2Binding
import com.example.myparking.databinding.ParkingCarouselItemBinding
import com.example.myparking.fragements.NavAppBottomDialog
import com.example.myparking.fragements.NavigationListener
import com.example.myparking.models.Parking

import com.example.myparking.models.ParkingModel
import com.example.myparking.models.RouteDetail
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.here.android.mpa.common.GeoCoordinate
import com.here.android.mpa.mapping.MapRoute
import com.here.android.mpa.routing.CoreRouter
import com.here.android.mpa.routing.Route
import com.here.android.mpa.routing.RouteResult
import com.here.android.mpa.routing.RoutingError
import kotlinx.android.synthetic.main.parking_carousel_item2.view.*

class ParkingCarouselAdapter(val parkings:ArrayList<Parking>,
                             val navigationListener: NavigationListener) :
    MyAdapter<Parking, ParkingCarouselItemBinding>(parkings,
        R.layout.parking_carousel_item,navigationListener) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val parking = parkings[position]
        holder.binding.parking = parking
        //holder.binding.root.parking_img.setImageResource(R.drawable.parking_p)
        holder.binding.root.setOnClickListener {
            navigationListener.onItemClicked(parkings[position])
        }
        holder.binding.root.go_to.setOnClickListener {
            navigationListener.showNavigationChoice()
        }

    }


    private lateinit var route: Route

    fun calcualteRoute(currentCooridinates: GeoCoordinate,target:Parking, listener: CoreRouter.Listener ) {
        val points = arrayListOf<GeoCoordinate>(
            GeoCoordinate(currentCooridinates.latitude, currentCooridinates.longitude),
            GeoCoordinate(target.lattitude, target.longitude)
        )
    }

    fun onProgress(progress: Int) {

    }

    fun onCalculateRouteFinished(p0: MutableList<RouteResult>?, p1: RoutingError?) {

    }



    //lateinit var frgManager: FragmentManager


}