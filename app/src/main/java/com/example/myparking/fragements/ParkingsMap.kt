package com.example.myparking.fragements

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.PointF
import android.location.Location

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myparking.R
import com.example.myparking.activities.ParkingsDetailsContainer
import com.example.myparking.adapters.MyAdapter
import com.example.myparking.adapters.OnSearchListener
import com.example.myparking.adapters.ParkingCarouselAdapter
import com.example.myparking.databinding.FragmentParkingsMapBinding
import com.example.myparking.databinding.ParkingCarouselItem2Binding
import com.example.myparking.databinding.ParkingCarouselItemBinding

import com.example.myparking.models.Parking
import com.example.myparking.models.ParkingModel
import com.example.myparking.models.RouteDetail
import com.example.myparking.models.SearchResult
import com.example.myparking.repositories.ParkingListRepository
import com.example.myparking.utils.DataSource
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.MapView
import com.example.myparking.utils.MapsUtils
import com.example.myparking.viewmodels.ParkingListViewModel
import com.example.myparking.viewmodels.ParkingListViewModelFactory

import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.here.android.mpa.common.*
import com.here.android.mpa.common.MapSettings.setIsolatedDiskCacheRootPath
import com.here.android.mpa.mapping.*
import com.here.android.mpa.mapping.Map
import com.here.android.mpa.routing.*
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import kotlinx.android.synthetic.main.bottom_sheet_layout.view.*
import kotlinx.android.synthetic.main.fragment_parkings_map.view.*
import java.io.File
import java.lang.ref.WeakReference
import java.math.BigDecimal

/**
 * The map fragment , containing a map with parking pins
 * @author BOUAYACHE
 * @property carousel A carousel of parkings (horizontal list)
 * @property bottomSheetBehavior Behaviour of bottom sheet (Collapsed or Expanded)
 * @property infiniteAdapter Adapter with inifinite behaviour
 * @property mMap Map from Google Maps SDK
 * @property mMapView The map view in the layout
 * @property markers The list of pins of parkings
 * @property parkings The List of parkings
 * @property binding Binding data with the view
 */
