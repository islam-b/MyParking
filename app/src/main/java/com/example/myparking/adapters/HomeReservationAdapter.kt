package com.example.myparking.adapters

import com.example.myparking.R
import com.example.myparking.databinding.HomeReservationItemBinding
import com.example.myparking.models.Parking
import com.example.myparking.models.Reservation
import kotlinx.android.synthetic.main.home_proposed_parking_item.view.*
import kotlinx.android.synthetic.main.home_reservation_item.view.*

class HomeReservationAdapter(val reservationList : ArrayList<Reservation>, val listener: ItemAdapterListener<Reservation>
): MyAdapter<Reservation, HomeReservationItemBinding>(reservationList, R.layout.home_reservation_item, listener)
{

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.reservation = reservationList[position]
        holder.binding.root.home_reservation.setOnClickListener{
            listener.onItemClicked(reservationList[position])
        }
    }
}