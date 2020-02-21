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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.myparking.R
import com.example.myparking.activities.ReservationActivity
import com.example.myparking.adapters.*
import com.example.myparking.models.*
import com.example.myparking.repositories.ParkingListRepository

import com.example.myparking.utils.loadBackground
import com.example.myparking.viewmodels.ParkingItemViewModel
import com.example.myparking.viewmodels.ParkingItemViewModelFactory
import com.example.myparking.viewmodels.ParkingListViewModel
import com.example.myparking.viewmodels.ParkingListViewModelFactory
import com.google.android.material.appbar.AppBarLayout
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_parking_details.*
import kotlinx.android.synthetic.main.activity_parking_details.view.*
import kotlinx.android.synthetic.main.parking_content_scrolling.*
import kotlinx.android.synthetic.main.parking_content_scrolling.view.*


class ParkingDetailFragment : Fragment() {


    private lateinit var binding: ActivityParkingDetailsBinding
    private var currentParkingIndex: Int = 5

    private lateinit var mParkingViewModel : ParkingItemViewModel
    private var mAdapterTarif: TarifsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            currentParkingIndex = it.getInt("PARKINGINDEX")
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.activity_parking_details, container, false)
       /* binding.parking = currentParking*/
        val factory = ParkingItemViewModelFactory(currentParkingIndex, ParkingListRepository.getInstance())

        mParkingViewModel = ViewModelProviders.of(this, factory)
            .get(ParkingItemViewModel::class.java)

        mParkingViewModel.getParkingItem().observe(this, Observer<Parking>
        {
            mAdapterTarif?.updateList(it.tarifs)

        })
        binding.parking = mParkingViewModel
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
        loadBackground(image_info_parking, mParkingViewModel.getImageUrl())

        showHoraireList()
        showTarifsList()
        showPaiementList()
        showEquipementList()
        showTermsList()

        //reserve btn click listener
        reserver.setOnClickListener {
            val i = ReservationActivity.newIntent(context!!,mParkingViewModel.getParkingItem().value!!)
            startActivity(i)
        }
    }


    private fun showHoraireList() {
        var recyclerview = binding.root.horaire_list
        recyclerview.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        val adapter = HoraireAdapter(
            mParkingViewModel.getHoraires(),
            object : MyAdapter.ItemAdapterListener<Horaire> {
                override fun onItemClicked(item: Horaire) {
                    Log.d("Horaire clicked", item.Jours)
                }

            })
        recyclerview.adapter = adapter
    }

    private fun showTarifsList() {
        var recyclerview = binding.root.tarifs_list
        recyclerview.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        val adapter =
            TarifsAdapter(
               mParkingViewModel.getTarifs(),
                object : MyAdapter.ItemAdapterListener<Tarif> {
                    override fun onItemClicked(item: Tarif) {
                        Log.d("tarifs clicked", item.prix.toString())
                    }

                })
        recyclerview.adapter = adapter
    }

    private fun showPaiementList() {
        val recyclerview = binding.root.paiement_list
        recyclerview.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        val adapter =
            PaiementAdapter(
                mParkingViewModel.getPaiments(),
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
            mParkingViewModel.getEquipements(),
            object : MyAdapter.ItemAdapterListener<Equipement> {
                override fun onItemClicked(item: Equipement) {
                    Log.d("Equipement clicked", item.designation)
                }

            }
        )
        recyclerview.adapter = adapter

    }

    private fun showTermsList() {
        val recyclerview = binding.root.terms_list
        recyclerview.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        val adapter = TermsAdapter(
            mParkingViewModel.getTermes(),
            object : MyAdapter.ItemAdapterListener<Terme> {
                override fun onItemClicked(item: Terme) {
                    Log.d("Term clicked", item.contenu)
                }

            }
        )
        recyclerview.adapter = adapter

    }

    companion object {
        @JvmStatic
        fun newInstance(/*parking: Parking*/index: Int) =
            ParkingDetailFragment().apply {
                arguments = Bundle().apply {
                    /*putParcelable("PARKING", parking)*/
                    putInt("PARKINGINDEX", index)

                }
            }
    }
}
