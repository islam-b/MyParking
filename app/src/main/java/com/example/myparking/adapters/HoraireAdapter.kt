package com.example.myparking.adapters

import com.example.myparking.databinding.ParkingDetailsItemType1Binding
import com.example.myparking.R
import com.example.myparking.models.Horaire
import com.example.myparking.models.HoraireStatus


class HoraireAdapter(val horaireList : ArrayList<Horaire>,
                     val listener : ItemAdapterListener<Horaire>) :
        MyAdapter<Horaire, ParkingDetailsItemType1Binding>(horaireList, R.layout.parking_details_item_type1, listener) {
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.horaire = horaireList[position]
        holder.binding.type = HORAIRE_TYPE
        holder.binding.root.setOnClickListener{
            listener.onItemClicked(horaireList[position])
        }
    }

    companion object {
        const val HORAIRE_TYPE = 0
    }

}