package com.example.myparking.adapters

import com.example.myparking.R
import com.example.myparking.databinding.FavoriteParkingItemBinding
import com.example.myparking.models.Parking
import kotlinx.android.synthetic.main.favorite_parking_item.view.*


class FavoriteParkingAdapter(val parkingList:ArrayList<Parking>, val listener:ItemAdapterListener<Parking>) :
    MyAdapter<Parking, FavoriteParkingItemBinding>(parkingList,
        R.layout.favorite_parking_item,listener) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.parking = parkingList[position]
        holder.binding.root.parking_img.setImageResource(R.drawable.parking_p)
        holder.binding.root.setOnClickListener {
            listener.onItemClicked(parkingList[position])
        }
    }

}