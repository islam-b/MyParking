package com.example.myparking.fragements

import com.example.myparking.databinding.ActivityParkingDetailsBinding
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myparking.MainActivity.Companion.MAP_VIEW

import com.example.myparking.R
import com.example.myparking.activities.ReservationActivity
import com.example.myparking.adapters.*
import com.example.myparking.models.*
import com.example.myparking.repositories.FavoriteParkingRepository
import com.example.myparking.repositories.ParkingListRepository
import com.example.myparking.utils.MapAction
import com.example.myparking.utils.PreferenceManager

import com.example.myparking.utils.loadBackground
import com.example.myparking.viewmodels.ParkingItemViewModel
import com.example.myparking.viewmodels.ParkingItemViewModelFactory
import com.example.myparking.viewmodels.ParkingListViewModel
import com.example.myparking.viewmodels.ParkingListViewModelFactory
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_parking_details.*
import kotlinx.android.synthetic.main.activity_parking_details.view.*
import kotlinx.android.synthetic.main.parking_content_scrolling.*
import kotlinx.android.synthetic.main.parking_content_scrolling.view.*


class ParkingDetailFragment : Fragment() {


    private lateinit var binding: ActivityParkingDetailsBinding
    private var currentParkingIndex: Int =0
    private lateinit var parkingsList: ArrayList<Parking>

    private lateinit var mParkingViewModel : ParkingItemViewModel
    private var mAdapterTarif: TarifsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            currentParkingIndex = it.getInt("parkingIndex")
            parkingsList = it.getParcelableArrayList<Parking>("parkingsList") as ArrayList<Parking>
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.activity_parking_details, container, false)
       /* binding.parking = currentParking*/
        binding.lifecycleOwner = activity
        val factory = ParkingItemViewModelFactory(currentParkingIndex, parkingsList)

        mParkingViewModel = ViewModelProviders.of(this, factory)
            .get(ParkingItemViewModel::class.java)

        mParkingViewModel.getParkingItem().observe(viewLifecycleOwner, Observer<Parking>
        {
            mAdapterTarif?.updateList(it.tarifs)

        })
        binding.parking = mParkingViewModel
        val toolbar = binding.root.toolbar

        toolbar.inflateMenu(R.menu.parking_details_menu)

        toolbar.setNavigationIcon(R.drawable.ic_back_toolbar)
        toolbar.setNavigationOnClickListener {
            val navController = Navigation.findNavController(activity!!,R.id.my_nav_host_fragment)
            navController.navigateUp()
            //activity!!.onBackPressed()
        }
        val idDriver = PreferenceManager(context!!).checkDriverProfile().toInt()
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_share -> {
                    Log.d("CLICK", "Share BTN CLICKED")
                    val parking = parkingsList[currentParkingIndex]
                    val uri = "https://www.google.com/maps/?q=${parking.lattitude},${parking.longitude}"
                    val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
                    sharingIntent.type = "text/plain"
                    val ShareSub = parking.nom
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, ShareSub)
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, uri)
                    startActivity(Intent.createChooser(sharingIntent, "Partager avec..."))
                    true
                }
                R.id.action_fav -> {
                    showFavLoading()
                   FavoriteParkingRepository.getInstance().addToFavorites(idDriver,
                       parkingsList[currentParkingIndex].idParking,null,null).observe(
                       this@ParkingDetailFragment, Observer<String> {
                       if (it !== null && it != "") {
                           hideFavLoading()
                           val snackbar = Snackbar
                               .make(binding.root, it, Snackbar.LENGTH_LONG)
                           snackbar.show()
                       }
                   })
                    true
                }

            }
            false
        }
        binding.root.navigate_btn.setOnClickListener {
            val args = bundleOf("viewType" to MAP_VIEW, "actionType" to MapAction.NAVIGATION_ACTION,
                "data" to mParkingViewModel.getParking())

            val navController = Navigation.findNavController(activity!!,R.id.my_nav_host_fragment)
            navController.navigate(R.id.action_global_mainActivity2, args)

        }
        return binding.root
    }


    private fun showFavLoading() {
        val item = toolbar.menu.findItem(R.id.action_fav)
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val abprogress = inflater.inflate(R.layout.loading_spinner, null)
        item.actionView = abprogress
    }

    private fun hideFavLoading() {
        val item = toolbar.menu.findItem(R.id.action_fav)
        item.actionView = null
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
            goToReservation(mParkingViewModel.getParkingItem().value!!)
        }
    }

    fun goToReservation(parking:Parking) {
        val navController = Navigation.findNavController(activity!!,R.id.my_nav_host_fragment)
        val bundle = bundleOf("parking" to parking)
        navController.navigate(R.id.action_parkingsDetailsContainer_to_reservationActivity,bundle)
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
        fun newInstance(parkings:ArrayList<Parking>,index: Int) =
            ParkingDetailFragment().apply {
                arguments = Bundle().apply {
                    /*putParcelable("PARKING", parking)*/
                    putInt("parkingIndex", index)
                    putParcelableArrayList("parkingsList",parkings)

                }
            }
    }
}
