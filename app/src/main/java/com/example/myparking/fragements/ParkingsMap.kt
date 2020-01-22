package com.example.myparking.fragements

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myparking.R
import com.example.myparking.adapters.MyAdapter
import com.example.myparking.adapters.ParkingCarouselAdapter
import com.example.myparking.databinding.FragmentParkingsMapBinding
import com.example.myparking.databinding.ParkingCarouselItemBinding

import com.example.myparking.models.Parking
import com.example.myparking.models.ParkingModel
import com.example.myparking.utils.DataSource
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.MapView
import com.example.myparking.utils.MapsUtils
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import kotlinx.android.synthetic.main.fragment_parkings_map.view.*


private const val ARG_PARAM1 = "param1"

class ParkingsMap : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    OnLocationListener {


    private lateinit var carousel: DiscreteScrollView
    private lateinit var infiniteAdapter: InfiniteScrollAdapter<MyAdapter<ParkingModel, ParkingCarouselItemBinding>.MyViewHolder>

    private lateinit var mMap: GoogleMap
    private lateinit var mMapView: MapView
    private var parkings: ArrayList<ParkingModel> = arrayListOf()
    private var markers: ArrayList<Marker> = arrayListOf()

    private lateinit var binding: FragmentParkingsMapBinding


    fun requestLocation() {
        val listener = this
        MapsUtils.getLastLocation(context!!, object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                listener.onLocationReady(p0?.lastLocation!!)
            }
        }, this)
    }

    override fun onMapReady(p0: GoogleMap) {
        Log.d("htest", "hhh here")
        mMap = p0
        this.requestLocation()
        mMap.setOnMarkerClickListener(this)
        mMap.setMinZoomPreference(10.0f)
        mMap.setMaxZoomPreference(20.0f)
        var r = 0
        parkings.forEach {
            val pin = mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(it.lat, it.long))
                    .title(it.name)
                    .icon(
                        BitmapDescriptorFactory.fromBitmap(
                            MapsUtils.createCustomMarker(
                                context!!, mMapView,
                                R.color.colorPrimary, "10%"
                            )
                        )
                    )
            )
            pin.tag = it
            markers.add(pin)
            r++
        }

    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        Log.d("dialod not ", "not")
        //val parking = p0?.tag as ParkingModel
//        ParkingBottomSheet.newInstance(parking).show(activity?.supportFragmentManager,"ActionBottomDialog")
        val target = parkings.indexOf(p0?.tag as ParkingModel)
        carousel.smoothScrollToPosition(infiniteAdapter.getClosestPosition(target))
        return false
    }


    override fun onLocationReady(location: Location) {
        Log.d("here", "mylocation")
        mMap.isMyLocationEnabled = true
        val myLocation = LatLng(location.latitude, location.longitude)
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
        Log.d("PERMIH", "PERM")
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
        parkings.addAll(
            listOf(
                ParkingModel(
                    "ChIJwa_rIPhRjhIRWG5EvXi9l6s",
                    "Parking de carrfour Alger",
                    36.7289608,
                    3.1794445
                ),
                ParkingModel(
                    "ChIJn365GQ1SjhIRusvV1OXvFJQ",
                    "Parking Safex Public",
                    36.7315206,
                    3.1593492
                ),
                ParkingModel(
                    "ChIJZzTFn3VSjhIR-G7-af43cBU",
                    "Parking ABC Tower",
                    36.7390456,
                    3.1553383
                ),
                ParkingModel(
                    "ChIJicmhtXZSjhIR2rBuhVj-B2c",
                    "Parking Safex Public 2",
                    36.7351639,
                    3.1516975
                )
            )
        )


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_parkings_map, container, false)
        //val rootView = inflater.inflate(R.layout.fragment_parkings_map, container, false)
        val rootView = binding.root
//        binding =
//            DataBindingUtil.inflate(inflater, R.layout.fragment_parkings_map, container, false)
        //val rootView = binding.root
        mMapView = rootView.findViewById<MapView>(R.id.mapV)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        carousel = binding.root.carousel

        carousel.setOffscreenItems(1)
        carousel.setSlideOnFling(true)
        val listener = object : MyAdapter.ItemAdapterListener<ParkingModel> {
            override fun onItemClicked(item: ParkingModel) {
                Log.d("Parking Model clicked", item.name)
            }

        }
        infiniteAdapter = InfiniteScrollAdapter.wrap(
            ParkingCarouselAdapter(parkings, listener)
        )
        carousel.adapter = infiniteAdapter
        carousel.setItemTransformer(
            ScaleTransformer.Builder()
                .setMaxScale(1f)
                .setMinScale(0.95f)
                .setPivotX(Pivot.X.CENTER)
                .setPivotY(Pivot.Y.BOTTOM)
                .build()
        )

        carousel.addOnItemChangedListener { p0, p1 ->
            val realPos = infiniteAdapter.getRealPosition(p1)
            Log.d("position", realPos.toString())
            val marker = markers[realPos]

            mMap.animateCamera(
                CameraUpdateFactory.newLatLng(marker.position),
                500,
                object : GoogleMap.CancelableCallback {
                    override fun onFinish() {
                        marker.showInfoWindow()
                    }

                    override fun onCancel() {

                    }

                })
        }
    }


}

interface OnLocationListener {
    fun onLocationReady(location: Location)
}