class ParkingsMap() : Fragment(), /*OnMapReadyCallback, GoogleMap.OnMarkerClickListener ,GoogleMap.OnMapClickListener,*/
    OnLocationListener , OnSearchListener, OnEngineInitListener,
    PositioningManager.OnPositionChangedListener, MapGesture.OnGestureListener {


    private var listDetails = arrayListOf<RouteDetail>()

    private lateinit var carousel: DiscreteScrollView
    private lateinit var bottomSheetBehavior : BottomSheetBehavior<LinearLayout>
    private  var mAdapter: ParkingCarouselAdapter? = null

    private lateinit var infiniteAdapter: InfiniteScrollAdapter<MyAdapter<RouteDetail, ParkingCarouselItem2Binding>.MyViewHolder>

    private lateinit var mMap: Map
    private lateinit var mMapView: AndroidXMapFragment
    private lateinit var pstManager: PositioningManager
    private lateinit var router: CoreRouter
    private var parkings: ArrayList<Parking> = arrayListOf()
    private var markers: ArrayList<MapMarker> = arrayListOf()
    private var route: MapRoute? = null
    private lateinit var mView : View
    private lateinit var binding: FragmentParkingsMapBinding
    private lateinit var mParkingListViewModel: ParkingListViewModel

    override fun onSearchClick(searchResult: SearchResult) {

        /*val pos= LatLng(searchResult.position[0], searchResult.position[1])
        mMap.addMarker(
            MarkerOptions()
                .position(pos)
                .title(searchResult.categoryTitle)
                .icon(
                    BitmapDescriptorFactory.fromBitmap(
                        MapsUtils.createCustomMarker(
                            context, mMapView,
                            R.color.authorized, ""
                        )
                    )
                )
        ).tag = searchResult
        //mMap.addMarker(MarkerOptions().position(myLocation).title("Marker in my location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pos))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15.0f))*/
    }

    fun getSearchListner():OnSearchListener {
        return this
    }
    /**
     * Request location from Maps Util
     * @see com.example.myparking.utils.MapsUtils.getLastLocation
     */
    fun requestLocation() {
        val listener = this
        if (context!=null) {
            MapsUtils.getLastLocation(context!!, object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult?) {
                    listener.onLocationReady(p0?.lastLocation!!)
                }
            }, this)
        }

    }

    /**
     * Initialize the map view when map configuration is ready
     * @param p0 Google Map (Parameters)
     */
    fun onMapReady() {
        Log.d("htest", "hhh here")
        Log.d("null context", context.toString())
        Log.d("null view", mView.toString())
        /*mMap = p0
        this.requestLocation()
        mMap.setOnMarkerClickListener(this)
        mMap.setOnMapClickListener(this)
        mMap.setMinZoomPreference(10.0f)
        mMap.setMaxZoomPreference(20.0f)*/
        var r = 0
        parkings?.forEach {
            val icon = Image()
            val prc = ((BigDecimal(it.nbPlaces).minus(BigDecimal(it.nbPlacesDisponibles)))
                .div(BigDecimal(it.nbPlaces)) * BigDecimal(100)).toInt().toString() +"%"
            icon.bitmap =  MapsUtils.createCustomMarker(
                context!!, mView as ViewGroup,
                R.color.colorPrimary, prc
            )
            val marker = MapMarker(GeoCoordinate(it.lattitude, it.longitude),icon)
            mMap.addMapObject(marker)
            /*val pin = mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(it.lattitude, it.longitude))
                    .title(it.nom)
                    .icon(
                        BitmapDescriptorFactory.fromBitmap(

                        )
                    )
            )*/
            //marker.tag = it
            markers.add(marker)
            r++
        }

    }

    /**
     * Triggered when the user clickes on a parking marker
     * @param p0 The marker (pin) of the parking
     * @return boolean value representing the state of the action triggered (succeed or failed)
     */
    /*override fun onMarkerClick(p0: Marker?): Boolean {
        Log.d("dialod not ", "not")
        //val parking = p0?.tag as ParkingModel
//        ParkingBottomSheet.newInstance(parking).show(activity?.supportFragmentManager,"ActionBottomDialog")
        val obj = p0?.tag
        if (obj is Parking) {
            val target = parkings.indexOf(obj)
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                carousel.smoothScrollToPosition(infiniteAdapter.getClosestPosition(target))
            }else {
                carousel.scrollToPosition(infiniteAdapter.getClosestPosition(target))
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        } else if (obj is SearchResult) {

        }

        return false
    }*/

    /**
     * Triggered when the user clicks on the map
     * @param p0 Coordinates (Lat and Long)
     */
    /*override fun onMapClick(p0: LatLng?) {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }*/


    /**
     * Show the current position when the location is ready
     * @see com.example.myparking.utils.MapsUtils.getLastLocation
     * @param location Coordinates (Lat and Long)
     */
    override fun onLocationReady(location: Location) {
        /*Log.d("here", "mylocation")
        mMap.isMyLocationEnabled = true
        val myLocation = LatLng(location.latitude, location.longitude)
        //mMap.addMarker(MarkerOptions().position(myLocation).title("Marker in my location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(13.0f))*/
    }

    /**
     * Handle the result of the permission request
     * @param requestCode Code of the request
     * @param permissions List of permissions codes
     * @param grantResults Result of permissions request (Granted or Refused)
     */
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

    /**
     * Triggered when the fragment is resumed
     */
    override fun onResume() {
        super.onResume()
        this.requestLocation()

    }


    /**
     * This function initialize the map fragment properties
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {

        }
    }

    /**
     * This function initialize the map view fragment
     * @param inflater The layout inflater, used to render the view
     * @param container The parent view (holding the fragment)
     * @return The view
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_parkings_map, container, false)
        binding.lifecycleOwner = activity
        //val rootView = inflater.inflate(R.layout.fragment_parkings_map, container, false)

        mView = binding.root

        mMapView = childFragmentManager.findFragmentById(R.id.hereMapfragment) as AndroidXMapFragment

        val factory = ParkingListViewModelFactory(ParkingListRepository.getInstance())

        mParkingListViewModel = ViewModelProviders.of(this.activity!!, factory)
            .get(ParkingListViewModel::class.java)

        mParkingListViewModel.getParkingsList().observe(this, Observer<ArrayList<Parking>>
        {
            parkings=it
            if (mapInitialized) {
                initiliazeRouteInfos()
            } else {
                mMapView.init(this)

            }


        })
        val listener = object : MyAdapter.ItemAdapterListener<Parking> {
            override fun onItemClicked(item: Parking) {
                val navController = Navigation.findNavController(activity!!,R.id.my_nav_host_fragment)
                val list = mParkingListViewModel.getParkingsList().value
                val index = list?.indexOf(item)
                val bundle= bundleOf("parking" to item, "parkingIndex" to index)
                navController.navigate(R.id.action_mainActivity2_to_parkingsDetailsContainer, bundle)
            }

        }
        mAdapter = ParkingCarouselAdapter(listDetails, object :MyAdapter.ItemAdapterListener<RouteDetail>{
            override fun onItemClicked(item: RouteDetail) {
                // fill up later
            }

        })
        mAdapter?.frgManager = childFragmentManager

        infiniteAdapter = InfiniteScrollAdapter.wrap(
            mAdapter!!
        )

        val llBottomSheet =  mView.findViewById<LinearLayout>(R.id.bottom_sheet)

// init the bottom sheet behavior

        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet)

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED





        val success = setIsolatedDiskCacheRootPath(
                context?.applicationContext?.getExternalFilesDir(null)?.path + File.separator + ".here-maps",
        "here_map_intent")






        /*mMapView.onResume()

        try {
            MapsInitializer.initialize(activity!!.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mMapView.getMapAsync(this)*/


        return mView
    }

    fun initiliazeRouteInfos() {

        mMap.removeMapObjects(markers.toList())
        markers.clear()
        listDetails.clear()

        if (mapInitialized && parkings.isNotEmpty()) {
            val currentCooridinates = pstManager.position.coordinate
            calculateRoute(0,currentCooridinates)
        }
    }

    fun calculateRoute(index: Int, currentCooridinates: GeoCoordinate) {
        val target = parkings[index]
        var next = -1
        if (index<parkings.size-1) next = index+1
        val points = arrayListOf<GeoCoordinate>(
            GeoCoordinate(currentCooridinates.latitude, currentCooridinates.longitude),
            GeoCoordinate(target.lattitude, target.longitude)
        )

        val routePlan = RoutePlan()
        routePlan.addWaypoint(RouteWaypoint(points[0]))
        routePlan.addWaypoint(RouteWaypoint(points[1]))

        val routeOptions = RouteOptions()
        routeOptions.transportMode = RouteOptions.TransportMode.CAR
        routeOptions.routeType = RouteOptions.Type.BALANCED

        routePlan.routeOptions = routeOptions

        router.calculateRoute(routePlan, object: CoreRouter.Listener {
            override fun onCalculateRouteFinished(
                p0: MutableList<RouteResult>?,
                p1: RoutingError?
            ) {
                if (p1 == RoutingError.NONE) {
                    listDetails.add(RouteDetail(target,p0?.get(0)?.route!!,mMap))
                    mAdapter?.notifyDataSetChanged()
                    infiniteAdapter.notifyDataSetChanged()
                    val icon = Image()
                    val prc = ((BigDecimal(target.nbPlaces).minus(BigDecimal(target.nbPlacesDisponibles)))
                        .div(BigDecimal(target.nbPlaces)) * BigDecimal(100)).toInt().toString() +"%"
                    icon.bitmap =  MapsUtils.createCustomMarker(
                        context!!, binding.root as ViewGroup,
                        R.color.colorPrimary, prc
                    )
                    val marker = MapMarker(GeoCoordinate(target.lattitude, target.longitude),icon)
                    mMap.addMapObject(marker)
                    markers.add(marker)
                    if (next>0) calculateRoute(next,currentCooridinates)
                } else {
                    Log.i("errorRoute",p1.toString())
                }
            }

            override fun onProgress(p0: Int) {
            }

        })
    }

    var mapInitialized=false
    override fun onEngineInitializationCompleted(p0: OnEngineInitListener.Error?) {
        if (p0 == OnEngineInitListener.Error.NONE) {
            // now the map is ready to be used

            pstManager = PositioningManager.getInstance()
            pstManager.start(PositioningManager.LocationMethod.GPS_NETWORK_INDOOR)
            pstManager.addListener(
                WeakReference<PositioningManager.OnPositionChangedListener>(this))
            mMapView.mapGesture.addOnGestureListener(this,100,false)
            mMap = mMapView.map
            mMapView.positionIndicator.isVisible = true
            mMap.projectionMode = Map.Projection.MERCATOR
//            mMap.zoomLevel = 13.0
//            mMap.setCenter(GeoCoordinate(36.7553388,3.0605876), Map.Animation.NONE)

            mapInitialized = true
            router = CoreRouter()

            // ...
        } else {
            Log.d("errorHere",p0?.details.toString())
        }

    }

    var firstPos = true
    override fun onPositionFixChanged(
        p0: PositioningManager.LocationMethod?,
        p1: PositioningManager.LocationStatus?
    ) {


    }

    override fun onPositionUpdated(
        p0: PositioningManager.LocationMethod?,
        p1: GeoPosition?,
        p2: Boolean
    ) {
        if (firstPos) {
            firstPos = false


            p1?.let {
                mMap.zoomLevel = 14.0
                mMap.setCenter(GeoCoordinate(it.coordinate.latitude,it.coordinate.longitude), Map.Animation.BOW)
                initiliazeRouteInfos()
            }

        }

    }

    override fun onMapObjectsSelected(p0: MutableList<ViewObject>?): Boolean {
        for (viewObj in p0!!) {
            if (viewObj.baseType == ViewObject.Type.USER_OBJECT) {
                if ((viewObj as MapObject).type == MapObject.Type.MARKER) {

                    val index = markers.indexOf(viewObj)
                    val target = mAdapter!!.routeDetailsList[index]

                    Log.d("indexHERE", index.toString())
                        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                            carousel.smoothScrollToPosition(infiniteAdapter.getClosestPosition(index))
                        } else {
                            carousel.scrollToPosition(infiniteAdapter.getClosestPosition(index))
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                        }

                    if (route!=null) mMap.removeMapObject(route)
                    route = MapRoute(target.route)
                    mMap.addMapObject(route)




                }
            }
        }

        return true
    }

