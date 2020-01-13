package com.example.myparking.adapters

import com.example.myparking.R
import com.example.myparking.databinding.ParkingDetailsItemType2Binding
import com.example.myparking.models.Equipement
import kotlinx.android.synthetic.main.parking_details_item_type2.view.*

class EquipementAdapter(val equipementList : ArrayList<Equipement>,
                      val listener : ItemAdapterListener<Equipement>) :
    MyAdapter<Equipement, ParkingDetailsItemType2Binding>(equipementList, R.layout.parking_details_item_type2, listener) {
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.equipement =equipementList[position]
        holder.binding.type = EQUIPEMENT_TYPE
        holder.binding.root.right_icon.setImageResource(equipementList[position].icon)
        holder.binding.root.setOnClickListener{
            listener.onItemClicked(equipementList[position])
        }
    }

    companion object {
        const val EQUIPEMENT_TYPE = 1
    }

}