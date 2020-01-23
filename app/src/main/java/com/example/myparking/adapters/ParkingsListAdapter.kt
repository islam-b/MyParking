package com.example.myparking.adapters

import com.example.myparking.databinding.ParkingItemBinding
import com.example.myparking.R
import com.example.myparking.models.Parking
import com.example.myparking.utils.loadImage
import kotlinx.android.synthetic.main.parking_item.view.*
import com.chauthai.swipereveallayout.ViewBinderHelper



class ParkingsListAdapter(val parkingsList : ArrayList<Parking>,
                       val listener: ItemAdapterListener<Parking>
                       ): MyAdapter<Parking, ParkingItemBinding>(parkingsList, R.layout.parking_item, listener)
{
    private val viewBinderHelper = ViewBinderHelper()
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.parking = parkingsList[position]
        loadImage(holder.binding.root.image, parkingsList[position].image)
      //  viewBinderHelper.bind(holder.swipeRevealLayout, parkingsList[position].name)
        holder.binding.root.main_layout_parking_item.setOnClickListener{
            listener.onItemClicked(parkingsList[position])
        }
    }
}