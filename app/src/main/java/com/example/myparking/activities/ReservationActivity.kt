package com.example.myparking.activities

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.widget.TimePicker

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer

import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myparking.R
import com.example.myparking.adapters.DurationAdapter
import com.example.myparking.adapters.MyAdapter
import com.example.myparking.adapters.TarifsAdapter
import com.example.myparking.databinding.ActivityReservationBinding
import com.example.myparking.models.*
import com.example.myparking.repositories.ReservationListRepository
import com.example.myparking.services.ReservationRequest

import com.example.myparking.utils.DataSource
import kotlinx.android.synthetic.main.activity_reservation.*

import java.util.*

import kotlinx.android.synthetic.main.reservation_dialog.view.*
import java.text.SimpleDateFormat


class ReservationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReservationBinding
    private lateinit var currentParking: Parking
    private lateinit var dateEntree: String
    private lateinit var dateSortie: String
    private lateinit var mDialogSuccuess : View
    val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.FRANCE)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)

        val toolbar: Toolbar = findViewById(R.id.toolbar1)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = "Réserver une place"

        binding = DataBindingUtil.setContentView(this, R.layout.activity_reservation)
        val bundle = intent.extras
        bundle?.let {
            bundle.apply {
                val parking = getParcelable<Parking>("PARKING") as Parking
                binding.parking = parking
                currentParking = parking
            }
        }

        //initTarifsLists()
        initDurationsList()
        confirm_btn.setOnClickListener {
            // create Reservation
            val date = Calendar.getInstance().time
            Log.d("dateentree", dateEntree)
            Log.d("datesortie", dateSortie)
            val reservationRequest = ReservationRequest(
                dateEntree,
                dateSortie,
                currentParking.idParking.toString(),
                "1",
                "1",
                PaiementInstance("89", "5846", df.format(date))
            )
            ReservationListRepository.getInstance().getCreatedReservation(reservationRequest)
                .observe(this, Observer<Reservation> {res ->
                    val mBuilder = AlertDialog.Builder(this)
                        .setView(mDialogSuccuess)

                    val mAlertDialog = mBuilder.show()
                    mAlertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    mDialogSuccuess.see_details_btn.setOnClickListener {

                        val reservationDetailsActivity =
                            ReservationDetailsActivity.newIntent(this, res!!)
                        startActivity(reservationDetailsActivity)
                        finish()

                    }

                })
            mDialogSuccuess = LayoutInflater.from(this).inflate(R.layout.reservation_dialog, null)
          /*  val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogSuccuess)

            val mAlertDialog = mBuilder.show()
            mAlertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent) */
          /*  mDialogView.see_details_btn.setOnClickListener {

                // real data of reseravtion later
                // val reservation = Reservation("DZ - 12458647", "Lun, déc 16 12.40", " Lun, déc 16 13.40", currentParking)
                val reservations = ReservationListRepository.getInstance().getReservations()
                val reservation =
                    reservations.find { res -> res.parking.idParking == currentParking.idParking }
                val reservationDetailsActivity =
                    ReservationDetailsActivity.newIntent(this, reservation!!)
                startActivity(reservationDetailsActivity)
                finish()

            }*/

        }
    }


    fun initDurationsList() {

        val c = Calendar.getInstance()
        var hourIN = c.get(Calendar.HOUR_OF_DAY)
        var minuteIN = c.get(Calendar.MINUTE)
        dateEntree = df.format(c.time)
        c.add(Calendar.HOUR_OF_DAY, 2)
        var hourOUT = c.get(Calendar.HOUR_OF_DAY)
        var minuteOUT = c.get(Calendar.MINUTE)
        dateSortie = df.format(c.time)
        c.add(Calendar.HOUR_OF_DAY, -2)

        val IN = Duration(
            "Entrée",
            String.format("%02d:%02d", hourIN, minuteIN),
            R.drawable.ic_hourglass_full,
            "IN",
            true
        )
        val OUT = Duration(
            "Sortie",
            String.format("%02d:%02d", hourOUT, minuteOUT),
            R.drawable.ic_hourglass_empty,
            "OUT",
            true
        )
        val TOTAL = Duration("Temps total", "2 heures", R.drawable.ic_timer, "TIME", false)

        val recyclerView = duration_container
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val list = arrayListOf(
            IN,
            OUT,
            TOTAL
        )
        val context = this


        var adapter = DurationAdapter(list, object : MyAdapter.ItemAdapterListener<Duration> {
            override fun onItemClicked(item: Duration) {
            }

        })
        val listener = object : MyAdapter.ItemAdapterListener<Duration> {

            override fun onItemClicked(item: Duration) {
                val c = Calendar.getInstance().time

                when (item.TAG) {
                    "IN" -> {
                        TimePickerDialog(
                            context,
                            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                                hourIN = hourOfDay
                                minuteIN = minute
                                IN.text2 = String.format("%02d:%02d", hourOfDay, minute)
                                updateTotal()
                                adapter.notifyDataSetChanged()
                                c.hours = hourOfDay
                                c.minutes = minute
                                dateEntree = df.format(c)
                            }, hourIN, minuteIN, true
                        ).show()
                    }
                    "OUT" -> {
                        TimePickerDialog(
                            context,
                            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                                hourOUT = hourOfDay
                                minuteOUT = minute
                                OUT.text2 = String.format("%02d:%02d", hourOfDay, minute)
                                updateTotal()
                                adapter.notifyDataSetChanged()
                                c.hours = hourOfDay
                                c.minutes = minute
                                dateSortie = df.format(c)
                            }, hourOUT, minuteOUT, true
                        ).show()
                    }
                }
            }

            fun updateTotal() {
                val c1 = Calendar.getInstance()
                val c2 = Calendar.getInstance()
                c1.set(Calendar.HOUR_OF_DAY, hourIN)
                c1.set(Calendar.MINUTE, minuteIN)
                c2.set(Calendar.HOUR_OF_DAY, hourOUT)
                c2.set(Calendar.MINUTE, minuteOUT)
                val diff = c2.time.time - c1.time.time
                val seconds = diff / 1000
                val minutes = seconds / 60
                val hours = minutes / 60
                TOTAL.text2 = String.format("%02d heures", hours)
            }
        }
        adapter = DurationAdapter(list, listener)
        recyclerView.adapter = adapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


    companion object {
        fun newIntent(context: Context, parking: Parking): Intent {
            val intent = Intent(context, ReservationActivity::class.java)
            intent.putExtra("PARKING", parking)
            return intent
        }
    }


}
