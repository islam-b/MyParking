package com.example.myparking.adapters
import android.view.View
import com.example.myparking.R
import com.example.myparking.databinding.HomeProposedParkingItemBinding
import com.example.myparking.models.Parking
import kotlinx.android.synthetic.main.home_proposed_parking_item.view.*


class HomeParkingAdapter(val parkingsList : ArrayList<Parking>, val listener: ItemAdapterListener<Parking>
): MyAdapter<Parking, HomeProposedParkingItemBinding>(parkingsList, R.layout.home_proposed_parking_item, listener)
{

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.parking = parkingsList[position]
        if (position!=0) holder.binding.root.closest_badge.visibility = View.INVISIBLE
        holder.binding.root.home_parking.setOnClickListener{
            listener.onItemClicked(parkingsList[position])
        }
    }
}