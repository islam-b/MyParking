package com.example.myparking.activities

import com.example.myparking.databinding.ActivityParkingDetailsBinding
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myparking.utils.DataSource
import com.example.myparking.R
import com.example.myparking.adapters.*

import com.example.myparking.models.*
import com.example.myparking.utils.loadBackground
import kotlinx.android.synthetic.main.activity_parking_details.*
import kotlinx.android.synthetic.main.parking_content_scrolling.view.*

class ParkingDetailsActivity : AppCompatActivity() {


    private var currentParking: Parking? = null
    private lateinit var binding: ActivityParkingDetailsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking_details)
        val bundle = intent.extras
        bundle?.let {
            bundle.apply {
                currentParking = getParcelable<Parking>("PARKING") as Parking

            }
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_parking_details)
        binding.parking = currentParking
        loadBackground(image_info_parking,currentParking?.image)

        showHoraireList()
        showTarifsList()
        showPaiementList()
        showEquipementList()

        //reserve btn click listener
        reserver.setOnClickListener {
            val i = ReservationActivity.newIntent(this)
            startActivity(i)
            finish()
        }


    }

    private fun showHoraireList() {
        var recyclerview = binding.root.horaire_list
        recyclerview.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapter = HoraireAdapter(DataSource.getHoraire(currentParking), object : MyAdapter.ItemAdapterListener<Horaire>{
            override fun onItemClicked(item: Horaire) {
                Log.d("Horaire clicked", item.horaire_days)
            }

        })
        recyclerview.adapter = adapter
    }
    private fun showTarifsList() {
        var recyclerview = binding.root.tarifs_list
        recyclerview.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapter =
            TarifsAdapter(DataSource.getTarifs(currentParking), object : MyAdapter.ItemAdapterListener<Tarif> {
                override fun onItemClicked(item: Tarif) {
                    Log.d("tarifs clicked", item.tarif_sum)
                }

            })
        recyclerview.adapter = adapter
    }
    private fun showPaiementList() {
        val recyclerview = binding.root.paiement_list
        recyclerview.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapter =
            PaiementAdapter(DataSource.getPaiement(currentParking), object : MyAdapter.ItemAdapterListener<Paiement>{
                override fun onItemClicked(item: Paiement) {
                    Log.d("Paiement clicked", item.type)
                }

            })
        recyclerview.adapter = adapter
    }
    private fun showEquipementList() {
        val recyclerview = binding.root.equipement_list
        recyclerview.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapter = EquipementAdapter(
            DataSource.getEquipement(currentParking),
            object : MyAdapter.ItemAdapterListener<Equipement>{
                override fun onItemClicked(item: Equipement) {
                    Log.d("Equipement clicked", item.type)
                }

            }
        )
        recyclerview.adapter = adapter

    }



    companion object {
        fun newIntent(context: Context, list:ArrayList<Parking>, position: Int): Intent {
            val intent = Intent(context, ParkingDetailsActivity::class.java)

            val parking = list[position]

            intent.putExtra("PARKING", parking)

            return intent
        }
    }
}
