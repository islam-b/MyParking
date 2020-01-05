package com.example.myparking

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.MapView
import com.example.myparking.Utils.MapsUtils
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions




private const val ARG_PARAM1 = "param1"

class ParkingsMap : Fragment() , OnMapReadyCallback, GoogleMap.OnMarkerClickListener, OnLocationListener {


    private lateinit var mMap: GoogleMap
    private lateinit var mMapView: MapView
    private var parkings:ArrayList<ParkingModel> = arrayListOf()



    fun requestLocation() {
        val listener = this
        MapsUtils.getLastLocation(context!!, object:LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                listener.onLocationReady(p0?.lastLocation!!)
            }
        },this)
    }

    override fun onMapReady(p0: GoogleMap) {
        Log.d("htest","hhh here")
        mMap = p0
        this.requestLocation()
        mMap.setOnMarkerClickListener(this)
        mMap.setMinZoomPreference(10.0f)
        mMap.setMaxZoomPreference(20.0f)
        var r=0
        parkings.forEach {
            val pin=mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(it.lat,it.long))
                    .title(it.name)
                    .icon(BitmapDescriptorFactory.fromBitmap(
                        MapsUtils.createCustomMarker(context!!,mMapView, R.color.colorPrimary,"10%")
                    )))
            pin.tag = it
            r++
        }

    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        Log.d("dialod not ", "not")
        val parking = p0?.tag as ParkingModel
        ParkingBottomSheet.newInstance(parking).show(activity?.supportFragmentManager,"ActionBottomDialog")
        return false
    }


    override fun onLocationReady(location: Location) {
        Log.d("here","mylocation")
        mMap.isMyLocationEnabled = true
        val myLocation = LatLng(location.latitude,location.longitude)
        //mMap.addMarker(MarkerOptions().position(myLocation).title("Marker in my location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(13.0f))
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("PERMIH","PERM")
        if (requestCode == MapsUtils.PERMISSION_ID) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.requestLocation()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        this.requestLocation()

    }



    private var listener: OnFragmentInteractionListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
//        }
//        {'id_parking': 'ChIJwa_rIPhRjhIRWG5EvXi9l6s', 'name': 'Parking de carrfour Alger', 'location': [36.7289608, 3.1794445], 'places': 13},
//        {'id_parking': 'ChIJn365GQ1SjhIRusvV1OXvFJQ', 'name': 'Parking Safex Public', 'location': [36.7315206, 3.1593492], 'places': 15},
//        {'id_parking': 'ChIJO2kzwh1SjhIRoyVikwECJYQ', 'name': 'Parking communal 100 places', 'location': [36.7245058, 3.1765598], 'places': 15},
//        {'id_parking': 'ChIJfWcYsfZRjhIRBvNTVJdF5xg', 'name': 'Parking Daïra', 'location': [36.7243167, 3.1825472], 'places': 11},
//        {'id_parking': 'ChIJZzTFn3VSjhIR-G7-af43cBU', 'name': 'Parking ABC Tower', 'location': [36.7390456, 3.1553383], 'places': 14},
//        {'id_parking': 'ChIJicmhtXZSjhIR2rBuhVj-B2c', 'name': 'Parking Safex Public 2', 'location': [36.7351639, 3.1516975], 'places': 11},
//        {'id_parking': 'ChIJQRomCyBSjhIRPLupkg99hiM', 'name': 'Parking El Djorf', 'location': [36.7168406, 3.1773357], 'places': 20},
//        {'id_parking': 'ChIJ50cQ1ntTjhIR9XHYmB52be8', 'name': 'ERMA', 'location': [36.7150498, 3.1753268], 'places': 16},
//        {'id_parking': 'ChIJo1-bR81RjhIRnSAl4fxq4xQ', 'name': 'Parking Marche', 'location': [36.7169083, 3.1875255], 'places': 13},
//        {'id_parking': 'ChIJMYWLDY1RjhIRqzl9svJjJ2I', 'name': 'Parking au visiteurs', 'location': [36.7158669, 3.1865293], 'places': 18}
//        ]
        parkings.addAll(listOf(
            ParkingModel("ChIJwa_rIPhRjhIRWG5EvXi9l6s", "Parking de carrfour Alger", 36.7289608, 3.1794445),
            ParkingModel("ChIJn365GQ1SjhIRusvV1OXvFJQ", "Parking Safex Public", 36.7315206, 3.1593492),
            ParkingModel("ChIJZzTFn3VSjhIR-G7-af43cBU", "Parking ABC Tower", 36.7390456, 3.1553383),
            ParkingModel("ChIJicmhtXZSjhIR2rBuhVj-B2c", "Parking Safex Public 2", 36.7351639, 3.1516975)
        ))




    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_parkings_map, container, false)

        mMapView = rootView.findViewById(R.id.mapV) as MapView
 //       val options = GoogleMapOptions()
//        options.mapType(GoogleMap.MAP_TYPE_NORMAL)
//            .compassEnabled(false)
//            .rotateGesturesEnabled(false)
//            .tiltGesturesEnabled(false)


        mMapView.onCreate(savedInstanceState)

        mMapView.onResume()

        try {
            MapsInitializer.initialize(activity!!.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mMapView.getMapAsync(this)


        return rootView
    }



    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        if (context is OnFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
//        }
    }

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
        fun newInstance() = ParkingsMap().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
interface OnLocationListener {
    fun onLocationReady(location: Location)
}