//    override fun onCalculateRouteFinished(p0: MutableList<RouteResult>?, p1: RoutingError?) {
//        if (p1 == RoutingError.NONE) {
//            if (route!=null) mMap.removeMapObject(route)
//            route = MapRoute(p0?.get(0)?.route)
//            mMap.addMapObject(route)
//        } else {
//            Log.i("errorRoute",p1.toString())
//        }
//    }
//
//    override fun onProgress(p0: Int) {
//        Log.d("PROGRESS",p0.toString())
//    }

    /**
     * Triggered when the main view is created
     * @param view The root view
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        carousel = binding.root.carousel

        carousel.setOffscreenItems(1)
        carousel.setSlideOnFling(true)


        carousel.adapter = infiniteAdapter
        carousel.setItemTransformer(
            ScaleTransformer.Builder()
                .setMaxScale(1f)
                .setMinScale(0.95f)
                .setPivotX(Pivot.X.CENTER)
                .setPivotY(Pivot.Y.BOTTOM)
                .build()
        )

      /*  carousel.addOnItemChangedListener { p0, p1 ->
            val realPos = infiniteAdapter.getRealPosition(p1)
            Log.d("position", realPos.toString())
            val marker = markers[realPos]

            /*mMap.animateCamera(
                CameraUpdateFactory.newLatLng(marker.position),
                500,
                object : GoogleMap.CancelableCallback {
                    override fun onFinish() {
                        marker.showInfoWindow()
                    }

                    override fun onCancel() {

                    }

                })*/
        }*/
    }

    override fun onLongPressRelease() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onRotateEvent(p0: Float): Boolean {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
       return false
    }

    override fun onMultiFingerManipulationStart() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onPinchLocked() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onPinchZoomEvent(p0: Float, p1: PointF?): Boolean {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        return false
    }

    override fun onTapEvent(p0: PointF?): Boolean {
        if (mMap.getSelectedObjects(p0).isEmpty()) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            val obj = mMap.getSelectedObjects(p0)[0]
            if (obj !is MapMarker || !markers.contains(obj)) bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }


        return false
    }

    override fun onPanStart() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onMultiFingerManipulationEnd() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onDoubleTapEvent(p0: PointF?): Boolean {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        return false
    }

    override fun onPanEnd() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onTiltEvent(p0: Float): Boolean {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        return false
    }

    override fun onRotateLocked() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onLongPressEvent(p0: PointF?): Boolean {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        return false
    }

    override fun onTwoFingerTapEvent(p0: PointF?): Boolean {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        return false
    }



    companion object {
        fun getListener():OnSearchListener  {
            return object : OnSearchListener {
                override fun onSearchClick(searchResult: SearchResult) {

                }

            }
        }
    }


}

/**
 * Interface: Represing a listener of the current location
 */
interface OnLocationListener {
    /**
     * Callback when the location is ready (provided)
     * @param location Coordinates (Lat and Long)
     */
    fun onLocationReady(location: Location)
}


