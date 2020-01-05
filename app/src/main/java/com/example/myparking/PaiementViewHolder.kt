package com.example.myparking

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.myparking.adapters.ListAdapter
import com.example.myparking.models.Paiement


class PaiementViewHolder(itemView: View, listener: ListAdapter.OnItemClickListener): ViewHolder(itemView) {
    override fun onClick(p0: View?) {
        mListener.OnItemClick(mpaiement as Any)
    }
    init {
        itemView.setOnClickListener(this)
    }
    val  mListener = listener
    var  mpaiement : Paiement? = null

    val left_text = itemView.findViewById<TextView>(R.id.left_text)
    val right_icon = itemView.findViewById<ImageView>(R.id.right_icon)
}