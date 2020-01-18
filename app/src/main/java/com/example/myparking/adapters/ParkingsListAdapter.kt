package com.example.myparking.adapters

import com.example.myparking.databinding.ParkingItemBinding
import com.example.myparking.R
import com.example.myparking.models.Parking
import com.example.myparking.utils.loadImage
import kotlinx.android.synthetic.main.parking_item.view.*

class ParksListAdapter(val parkingsList : ArrayList<Parking>,
                       val listener: ItemAdapterListener<Parking>
                       ): MyAdapter<Parking, ParkingItemBinding>(parkingsList, R.layout.parking_item, listener)
{
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.parking = parkingsList[position]
        loadImage(holder.binding.root.image, parkingsList[position].image)
        holder.binding.root.setOnClickListener{
            listener.onItemClicked(parkingsList[position])
        }
    }
}