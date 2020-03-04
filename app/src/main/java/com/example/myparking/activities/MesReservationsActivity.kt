package com.example.myparking.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myparking.R
import com.example.myparking.adapters.MyAdapter
import com.example.myparking.adapters.ReservationAdapter
import com.example.myparking.databinding.ActivityMesReservationsBinding
import com.example.myparking.databinding.ActivityReservationDetailsBinding
import com.example.myparking.models.Reservation
import com.example.myparking.repositories.ParkingListRepository
import com.example.myparking.repositories.ReservationListRepository
import com.example.myparking.utils.DataSource
import com.example.myparking.viewmodels.ParkingListViewModelFactory
import com.example.myparking.viewmodels.ReservationListViewModel
import com.example.myparking.viewmodels.ReservationListViewModelFactory
import kotlinx.android.synthetic.main.activity_mes_reservations.*
import kotlinx.android.synthetic.main.activity_mes_reservations.view.*

class MesReservationsActivity : Fragment(), MyAdapter.ItemAdapterListener<Reservation> {
    private var mAdapter: ReservationAdapter? = null
    private lateinit var mReservationListViewModel: ReservationListViewModel
    private lateinit var binding: ActivityMesReservationsBinding
    private var reservations =  ArrayList<Reservation> ()

    override fun onItemClicked(item: Reservation) {
       Log.d("Reservation clicked", item.idReservation.toString())
        goToReservationDetails(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_mes_reservations, container, false)
        binding.lifecycleOwner = activity
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = ReservationListViewModelFactory(1, ReservationListRepository.getInstance())
        mReservationListViewModel = ViewModelProviders.of(this, factory)
            .get(ReservationListViewModel::class.java)
        progress_bar.visibility = View.VISIBLE
        mReservationListViewModel.getReservationsList().observe(this, Observer<ArrayList<Reservation>>
        {
            // custom dates to refactor later
            mAdapter?.updateList(it)
            progress_bar.visibility = View.GONE
        })

        val recyclerview = binding.root.mes_reservations_list
        recyclerview.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        mAdapter = ReservationAdapter(reservations, this)
        recyclerview.adapter = mAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun goToReservationDetails( reservation: Reservation) {
        val navController = Navigation.findNavController(binding.root)
        val bundle = bundleOf("reservation" to reservation)
        navController.navigate(R.id.action_mesReservationsActivity_to_reservationDetailsActivity,bundle)
    }
}
