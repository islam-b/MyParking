package com.example.myparking.fragements

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.myparking.R
import com.example.myparking.activities.ReservationActivity
import com.example.myparking.adapters.ListAdapter
import com.example.myparking.models.Parking
import com.example.myparking.utils.DataSource
import com.google.android.material.appbar.AppBarLayout
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.parking_content_scrolling.*


class ParkindDetailFragment : Fragment(), ListAdapter.OnItemClickListener {

    override fun OnItemClick(item: Any) {

    }


    private var currentParking : Parking? = null
    private var listener: OnFragmentInteractionListener? = null

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
        val v = inflater.inflate(R.layout.activity_parking_details, container, false)
        initView(v)
        return v
    }

    fun initView(view:View) {
        //horaire recycleview
        var recyclerview = view.findViewById<RecyclerView>(R.id.horaire_list)
        recyclerview.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val adapter = ListAdapter(DataSource.getHoraire(currentParking), this)
        recyclerview.adapter = adapter



        //general info and image
        view.findViewById<TextView>(R.id.name).text = currentParking?.name
        view.findViewById<TextView>(R.id.parking_address).text = currentParking?.address
        view.findViewById<TextView>(R.id.opened).text = currentParking?.opened
        view.findViewById<TextView>(R.id.distance).text = currentParking?.distance
        view.findViewById<TextView>(R.id.capacity).text = currentParking?.capacity
        view.findViewById<TextView>(R.id.walk_time).text = currentParking?.walk_time

        val temp = ImageView(context!!)
        Picasso.get().load(currentParking?.image).into(temp,object: Callback {
            override fun onSuccess() {
                view.findViewById<AppBarLayout>(R.id.image_info_parking).background = temp.drawable
            }
            override fun onError(e: Exception?) {
            }
        })

        //tarifs recycle view

        var recyclerviewTarifs = view.findViewById<RecyclerView>(R.id.tarifs_list)
        recyclerviewTarifs.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val adapterTarifs =
            ListAdapter(DataSource.getTarifs(currentParking), this)
        recyclerviewTarifs.adapter = adapterTarifs

        //paiment recycle view

        var recyclerviewPaiment = view.findViewById<RecyclerView>(R.id.paiement_list)
        recyclerviewPaiment.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val adapterPaiement =
            ListAdapter(DataSource.getPaiement(currentParking), this)
        recyclerviewPaiment.adapter = adapterPaiement

        //equipement recycle view

        var recyclerviewEquip = view.findViewById<RecyclerView>(R.id.equipement_list)
        recyclerviewEquip.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val adapterEquip = ListAdapter(
            DataSource.getEquipement(currentParking),
            this
        )
        recyclerviewEquip.adapter = adapterEquip

        //reserve btn click listener
        view.findViewById<Button>(R.id.reserver).setOnClickListener {
            val i = ReservationActivity.newIntent(context!!)
            startActivity(i)
        }
    }


    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is OnFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
//        }
//    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(parking: Parking) =
            ParkindDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("PARKING", parking)
                }
            }
    }
}
