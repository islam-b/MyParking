package com.example.myparking.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myparking.utils.DataSource
import com.example.myparking.R
import com.example.myparking.adapters.ListAdapter
import com.example.myparking.models.Parking
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_parking_details.*
import kotlinx.android.synthetic.main.parking_content_scrolling.*

class ParkingDetailsActivity : AppCompatActivity() , ListAdapter.OnItemClickListener{

//    private var currentParking = Parking()
    private var currentParking : Parking? = null

    override fun OnItemClick(item: Any) {
      Log.d("Horaire", item as String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras
        bundle?.let {
            bundle.apply {
                currentParking = getParcelable<Parking>("PARKING") as Parking
            }
        }
        setContentView(R.layout.activity_parking_details)

        //horaire recycleview
        var recyclerview = horaire_list
        recyclerview.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapter =
            ListAdapter(DataSource.getHoraire(currentParking), this)
        recyclerview.adapter = adapter



        //general info and image
        name.text = currentParking?.name
        parking_address.text = currentParking?.address
        opened.text = currentParking?.opened
        distance.text = currentParking?.distance
        capacity.text = currentParking?.capacity
        walk_time.text = currentParking?.walk_time

        val temp = ImageView(this)
        Picasso.get().load(currentParking?.image).into(temp,object: Callback {
            override fun onSuccess() {
                image_info_parking.background = temp.drawable
            }
            override fun onError(e: Exception?) {
            }
        })

        //tarifs recycle view

        var recyclerviewTarifs = tarifs_list
        recyclerviewTarifs.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapterTarifs =
            ListAdapter(DataSource.getTarifs(currentParking), this)
        recyclerviewTarifs.adapter = adapterTarifs

        //paiment recycle view

        var recyclerviewPaiment = paiement_list
        recyclerviewPaiment.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapterPaiement =
            ListAdapter(DataSource.getPaiement(currentParking), this)
        recyclerviewPaiment.adapter = adapterPaiement

        //equipement recycle view

        var recyclerviewEquip = equipement_list
        recyclerviewEquip.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapterEquip = ListAdapter(
            DataSource.getEquipement(currentParking),
            this
        )
        recyclerviewEquip.adapter = adapterEquip
    }

    companion object {
        fun newIntent(context: Context, parking: Parking): Intent {
            val intent = Intent(context, ParkingDetailsActivity::class.java)
            Log.d("Parking details", parking.name)
            intent.putExtra("PARKING", parking)
            //intent.putExtra("NAME", parking.name)
            //intent.putExtra("NEWS", news)
            //intent.putExtra("TITLE",news.description)
            return intent
        }
    }
}
