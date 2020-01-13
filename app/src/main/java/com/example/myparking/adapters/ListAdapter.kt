package com.example.myparking.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myparking.*
import com.example.myparking.models.*
import com.squareup.picasso.Picasso

class ListAdapter<T>(val items : ArrayList<T>, listener : OnItemClickListener) : RecyclerView.Adapter<ViewHolder>() {

    private val mListener: OnItemClickListener =listener
    private val VIEW_TYPE_PARKING_LIST = 0
    private val VIEW_TYPE_HORAIRE_LIST = 1
    private val VIEW_TYPE_TARIFS_LIST = 2
    private val VIEW_TYPE_PAIEMENT_LIST = 3
    private val VIEW_TYPE_DURATION_LIST = 4

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_PARKING_LIST){
            val parking = items[position] as Parking
            val parkingholder =  (holder as ParkingViewHolder)
            if (parking.image!= ""){ Picasso.get().load(parking.image).fit().centerInside().into(parkingholder.image)}
            //val parkingholder = holder as
            parkingholder.name.text = parking.name
            parkingholder.capacity.text = parking.capacity
            parkingholder.walk_time.text = parking.walk_time
            parkingholder.address.text = parking.address
            parkingholder.distance.text = parking.distance
            parkingholder.opened.text = parking.opened
            parkingholder.mparking = parking
        } else if (getItemViewType(position) == VIEW_TYPE_HORAIRE_LIST) {
            val horaire = items[position] as Horaire
            val horaireholder = holder as HoraireViewHolder
            horaireholder.horaire_days.text = horaire.horaire_days
            horaireholder.horaire_time.text = horaire.horaire_time
        } else if (getItemViewType(position) == VIEW_TYPE_TARIFS_LIST){
            val tarif = items[position] as Tarif
            val tarifholder = holder as TarifViewHolder
            tarifholder.tarif_time.text = tarif.tarif_time
            tarifholder.tarif_sum.text = tarif.tarif_sum
        } else if ( getItemViewType(position) == VIEW_TYPE_PAIEMENT_LIST) {
            val paiement = items[position] as Paiement
            val paiementholder = holder as PaiementViewHolder
            // if (paiement.icon!= ""){ Picasso.get().load(paiement.icon).fit().centerInside().into(paiementholder.right_icon)}
            paiementholder.left_text.text = paiement.type
            paiementholder.right_icon.setImageResource(paiement.icon)
        } else if (getItemViewType(position) == VIEW_TYPE_DURATION_LIST) {
            val duration = items[position] as Duration
            val durationHolder = holder as DurationViewHolder
            durationHolder.duration_text1.text = duration.text1
            durationHolder.duration_text2.text = duration.text2
            durationHolder.icon.setImageResource(duration.icon)
            durationHolder.mDuration = duration
        } else { //equipement
            val equipement = items[position] as Equipement
            val equipementtholder = holder as EquipementViewHolder
            equipementtholder.left_text.text = equipement.type
            equipementtholder.right_icon.setImageResource(equipement.icon)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        if (items[position] is Parking) {
            return VIEW_TYPE_PARKING_LIST
        }else if (items[position] is Horaire) {
            return VIEW_TYPE_HORAIRE_LIST
        }else if (items[position] is Tarif) {
            return  VIEW_TYPE_TARIFS_LIST
        } else if (items[position] is Paiement) {
            return VIEW_TYPE_PAIEMENT_LIST
        } else if (items[position] is Duration) {
            return VIEW_TYPE_DURATION_LIST
        }
        return 100000
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if (viewType == VIEW_TYPE_PARKING_LIST) {
            val vi = LayoutInflater.from(parent.context).inflate(R.layout.parking_item, parent, false)
            Log.d("PARKING inside If",(items[viewType] as Parking).name)
            return ParkingViewHolder(vi, mListener)
        }else if (viewType == VIEW_TYPE_HORAIRE_LIST){
            val vi = LayoutInflater.from(parent.context).inflate(R.layout.horaire_item, parent, false)
            return HoraireViewHolder(vi, mListener)
        }else if (viewType == VIEW_TYPE_TARIFS_LIST) {
            val vi = LayoutInflater.from(parent.context).inflate(R.layout.horaire_item, parent, false)
            return TarifViewHolder(vi, mListener)
        }else if (viewType == VIEW_TYPE_PAIEMENT_LIST) {
            val vi = LayoutInflater.from(parent.context)
                .inflate(R.layout.parking_details_item_type2, parent, false)
            return PaiementViewHolder(vi, mListener)
        }else if (viewType == VIEW_TYPE_DURATION_LIST) {
            val vi = LayoutInflater.from(parent.context).inflate(R.layout.reservation_duration_layout, parent, false)
            return DurationViewHolder(vi, mListener)
        } else {
            val vi = LayoutInflater.from(parent.context).inflate(R.layout.parking_details_item_type2, parent, false)
            return EquipementViewHolder(vi, mListener)
        }
    }

    interface OnItemClickListener{
        fun OnItemClick(item: Any)
    }

}