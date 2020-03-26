package com.example.myparking.adapters

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.myparking.databinding.ParkingItemBinding
import com.example.myparking.R
import com.example.myparking.models.Parking
import com.example.myparking.utils.loadImage
import kotlinx.android.synthetic.main.parking_item.view.*
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.example.myparking.MainActivity
import com.example.myparking.fragements.ParkingsMap
import com.example.myparking.utils.MapAction


class ParkingsListAdapter(var parkingsList : ArrayList<Parking>,
                       val listener: ItemAdapterListener<Parking>
                       ): MyAdapter<Parking, ParkingItemBinding>(parkingsList, R.layout.parking_item, listener)
{
    private val viewBinderHelper = ViewBinderHelper()
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.parking = parkingsList[position]
        loadImage(holder.binding.root.image, parkingsList[position].imageUrl)
      //  viewBinderHelper.bind(holder.swipeRevealLayout, parkingsList[position].name)
        holder.binding.root.main_layout_parking_item.setOnClickListener{
            listener.onItemClicked(parkingsList[position])
        }
        holder.binding.root.start_itin_btn.setOnClickListener {
            navigateToParking(parkingsList[position])
        }
    }

    fun navigateToParking(target:Parking) {
        val args = bundleOf("viewType" to MainActivity.MAP_VIEW, "actionType" to MapAction.NAVIGATION_ACTION,
            "data" to target)

        val activity = (listener as Fragment).activity!!
        val navController = Navigation.findNavController(activity,R.id.my_nav_host_fragment)
        navController.navigate(R.id.action_global_mainActivity2, args)
    }

}