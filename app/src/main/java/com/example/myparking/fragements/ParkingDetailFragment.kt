package com.example.myparking.fragements

import com.example.myparking.databinding.ActivityParkingDetailsBinding
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.myparking.R
import com.example.myparking.activities.ReservationActivity
import com.example.myparking.adapters.*
import com.example.myparking.models.*

import com.example.myparking.utils.DataSource
import com.example.myparking.utils.loadBackground
import com.google.android.material.appbar.AppBarLayout
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_parking_details.*
import kotlinx.android.synthetic.main.activity_parking_details.view.*
import kotlinx.android.synthetic.main.parking_content_scrolling.*
import kotlinx.android.synthetic.main.parking_content_scrolling.view.*


class ParkingDetailFragment : Fragment() {


    private lateinit var binding: ActivityParkingDetailsBinding
    private var currentParking: Parking? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currentParking = it.getParcelable("PARKING")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding =
            DataBindingUtil.inflate(inflater, R.layout.activity_parking_details, container, false)
        binding.parking = currentParking
        val toolbar = binding.root.toolbar

        toolbar.inflateMenu(R.menu.parking_details_menu)
        toolbar.setNavigationIcon(R.drawable.ic_back_toolbar)
        toolbar.setNavigationOnClickListener { activity!!.onBackPressed() }
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_share -> {
                    Log.d("CLICK", "Share BTN CLICKED")
                    true
                }
            }
            false
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadBackground(image_info_parking, currentParking?.image)

        showHoraireList()
        showTarifsList()
        showPaiementList()
        showEquipementList()

        //reserve btn click listener
        reserver.setOnClickListener {
            val i = ReservationActivity.newIntent(context!!,currentParking!!)
            startActivity(i)
        }
    }


    private fun showHoraireList() {
        var recyclerview = binding.root.horaire_list
        recyclerview.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        val adapter = HoraireAdapter(
            DataSource.getHoraire(currentParking),
            object : MyAdapter.ItemAdapterListener<Horaire> {
                override fun onItemClicked(item: Horaire) {
                    Log.d("Horaire clicked", item.horaire_days)
                }

            })
        recyclerview.adapter = adapter
    }

    private fun showTarifsList() {
        var recyclerview = binding.root.tarifs_list
        recyclerview.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        val adapter =
            TarifsAdapter(
                DataSource.getTarifs(currentParking),
                object : MyAdapter.ItemAdapterListener<Tarif> {
                    override fun onItemClicked(item: Tarif) {
                        Log.d("tarifs clicked", item.tarif_sum)
                    }

                })
        recyclerview.adapter = adapter
    }

    private fun showPaiementList() {
        val recyclerview = binding.root.paiement_list
        recyclerview.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        val adapter =
            PaiementAdapter(
                DataSource.getPaiement(currentParking),
                object : MyAdapter.ItemAdapterListener<Paiement> {
                    override fun onItemClicked(item: Paiement) {
                        Log.d("Paiement clicked", item.type)
                    }

                })
        recyclerview.adapter = adapter
    }

    private fun showEquipementList() {
        val recyclerview = binding.root.equipement_list
        recyclerview.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        val adapter = EquipementAdapter(
            DataSource.getEquipement(currentParking),
            object : MyAdapter.ItemAdapterListener<Equipement> {
                override fun onItemClicked(item: Equipement) {
                    Log.d("Equipement clicked", item.type)
                }

            }
        )
        recyclerview.adapter = adapter

    }


    companion object {
        @JvmStatic
        fun newInstance(parking: Parking) =
            ParkingDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("PARKING", parking)
                }
            }
    }
}
