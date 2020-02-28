package com.example.myparking.adapters

import com.example.myparking.databinding.ServiceItemBinding
import com.example.myparking.R
import com.example.myparking.models.Horaire
import com.example.myparking.models.Service
import kotlinx.android.synthetic.main.service_item.view.*

class ServiceAdapter(val servicesList : ArrayList<Service>,
                     val listener : ItemAdapterListener<Service>) :
    MyAdapter<Service, ServiceItemBinding>(servicesList, R.layout.service_item,listener)  {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.service = servicesList[position]
        holder.binding.checked = servicesList[position].checked
        holder.binding.root.service_icon.setImageResource(servicesList[position].icon)
        holder.binding.root.setOnClickListener{
            val b = holder.binding.checked
             holder.binding.checked = !(b!!)
            servicesList[position].checked = !(b!!)
            listener.onItemClicked(servicesList[position])
        }

    }
}