package com.example.myparking.adapters

import android.content.Context
import android.view.View
import android.view.View.*
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.example.myparking.R
import com.example.myparking.databinding.ParkingCarouselItem2Binding
import com.example.myparking.databinding.ParkingCarouselItemBinding
import com.example.myparking.fragements.NavAppBottomDialog
import com.example.myparking.fragements.NavigationListener
import com.example.myparking.models.Parking

import com.example.myparking.models.ParkingModel
import com.example.myparking.models.RouteDetail
import com.example.myparking.models.SearchResult
import com.example.myparking.utils.AnimationUtils
import com.example.myparking.utils.MapsUtils
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.here.android.mpa.common.GeoCoordinate
import com.here.android.mpa.mapping.MapRoute
import com.here.android.mpa.routing.CoreRouter
import com.here.android.mpa.routing.Route
import com.here.android.mpa.routing.RouteResult
import com.here.android.mpa.routing.RoutingError
import kotlinx.android.synthetic.main.parking_carousel_item.view.*
import kotlinx.android.synthetic.main.parking_carousel_item2.view.*
import kotlinx.android.synthetic.main.parking_carousel_item2.view.go_to

class ParkingCarouselAdapter(val parkings:ArrayList<Parking>,
                             val navigationListener: NavigationListener, var destination: SearchResult?) :
    MyAdapter<Parking, ParkingCarouselItemBinding>(parkings,
        R.layout.parking_carousel_item,navigationListener) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val parking = parkings[position]
        val view = holder.binding.root
        holder.binding.parking = parking
        if (destination==null) {
            view.nav_parking_info.visibility = INVISIBLE
            view.nav_parking_info.layoutParams = ConstraintLayout.LayoutParams(0,AnimationUtils.convertDpToPixel(30f))
            view.parking_indic.visibility = GONE
            view.destination_indic.setImageResource(R.drawable.parking_ico)
            view.destination_indic.setColorFilter(ContextCompat.getColor(holder.binding.root.context, R.color.colorPrimary))
            view.destination_info_txt.text = parking.nom
            view.destination_label.text = "Parking"
            view.walk_btn.visibility = GONE
        } else {
            view.parking_info_txt.text = parking.nom
            view.destination_info_txt.text = destination!!.title
            if (parking.isWalkable()) {
                view.walk_btn.visibility = VISIBLE
            }
            else {
                view.walk_btn.visibility = GONE
            }

        }
        parking.routeInfo?.let {
            view.distance_txt.text = (it.travelDistance/1000).toString() + " km"
            view.time_txt.text = (it.travelTime/60).toString() + " min"
        }
        view.car_btn.setOnClickListener { switchRouteInfo(view.car_btn, view,parking) }
        view.walk_btn.setOnClickListener { switchRouteInfo(view.walk_btn, view,parking) }

        holder.binding.root.setOnClickListener {
            navigationListener.onItemClicked(parkings[position])
        }
        holder.binding.root.go_to.setOnClickListener {
            navigationListener.showNavigationChoice()
        }



    }

    private var byCar = true

//    fun setDestination(destination: SearchResult?) {
//        val clone = ArrayList(parkings)
//        this.destination = destination
//        super.updateList(clone)
//    }

    private fun switchRouteInfo(source: View, root: View, parking:Parking) {
        if (byCar && source==root.walk_btn) {
            root.car_btn.setColorFilter(ContextCompat.getColor(root.context,R.color.inactive_route_inco))
            root.walk_btn.setColorFilter(ContextCompat.getColor(root.context,R.color.colorPrimary))
            parking.routeInfo?.let {
                root.distance_txt.text = (it.walkingDistance/1000).toString() + " km"
                root.time_txt.text = (it.walkingTime/60).toString() + " min"
            }
            byCar = !byCar

        } else if (!byCar && source==root.car_btn) {
            root.walk_btn.setColorFilter(ContextCompat.getColor(root.context,R.color.inactive_route_inco))
            root.car_btn.setColorFilter(ContextCompat.getColor(root.context,R.color.colorPrimary))
            parking.routeInfo?.let {
                root.distance_txt.text = (it.travelDistance/1000).toString() + " km"
                root.time_txt.text = (it.travelTime/60).toString() + " min"
            }
            byCar = !byCar
        }

    }



}