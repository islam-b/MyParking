package com.example.myparking.adapters

import com.example.myparking.databinding.ServiceItemBinding
import com.example.myparking.R
import com.example.myparking.models.Equipement
import com.example.myparking.models.Horaire
import com.example.myparking.models.Service
import com.example.myparking.utils.loadImage
import kotlinx.android.synthetic.main.service_item.view.*

class ServiceAdapter(var servicesList : ArrayList<Equipement>,
                     val listener : ItemAdapterListener<Equipement>) :
    MyAdapter<Equipement, ServiceItemBinding>(servicesList, R.layout.service_item,listener)  {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.service = servicesList[position]
        holder.binding.checked = servicesList[position].checked
        //holder.binding.root.service_icon.setImageResource(servicesList[position].icon)
        loadImage(holder.binding.root.service_icon, servicesList[position].iconUrl)
        holder.binding.root.setOnClickListener{
            val b = holder.binding.checked
             holder.binding.checked = !(b!!)
            servicesList[position].checked = !(b!!)
            listener.onItemClicked(servicesList[position])
        }

    }
}