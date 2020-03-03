package com.example.myparking.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.myparking.R
import com.example.myparking.databinding.ActivityReservationDetailsBinding
import com.example.myparking.models.Reservation
import com.example.myparking.utils.loadImage
import kotlinx.android.synthetic.main.activity_reservation_details.*
import kotlinx.android.synthetic.main.activity_reservation_details.view.*
import kotlinx.android.synthetic.main.qr_code_zoomed.view.*

class ReservationDetailsActivity : Fragment() {

    private lateinit var binding: ActivityReservationDetailsBinding
    private lateinit var reservation: Reservation


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = DataBindingUtil.inflate(inflater, R.layout.activity_reservation_details, container, false)
        binding.lifecycleOwner = activity
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.zoom_btn.setOnClickListener {
            val mDialogZoom = LayoutInflater.from(context!!).inflate(R.layout.qr_code_zoomed, null)

            val mBuilder = AlertDialog.Builder(context!!)
                .setView(mDialogZoom)
            val mAlertDialog = mBuilder.create()
            mAlertDialog.window?.attributes?.windowAnimations = R.style.DialogAnimation_Zoom_In
            mAlertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            mAlertDialog.show()
            loadImage(mDialogZoom.qrImageBig, reservation.qrUrl)
        }
        binding.reservation = reservation
        loadImage(binding.root.qrImage, reservation.qrUrl)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        reservation = arguments?.getParcelable<Reservation>("reservation") as Reservation

    }

}
