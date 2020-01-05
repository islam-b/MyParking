package com.example.myparking

import android.view.View
import android.widget.TextView
import com.example.myparking.adapters.ListAdapter
import com.example.myparking.models.Tarif

class TarifViewHolder(itemView: View, listener: ListAdapter.OnItemClickListener): ViewHolder(itemView) {
    override fun onClick(p0: View?) {
        mListener.OnItemClick(mtarif as Any)
    }
    init {
        itemView.setOnClickListener(this)
    }
    val  mListener = listener
    var  mtarif : Tarif? = null

    val tarif_time = itemView.findViewById<TextView>(R.id.horaire_days)
    val tarif_sum = itemView.findViewById<TextView>(R.id.horaire_time)
}