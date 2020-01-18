package com.example.myparking.adapters

import com.example.myparking.databinding.ParkingDetailsItemType1Binding
import com.example.myparking.R
import com.example.myparking.models.Tarif

class TarifsAdapter(val tarifsList : ArrayList<Tarif>,
                     val listener : ItemAdapterListener<Tarif>) :
    MyAdapter<Tarif, ParkingDetailsItemType1Binding>(tarifsList, R.layout.parking_details_item_type1, listener) {
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.tarif = tarifsList[position]
        holder.binding.type = TARIF_TYPE
        holder.binding.root.setOnClickListener{
            listener.onItemClicked(tarifsList[position])
        }
    }

    companion object {
        const val TARIF_TYPE = 1
    }

}