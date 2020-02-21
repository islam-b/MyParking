package com.example.myparking.adapters

import com.example.myparking.R
import com.example.myparking.databinding.ParkingCarouselItemBinding
import com.example.myparking.models.Parking

import com.example.myparking.models.ParkingModel

class ParkingCarouselAdapter(val parkingList:ArrayList<Parking>,
                             val listener:ItemAdapterListener<Parking>) :
    MyAdapter<Parking, ParkingCarouselItemBinding>(parkingList,
        R.layout.parking_carousel_item,listener) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.parking = parkingList[position]
        //holder.binding.root.parking_img.setImageResource(R.drawable.parking_p)
        holder.binding.root.setOnClickListener {
            listener.onItemClicked(parkingList[position])
        }
    }


}