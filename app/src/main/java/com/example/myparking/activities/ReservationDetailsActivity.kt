package com.example.myparking.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.example.myparking.R
import com.example.myparking.databinding.ActivityReservationDetailsBinding
import com.example.myparking.models.Reservation
import kotlinx.android.synthetic.main.activity_reservation_details.*

class ReservationDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReservationDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_details)
        val toolbar = findViewById<Toolbar>(R.id.toolbar2)
        setSupportActionBar(toolbar)
       // supportActionBar?.title = "Details de réservation"
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reservation_details)
        val bundle = intent.extras
        bundle?.let {
            bundle.apply {
                binding.reservation = getParcelable<Reservation>("RESERVATION") as Reservation
            }
        }
    }

    companion object {
        fun newIntent(context: Context, reservation: Reservation): Intent {
            val intent = Intent(context, ReservationDetailsActivity::class.java)
            intent.putExtra("RESERVATION", reservation)
            return intent
        }
    }
}
