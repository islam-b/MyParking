package com.example.myparking.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myparking.R
import com.example.myparking.adapters.FavoriteParkingAdapter
import com.example.myparking.adapters.MyAdapter
import com.example.myparking.models.Parking
import com.example.myparking.utils.DataSource
import kotlinx.android.synthetic.debug.activity_favorite_parkings.*

class FavoriteParkingsActivity : AppCompatActivity(), MyAdapter.ItemAdapterListener<Parking> {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_parkings)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        initFavoriteParkingsList()
    }

    fun initFavoriteParkingsList() {
        fav_parking_list.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        fav_parking_list.adapter = FavoriteParkingAdapter(DataSource.getParkings(),this)
    }

    override fun onItemClicked(item: Parking) {
        Log.d("parking clicked", "cc")
        val l =DataSource.getParkings()
        val i = l.indexOf(item)
        startActivity(ParkingsDetailsContainer.newIntent(this,l,i))
        finish()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context,FavoriteParkingsActivity::class.java)
            return intent
        }
    }
}
