package com.example.myparking.adapters

import android.view.View.GONE
import android.view.View.VISIBLE
import com.example.myparking.R
import com.example.myparking.activities.FavortieParkingListener
import com.example.myparking.databinding.FavoriteParkingItemBinding
import com.example.myparking.models.Parking
import kotlinx.android.synthetic.main.favorite_parking_item.view.*


class FavoriteParkingAdapter(val parkingList:ArrayList<Parking>, val listener: FavortieParkingListener) :
    MyAdapter<Parking, FavoriteParkingItemBinding>(parkingList,
        R.layout.favorite_parking_item,listener) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.parking = parkingList[position]
        holder.binding.root.parking_img.setImageResource(R.drawable.parking_p)
        holder.binding.root.setOnClickListener {
            listener.onItemClicked(parkingList[position])
        }
        holder.binding.root.delete_fav_btn.setOnClickListener {
            holder.binding.root.delete_fev_progress.visibility = VISIBLE
            holder.binding.root.delete_fav_btn.visibility = GONE
            listener.removeFromFavorite(parkingList[position]) {
                holder.binding.root.delete_fev_progress.visibility = GONE
                holder.binding.root.delete_fav_btn.visibility = VISIBLE
            }
        }
    }

}