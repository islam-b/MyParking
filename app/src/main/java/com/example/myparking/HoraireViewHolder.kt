package com.example.myparking

import android.view.View
import android.widget.TextView
import com.example.myparking.adapters.ListAdapter
import com.example.myparking.models.Horaire

class HoraireViewHolder(itemView: View, listener: ListAdapter.OnItemClickListener): ViewHolder(itemView) {
    override fun onClick(p0: View?) {
        mListener.OnItemClick(mHoraireItem as Any)
    }
    init {
        itemView.setOnClickListener(this)
    }
    val  mListener = listener
    var  mHoraireItem : Horaire? = null
    val horaire_days = itemView.findViewById<TextView>(R.id.horaire_days)
    val horaire_time = itemView.findViewById<TextView>(R.id.horaire_time)

}