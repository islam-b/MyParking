package com.example.myparking.adapters

import com.example.myparking.databinding.ParkingDetailsItemType2Binding
import com.example.myparking.R
import com.example.myparking.models.Paiement
import com.example.myparking.utils.loadImage
import kotlinx.android.synthetic.main.parking_details_item_type2.view.*


class PaiementAdapter(var paiementList : ArrayList<Paiement>,
                     val listener : ItemAdapterListener<Paiement>) :
    MyAdapter<Paiement, ParkingDetailsItemType2Binding>(paiementList, R.layout.parking_details_item_type2, listener) {
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.paiement = paiementList[position]
        holder.binding.type = PAIEMENT_TYPE
        loadImage(holder.binding.root.right_icon, paiementList[position].iconUrl)
        holder.binding.root.setOnClickListener{
            listener.onItemClicked(paiementList[position])
        }
    }

    companion object {
        const val PAIEMENT_TYPE = 0
    }

}