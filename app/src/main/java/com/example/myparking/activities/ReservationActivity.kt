package com.example.myparking.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myparking.R
import com.example.myparking.adapters.DurationAdapter
import com.example.myparking.adapters.MyAdapter
import com.example.myparking.adapters.TarifsAdapter
import com.example.myparking.models.Duration
import com.example.myparking.models.Horaire
import com.example.myparking.models.Tarif
import com.example.myparking.utils.DataSource
import kotlinx.android.synthetic.main.activity_reservation.*
import kotlinx.android.synthetic.main.reservation_dialog.view.*

class ReservationActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        initTarifsLists()
        initDurationsList()
        confirm_btn.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.reservation_dialog, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)

            val mAlertDialog = mBuilder.show()
            mAlertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            mDialogView.see_details_btn.setOnClickListener{
                val reservationDetailsActivity = Intent(applicationContext, ReservationDetailsActivity::class.java)
                startActivity(reservationDetailsActivity)
                finish()

            }

        }
    }

    fun initTarifsLists() {
        val recyclerView = tarifs_container
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val list = arrayListOf(
            Tarif("1 heure","100 DZD"),
            Tarif("2 heure","200 DZD"),
            Tarif("3 heure","300 DZD")
        )
        val adapter = TarifsAdapter(list, object : MyAdapter.ItemAdapterListener<Tarif> {
            override fun onItemClicked(item: Tarif) {
                Log.d("tarifs clicked", item.tarif_sum)
            }

        })
        recyclerView.adapter = adapter
    }

    fun initDurationsList() {
        val recyclerView = duration_container
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val list = arrayListOf(
            Duration("Temps total","2 heure", R.drawable.ic_timer),
            Duration("Entrée","12:30", R.drawable.ic_hourglass_full),
            Duration("Sortie","14:30", R.drawable.ic_hourglass_empty)
        )
        val adapter = DurationAdapter(list, object: MyAdapter.ItemAdapterListener<Duration> {
            override fun onItemClicked(item: Duration) {
                Log.d("Duration clicked",item.text1)
            }
        })
        recyclerView.adapter = adapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }



    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, ReservationActivity::class.java)
            return intent
        }
    }


}
