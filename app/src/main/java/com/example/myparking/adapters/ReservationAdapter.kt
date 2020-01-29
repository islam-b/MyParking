package com.example.myparking.adapters

import com.example.myparking.R
import com.example.myparking.databinding.ReservationItemBinding
import com.example.myparking.models.Reservation
import kotlinx.android.synthetic.main.reservation_item.view.*

class ReservationAdapter(val listReservation : ArrayList<Reservation>,
                         val listener : ItemAdapterListener<Reservation>) :
    MyAdapter<Reservation, ReservationItemBinding>(listReservation, R.layout.reservation_item, listener) {
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.reservation = listReservation[position]
        holder.binding.root.my_reservation_fav.setOnClickListener{
            listener.onItemClicked(listReservation[position])
        }
    }
}