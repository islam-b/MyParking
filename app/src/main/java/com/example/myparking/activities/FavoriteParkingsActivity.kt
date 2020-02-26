package com.example.myparking.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myparking.R
import com.example.myparking.adapters.FavoriteParkingAdapter
import com.example.myparking.adapters.MyAdapter
import com.example.myparking.databinding.ActivityFavoriteParkingsBinding
import com.example.myparking.models.Parking
import com.example.myparking.utils.DataSource
import kotlinx.android.synthetic.debug.activity_favorite_parkings.*
import kotlinx.android.synthetic.debug.activity_favorite_parkings.view.*

class FavoriteParkingsActivity : AppCompatActivity(), MyAdapter.ItemAdapterListener<Parking> {

private lateinit var binding : ActivityFavoriteParkingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_parkings)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorite_parkings)
        initFavoriteParkingsList()
    }

    fun initFavoriteParkingsList() {
        val recyclerView = binding.root.fav_parking_list
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        recyclerView.adapter = FavoriteParkingAdapter(DataSource.getParkings(),this)
    }

    override fun onItemClicked(item: Parking) {
        Log.d("parking clicked", "cc")
        val l =DataSource.getParkings()
        val i = l.indexOf(item)
        startActivity(ParkingsDetailsContainer.newIntent(this,l,item.idParking))
        finish()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context,FavoriteParkingsActivity::class.java)
            return intent
        }
    }
}
