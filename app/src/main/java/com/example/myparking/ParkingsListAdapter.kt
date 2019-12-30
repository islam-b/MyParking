package com.example.myparking

import android.util.Log
import com.squareup.picasso.Picasso
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myparking.models.Parking

class ParkingsListAdapter(val parkings: ArrayList<Parking>, listener : OnParkingClickListener)
    : RecyclerView.Adapter<ParkingsListAdapter.ViewHolder>() {

    private val mListener=listener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val vi = LayoutInflater.from(parent.context).inflate(R.layout.parking_item, parent, false)
        Log.d("PARKING",parkings[viewType].name)
        return ViewHolder(vi,parkings[viewType],mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val parking = parkings[position]
       if (parking.image!= ""){ Picasso.get().load(parking.image).fit().centerInside().into(holder.image)}
        holder.name.text = parking.name
        holder.capacity.text = parking.capacity
        holder.walk_time.text = parking.walk_time
        holder.address.text = parking.address
        holder.distance.text = parking.distance
        holder.opened.text = parking.opened

        holder.mparking = parking
    }

    override fun getItemCount(): Int {
        return parkings.size
    }

    inner class ViewHolder(itemView: View, parking:Parking ,listener: OnParkingClickListener): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {
            mListener.OnParkingClick(mparking)
        }
        init {
            itemView.setOnClickListener(this)
        }
        val  mListener = listener
        var mparking=parking
        val name= itemView.findViewById<TextView>(R.id.name)
        val address= itemView.findViewById<TextView>(R.id.parking_address)
        val capacity= itemView.findViewById<TextView>(R.id.capacity)
        val opened= itemView.findViewById<TextView>(R.id.opened)
        val walk_time= itemView.findViewById<TextView>(R.id.walk_time)
        val distance=itemView.findViewById<TextView>(R.id.distance)
        val image = itemView.findViewById<ImageButton>(R.id.image)
    }
    interface OnParkingClickListener{
        fun OnParkingClick(parking: Parking)
    }


}