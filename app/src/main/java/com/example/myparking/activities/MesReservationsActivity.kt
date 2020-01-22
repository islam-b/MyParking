package com.example.myparking.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myparking.R
import com.example.myparking.adapters.MyAdapter
import com.example.myparking.adapters.ReservationAdapter
import com.example.myparking.databinding.ActivityMesReservationsBinding
import com.example.myparking.databinding.ActivityReservationDetailsBinding
import com.example.myparking.models.Reservation
import com.example.myparking.utils.DataSource
import kotlinx.android.synthetic.main.activity_mes_reservations.view.*

class MesReservationsActivity : AppCompatActivity(), MyAdapter.ItemAdapterListener<Reservation> {
    override fun onItemClicked(item: Reservation) {
       Log.d("Reservation clicked", item.code_res)
    }

    private lateinit var binding: ActivityMesReservationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mes_reservations)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mes_reservations)
        val recyclerview = binding.root.mes_reservations_list
        recyclerview.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapter = ReservationAdapter(DataSource.getReservations(), this)
        recyclerview.adapter = adapter
    }
}
