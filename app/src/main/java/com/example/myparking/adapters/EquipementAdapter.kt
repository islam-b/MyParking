package com.example.myparking.adapters

import android.util.Log
import com.example.myparking.R

import com.example.myparking.models.Equipement
import com.example.myparking.utils.loadImage
import kotlinx.android.synthetic.main.parking_details_item_type2.view.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myparking.databinding.ParkingDetailsItemType2Binding



class EquipementAdapter(val equipementList : ArrayList<Equipement>,
                      val listener : ItemAdapterListener<Equipement>) :
    MyAdapter<Equipement, ParkingDetailsItemType2Binding>(equipementList, R.layout.parking_details_item_type2, listener) {
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.equipement =equipementList[position]
        holder.binding.type = EQUIPEMENT_TYPE
        Log.d("iconurl", equipementList[position].iconUrl)
        loadImage(holder.binding.root.right_icon, equipementList[position].iconUrl)
       // holder.binding.root.right_icon.setImageResource(equipementList[position].icon)
        holder.binding.root.setOnClickListener{
            listener.onItemClicked(equipementList[position])
        }
    }

    companion object {
        const val EQUIPEMENT_TYPE = 1
    }

}