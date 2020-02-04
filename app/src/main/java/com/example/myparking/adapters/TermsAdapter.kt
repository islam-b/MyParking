package com.example.myparking.adapters

import com.example.myparking.R
import com.example.myparking.databinding.ParkingDetailsItemType3Binding

class TermsAdapter (val termsList : ArrayList<String>,
                    val listener : ItemAdapterListener<String>) :
    MyAdapter<String, ParkingDetailsItemType3Binding>(termsList, R.layout.parking_details_item_type3, listener) {
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.term= termsList[position]
        holder.binding.root.setOnClickListener{
            listener.onItemClicked(termsList[position])
        }
    }
}