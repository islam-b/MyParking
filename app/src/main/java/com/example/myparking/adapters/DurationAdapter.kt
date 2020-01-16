package com.example.myparking.adapters



import com.example.myparking.databinding.ReservationDurationLayoutBinding
import com.example.myparking.R
import com.example.myparking.models.Duration
import com.example.myparking.models.Equipement
import kotlinx.android.synthetic.main.reservation_duration_layout.view.*

class DurationAdapter(val durationList:ArrayList<Duration>, val listener:ItemAdapterListener<Duration>) :
    MyAdapter<Duration, ReservationDurationLayoutBinding>(durationList,
        R.layout.reservation_duration_layout,listener) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.duration = durationList[position]
        holder.binding.root.dure_icon.setImageResource(durationList[position].icon)
        holder.binding.root.setOnClickListener {
            listener.onItemClicked(durationList[position])
        }
    }


}