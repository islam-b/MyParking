package com.example.myparking

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.example.myparking.adapters.ListAdapter
import com.example.myparking.models.Parking

class ParkingViewHolder(itemView: View, listener: ListAdapter.OnItemClickListener): ViewHolder(itemView) {
    override fun onClick(p0: View?) {
        mListener.OnItemClick(mparking as Any)
    }
    init {
        itemView.setOnClickListener(this)
    }
    val  mListener = listener
    var  mparking : Parking? = null
    val name= itemView.findViewById<TextView>(R.id.name)
    val address= itemView.findViewById<TextView>(R.id.parking_address)
    val capacity= itemView.findViewById<TextView>(R.id.capacity)
    val opened= itemView.findViewById<TextView>(R.id.opened)
    val walk_time= itemView.findViewById<TextView>(R.id.walk_time)
    val distance=itemView.findViewById<TextView>(R.id.distance)
    val image = itemView.findViewById<ImageButton>(R.id.image)
}