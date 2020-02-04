package com.example.myparking.models

import android.os.Parcelable
import android.widget.ImageView
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.Glide
import androidx.databinding.BindingAdapter



@Parcelize
data class Parking(val opened: String = "",val  capacity: String ="",  val name: String ="" ,val address: String ="",
                   val distance: String ="",val walk_time: String ="", val image: String ="", val horaire :  @RawValue ArrayList<Horaire>? = null,
                   val tarifs :  @RawValue ArrayList<Tarif>? = null,
                   val paiements: ArrayList<Int>? = null,
                   val equipements: ArrayList<Int>? = null,
                   val termes : ArrayList<String>? = null)
    : Parcelable {



}