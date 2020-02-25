package com.example.myparking.adapters

import com.example.myparking.R
import com.example.myparking.databinding.HomeReservationItemBinding
import com.example.myparking.models.Parking
import com.example.myparking.models.Reservation
import com.example.myparking.utils.loadImage
import kotlinx.android.synthetic.main.home_proposed_parking_item.view.*
import kotlinx.android.synthetic.main.home_reservation_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeReservationAdapter(val reservationList : ArrayList<Reservation>, val listener: ItemAdapterListener<Reservation>
): MyAdapter<Reservation, HomeReservationItemBinding>(reservationList, R.layout.home_reservation_item, listener)
{

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.reservation = reservationList[position]
        loadImage(holder.binding.root.imageView6, reservationList[position].qrUrl)
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.FRANCE)
        val newFormatter = SimpleDateFormat("EEE, dd MMM, HH:mm", Locale.FRANCE)
        holder.binding.root.textView23.text = newFormatter.format(df.parse(reservationList[position].dateEntreeEffective.substring(0,19)!!)!!)
        holder.binding.root.home_reservation.setOnClickListener{
            listener.onItemClicked(reservationList[position])
        }
    }
}