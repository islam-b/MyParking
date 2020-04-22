package com.example.myparking.adapters

import android.view.View.*
import android.widget.ProgressBar
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
import com.example.myparking.fragements.ParkingItemListener
import com.example.myparking.fragements.ParkingsMap
import com.example.myparking.utils.MapAction
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ParkingsListAdapter(var parkingsList : ArrayList<Parking>,
                       val listener: ParkingItemListener
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
            listener.navigateToParking(parkingsList[position])
        }
        holder.binding.root.add_fav_btn.setOnClickListener {
            holder.binding.root.fav_progress.visibility = VISIBLE
            holder.binding.root.add_fav_btn.visibility = INVISIBLE
            listener.addToFavorites(parkingsList[position]) {
                holder.binding.root.fav_progress.visibility = GONE
                holder.binding.root.add_fav_btn.visibility = VISIBLE
            }
        }
    }
}