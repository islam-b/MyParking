package com.example.myparking.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.example.myparking.R
import com.example.myparking.databinding.ActivityReservationDetailsBinding
import com.example.myparking.models.Reservation
import com.example.myparking.utils.loadImage
import kotlinx.android.synthetic.main.activity_reservation_details.*
import kotlinx.android.synthetic.main.activity_reservation_details.view.*
import kotlinx.android.synthetic.main.qr_code_zoomed.view.*

class ReservationDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReservationDetailsBinding
    private lateinit var reservation: Reservation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_details)
        val toolbar = findViewById<Toolbar>(R.id.toolbar2)
        setSupportActionBar(toolbar)
       // supportActionBar?.title = "Details de réservation"
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reservation_details)

        binding.root.zoom_btn.setOnClickListener {
            val mDialogZoom = LayoutInflater.from(this).inflate(R.layout.qr_code_zoomed, null)

            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogZoom)
            val mAlertDialog = mBuilder.create()
            mAlertDialog.window?.attributes?.windowAnimations = R.style.DialogAnimation_Zoom_In
            mAlertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            mAlertDialog.show()
            loadImage(mDialogZoom.qrImageBig, reservation.qrUrl)
        }
        val bundle = intent.extras
        bundle?.let {
            bundle.apply {
                reservation = getParcelable<Reservation>("RESERVATION") as Reservation
                binding.reservation = reservation
                loadImage(binding.root.qrImage, reservation.qrUrl)
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
