package com.example.myparking.activities

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TimePicker
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
import java.util.*

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

        val c = Calendar.getInstance()
        var hourIN = c.get(Calendar.HOUR_OF_DAY)
        var minuteIN = c.get(Calendar.MINUTE)
        c.add(Calendar.HOUR_OF_DAY,2)
        var hourOUT = c.get(Calendar.HOUR_OF_DAY)
        var minuteOUT = c.get(Calendar.MINUTE)
        c.add(Calendar.HOUR_OF_DAY,-2)

        val IN = Duration("Entrée",String.format("%02d:%02d",hourIN,minuteIN), R.drawable.ic_hourglass_full,"IN",true)
        val OUT = Duration("Sortie",String.format("%02d:%02d",hourOUT,minuteOUT), R.drawable.ic_hourglass_empty, "OUT",true)
        val TOTAL = Duration("Temps total","2 heures", R.drawable.ic_timer, "TIME",false)

        val recyclerView = duration_container
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val list = arrayListOf(
            IN,
            OUT,
            TOTAL
        )
        val context = this


        var adapter = DurationAdapter(list,object: MyAdapter.ItemAdapterListener<Duration> {
            override fun onItemClicked(item: Duration) {
            }

        })
        val listener = object: MyAdapter.ItemAdapterListener<Duration> {

            override fun onItemClicked(item: Duration) {
                when (item.TAG) {
                    "IN"->{
                        TimePickerDialog(context,
                            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                                hourIN = hourOfDay
                                minuteIN = minute
                                IN.text2 = String.format("%02d:%02d",hourOfDay,minute)
                                updateTotal()
                                adapter.notifyDataSetChanged()
                            },hourIN,minuteIN,true).show()
                    }
                    "OUT"->{
                        TimePickerDialog(context,
                            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                                hourOUT = hourOfDay
                                minuteOUT = minute
                                OUT.text2 = String.format("%02d:%02d",hourOfDay,minute)
                                updateTotal()
                                adapter.notifyDataSetChanged()
                            },hourOUT,minuteOUT,true).show()
                    }
                }
            }
            fun updateTotal() {
                val c1 = Calendar.getInstance()
                val c2 = Calendar.getInstance()
                c1.set(Calendar.HOUR_OF_DAY,hourIN)
                c1.set(Calendar.MINUTE,minuteIN)
                c2.set(Calendar.HOUR_OF_DAY,hourOUT)
                c2.set(Calendar.MINUTE,minuteOUT)
                val diff = c2.time.time - c1.time.time
                val  seconds = diff / 1000
                val minutes = seconds / 60
                val hours = minutes / 60
                TOTAL.text2 = String.format("%02d heures",hours)
            }
        }
        adapter = DurationAdapter(list, listener )
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
