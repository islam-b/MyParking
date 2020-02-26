package com.example.myparking.adapters

import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.example.myparking.R
import com.example.myparking.databinding.ReservationItemBinding
import com.example.myparking.models.Reservation
import com.example.myparking.repositories.ReservationListRepository
import com.example.myparking.utils.loadImage
import com.example.myparking.viewmodels.ReservationItemViewModel
import com.example.myparking.viewmodels.ReservationItemViewModelFactory
import kotlinx.android.synthetic.main.reservation_item.view.*

class ReservationAdapter(var listReservation : ArrayList<Reservation>,
                         val listener : ItemAdapterListener<Reservation>) :
    MyAdapter<Reservation, ReservationItemBinding>(listReservation, R.layout.reservation_item, listener) {
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       /* val factory = ReservationItemViewModelFactory(position, 1, ReservationListRepository.getInstance())
        val mReservationViewModel = ViewModelProviders.of(, factory).get(ReservationItemViewModel::class.java)*/
        holder.binding.reservation = listReservation[position]
        loadImage(holder.binding.qrImage, listReservation[position].qrUrl)
        holder.binding.root.info_reseravtion_card.setOnClickListener{
            listener.onItemClicked(listReservation[position])
        }
    }
}