//package com.example.myparking
//
//import android.content.Context
//import android.view.View
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.constraintlayout.widget.ConstraintLayout
//import androidx.core.content.ContextCompat
//import com.example.myparking.adapters.ListAdapter
//import com.example.myparking.models.Service
//import com.example.myparking.models.Tarif
//
//class ServiceViewHolder(itemView: View, listener: ListAdapter.OnItemClickListener): ViewHolder(itemView) {
//
//    override fun onClick(p0: View?) {
//        if (checked) {
//            val greyColor = ContextCompat.getColor(mContext!!,R.color.inactiveDot)
//            service_title.setTextColor(greyColor)
//            service_icon.setColorFilter(greyColor)
//            service_icon_container.background = ContextCompat.getDrawable(mContext!!,R.drawable.service_icon_container_bg)
//            checked = false
//        }else {
//            val primaryColor = ContextCompat.getColor(mContext!!,R.color.colorPrimary)
//            service_title.setTextColor(primaryColor)
//            service_icon.setColorFilter(primaryColor)
//            service_icon_container.background = ContextCompat.getDrawable(mContext!!,R.drawable.service_icon_container_bg_activ)
//            checked = true
//        }
//    }
//    init {
//        itemView.setOnClickListener(this)
//
//
//    }
//    fun initColors() {
//        mContext = service_icon.context
//        val greyColor = ContextCompat.getColor(mContext!!,R.color.inactiveDot)
//        service_title.setTextColor(greyColor)
//        service_icon.setColorFilter(greyColor)
//        service_icon_container.background = ContextCompat.getDrawable(mContext!! ,R.drawable.service_icon_container_bg)
//        checked = false
//    }
//    var checked = false
//    val  mListener = listener
//    var   mService : Service? = null
//    var mContext: Context? =null
//    val service_title = itemView.findViewById<TextView>(R.id.service_title)
//    val service_icon = itemView.findViewById<ImageView>(R.id.service_icon)
//    val service_icon_container = itemView.findViewById<ConstraintLayout>(R.id.service_icon_container)
//
//}