package com.example.myparking.utils

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myparking.R
import com.example.myparking.models.NotificationModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList








class NotificationsListAdapter(val notifications: ArrayList<NotificationModel>, val listener:OnNotifClickListener): RecyclerView.Adapter<NotificationsListAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notification_item, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = notifications[position]
        holder.notif = notification
        holder.title.text = notification.title
        holder.body.text = notification.body
        holder.date.text = calculateDuration(notification.date)
        if (notification.read=="true") {
            holder.indicator.visibility= GONE
            holder.title.typeface = Typeface.DEFAULT
        } else {
            holder.indicator.visibility= VISIBLE
            holder.title.typeface = Typeface.DEFAULT_BOLD
        }
    }

    fun updateList(newList: ArrayList<NotificationModel>) {
        notifications.clear()
        notifications.addAll(newList)
        notifications.sort()
        notifyDataSetChanged()
    }

    private fun calculateDuration(date:String): String {
        val dateFormatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.FRANCE)
        try {
            val oldDate = dateFormatter.parse(date)
            val currentDate = Date()
            val diff = currentDate.time - oldDate!!.time
            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24
            if (days.toInt() > 0) {
                if (days.toInt() == 1) {
                    return "Il y a ${days.toInt()} jour"
                }
                if (days.toInt() > 1) {
                    return "Il y a ${days.toInt()} jours"
                }
            } else if (hours.toInt() > 0) {
                if (hours.toInt() == 1) {
                    return "Il y a ${hours.toInt()} heure"
                }
                if (hours.toInt() > 1) {
                    return "Il y a ${hours.toInt()} heures"
                }
            } else if (minutes.toInt() > 0) {
                if (minutes.toInt() == 1) {
                    return "Il y a ${minutes.toInt()} minute"
                }
                if (minutes.toInt() > 1) {
                    return "Il y a ${minutes.toInt()} minutes"
                }
            } else if (seconds.toInt() > 0) {
                return "A l'instant"
            } else {
                val dateformat2 = SimpleDateFormat("dd-MM-yyyy", Locale.FRANCE)
                return dateformat2.format(oldDate)
            }
        } catch  (e:Exception) {
            val dateformat2 = SimpleDateFormat("dd-MM-yyyy", Locale.FRANCE)
            return dateformat2.format(dateFormatter.parse(date)!!)
        }
        return ""
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {

        lateinit var notif: NotificationModel
        init {
            view.setOnClickListener(this)
            view.findViewById<CardView>(R.id.delete_notif).setOnClickListener {
                listener.onDelete(notif)
            }
        }
        val title = view.findViewById<TextView>(R.id.notif_title)
        val body = view.findViewById<TextView>(R.id.notif_body)
        val date = view.findViewById<TextView>(R.id.notif_date)
        val indicator = view.findViewById<ImageView>(R.id.notif_indic)

        override fun onClick(v: View?) {
            if(notif.read=="false") listener.onRead(notif)
        }

    }

    interface OnNotifClickListener {
        fun onDelete(notif:NotificationModel)
        fun onRead(notif:NotificationModel)
    }
}