package com.example.myparking.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myparking.R
import com.example.myparking.adapters.ListAdapter
import com.example.myparking.models.Duration
import com.example.myparking.models.Horaire
import com.example.myparking.models.Tarif
import com.example.myparking.utils.DataSource
import kotlinx.android.synthetic.main.activity_reservation.*

class ReservationActivity : AppCompatActivity(), ListAdapter.OnItemClickListener {


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
        val adapter =
            ListAdapter(list, this)
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
        val adapter = ListAdapter(list, this)
        recyclerView.adapter = adapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun OnItemClick(item: Any) {
    }

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, ReservationActivity::class.java)
            return intent
        }
    }


}
