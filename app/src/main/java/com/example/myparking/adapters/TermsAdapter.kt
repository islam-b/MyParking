package com.example.myparking.adapters

import com.example.myparking.R
import com.example.myparking.databinding.ParkingDetailsItemType3Binding
import com.example.myparking.models.Terme

class TermsAdapter (val termsList : ArrayList<Terme>,
                    val listener : ItemAdapterListener<Terme>) :
    MyAdapter<Terme, ParkingDetailsItemType3Binding>(termsList, R.layout.parking_details_item_type3, listener) {
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.terme= termsList[position]
        holder.binding.root.setOnClickListener{
            listener.onItemClicked(termsList[position])
        }
    }
}