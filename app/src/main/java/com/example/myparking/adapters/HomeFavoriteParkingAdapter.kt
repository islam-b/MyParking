package com.example.myparking.adapters


import android.util.Log
import com.example.myparking.R
import com.example.myparking.databinding.HomeFavoriteParkingItemBinding
import com.example.myparking.databinding.HomeProposedParkingItemBinding
import com.example.myparking.models.Parking
import kotlinx.android.synthetic.main.home_favorite_parking_item.view.*


class HomeFavoriteParkingAdapter(val parkingsList : ArrayList<Parking>, val listener: ItemAdapterListener<Parking>
): MyAdapter<Parking, HomeFavoriteParkingItemBinding>(parkingsList, R.layout.home_favorite_parking_item, listener)
{

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.binding.parking = parkingsList[position]
        holder.binding.root.home_fav_parking.setOnClickListener{
            listener.onItemClicked(parkingsList[position])
        }
    }
}