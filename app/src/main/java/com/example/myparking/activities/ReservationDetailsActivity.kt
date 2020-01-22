package com.example.myparking.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.myparking.R
import com.example.myparking.databinding.ActivityReservationDetailsBinding
import com.example.myparking.models.Reservation
import kotlinx.android.synthetic.main.activity_reservation_details.*

class ReservationDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReservationDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reservation_details)
        binding.reservation = Reservation("DZ - 12458647", "Lun, déc 16 12.40", " Lun, déc 16 13.40")


    }
}
