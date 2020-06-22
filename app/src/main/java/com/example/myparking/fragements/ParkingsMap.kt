package com.example.myparking.fragements

import android.content.Intent
import android.graphics.PointF
import android.location.Location

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.myparking.HomeActivity
import com.example.myparking.R
import com.example.myparking.adapters.MyAdapter
import com.example.myparking.adapters.ParkingCarouselAdapter
import com.example.myparking.databinding.FragmentParkingsMapBinding
import com.example.myparking.databinding.ParkingCarouselItemBinding

import com.example.myparking.models.Parking
import com.example.myparking.models.SearchResult
import com.example.myparking.models.UpdateLocationRequest
import com.example.myparking.repositories.ParkingListRepository
import com.example.myparking.utils.*
import com.example.myparking.utils.MapAction.*
import com.example.myparking.viewmodels.ParkingListViewModel
import com.example.myparking.viewmodels.ParkingListViewModelFactory
import com.facebook.shimmer.ShimmerFrameLayout

import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.here.android.mpa.common.*
import com.here.android.mpa.common.MapSettings.setIsolatedDiskCacheRootPath
import com.here.android.mpa.guidance.NavigationManager
import com.here.android.mpa.mapping.*
import com.here.android.mpa.mapping.Map
import com.here.android.mpa.odml.MapLoader
import com.here.android.mpa.odml.MapPackage
import com.here.android.mpa.routing.*
import com.luseen.spacenavigation.SpaceNavigationView
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import kotlinx.android.synthetic.main.bottom_sheet_layout.view.*
import net.cachapa.expandablelayout.ExpandableLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.lang.ref.WeakReference
import java.math.BigDecimal
import kotlin.collections.ArrayList

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
class ParkingsMap(val parentView:View) : Fragment(),
    OnEngineInitListener, NavigationListener,
    PositioningManager.OnPositionChangedListener, MapGesture.OnGestureListener, MapLoader.Listener {



    private lateinit var carousel: DiscreteScrollView
    private lateinit var bottomSheetBehavior : BottomSheetBehavior<LinearLayout>
    private  var mAdapter: ParkingCarouselAdapter? = null

    private lateinit var infiniteAdapter: InfiniteScrollAdapter<MyAdapter<Parking, ParkingCarouselItemBinding>.MyViewHolder>
    private lateinit var mMap: Map
    private lateinit var mMapView: AndroidXMapFragment
    private var pstManager: PositioningManager? = null
    private lateinit var router: CoreRouter
    private var parkings: ArrayList<Parking> = arrayListOf()
    private var markers: ArrayList<CustomMarker> = arrayListOf()
    private var destinationMarker : CustomMarker? = null
    private var carMarker : CustomMarker? = null
    private var route: MapRoute? = null
    private lateinit var mView : View
    private lateinit var binding: FragmentParkingsMapBinding
    private lateinit var mParkingListViewModel: ParkingListViewModel
    private lateinit var prefManager: PreferenceManager
    private var destination: SearchResult? = null

    private var actionType: MapAction? = null
    private var data: Any? = null



    /**
     * Triggered when the fragment is resumed
     */
    override fun onResume() {
        super.onResume()


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
        prefManager = PreferenceManager(context!!)
        val idDriver = prefManager.checkDriverProfile().toInt()
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_parkings_map, container, false)
        binding.lifecycleOwner = activity
        //val rootView = inflater.inflate(R.layout.fragment_parkings_map, container, false)

        mView = binding.root

        mMapView = childFragmentManager.findFragmentById(R.id.hereMapfragment) as AndroidXMapFragment

        val factory = ParkingListViewModelFactory(ParkingListRepository.getInstance(),idDriver,prefManager)

        mParkingListViewModel = ViewModelProviders.of(this.activity!!, factory)
            .get(ParkingListViewModel::class.java)

        mParkingListViewModel.filteredParkings.observe(this, Observer<ArrayList<Parking>>
        {
            parkings=it
            mAdapter?.updateList(it)
            if (mapInitialized) hideLoading()
            setupMarkers()

        })

        initCarousel()



        setAction(arguments)
        initMapEngine()


        return mView
    }



    fun setAction(arguments: Bundle?) {
        if (arguments==null) {
            actionType = NO_ACTION
            data = null
        } else {
            actionType = arguments.get("actionType") as MapAction?
            data = arguments.get("data")
        }

    }

    fun initMapEngine() {
       // val diskCacheRoot = activity!!.filesDir.path + File.separator + ".isolated-here-maps"
        // Retrieve intent name from manifest
        //var intentName = "here_map_intent"
        /*try {
            val ai = activity!!.packageManager.getApplicationInfo(activity!!.packageName, PackageManager.GET_META_DATA)
            val bundle = ai.metaData
            intentName = bundle.getString("INTENT_NAME")!!
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("map cache error", "Failed to find intent name, NameNotFound: " + e.message)
        }*/

        //val success = setIsolatedDiskCacheRootPath(diskCacheRoot, intentName)
        val success = setIsolatedDiskCacheRootPath(
            context?.applicationContext?.getExternalFilesDir(null)?.path + File.separator + ".here-maps")
            //"here_map_intent")


        if (mapInitialized) {
            hideLoading()
            setupMarkers()
            handleAction()
        } else {
            showLoading()
            mMapView.init(this)

        }

    }

    fun showLoading() {
        val ph=mView.findViewById<ShimmerFrameLayout>(R.id.map_shimmer)
        ph.visibility = VISIBLE
        //mView.findViewById<View>(R.id.hereMapfragment).visibility = GONE
        ph.startShimmer()
    }
    fun hideLoading() {
        val ph=mView.findViewById<ShimmerFrameLayout>(R.id.map_shimmer)
        ph.stopShimmer()
        //mView.findViewById<View>(R.id.hereMapfragment).visibility = VISIBLE
        ph.visibility = GONE
    }

    var mapInitialized=false
    private lateinit var mapLoader: MapLoader
    override fun onEngineInitializationCompleted(p0: OnEngineInitListener.Error?) {
        Log.d("error engine type",p0?.name.toString())
        if (p0 == OnEngineInitListener.Error.NONE) {
            // now the map is ready to be used

           /* mapLoader = MapLoader.getInstance()
            mapLoader.addListener(this)
            mapLoader.mapPackages*/

            mMapView.mapGesture?.addOnGestureListener(this,100,false)
            mMap = mMapView.map!!

            pstManager = PositioningManager.getInstance()
            pstManager?.start(PositioningManager.LocationMethod.GPS_NETWORK_INDOOR)
            pstManager?.addListener(
                WeakReference<PositioningManager.OnPositionChangedListener>(this))



            mMapView.positionIndicator?.isVisible = true
            mMap.projectionMode = Map.Projection.MERCATOR
           mMap.zoomLevel = 13.0
            mMap.setCenter(GeoCoordinate(36.7553388,3.0605876), Map.Animation.NONE)
            hideLoading()
            mapInitialized = true
            router = CoreRouter()

            setupMarkers()

            handleAction()

            // ...
        } else {
            Log.d("errorHere",p0?.details.toString())
        }

    }

    fun setupMarkers() {
        if (mapInitialized && parkings.isNotEmpty()) {
            mMap.removeMapObjects(markers.toList())
            markers.clear()
            parkings.forEach {target->
                var icon = Image()
                val prc = ((target.nbPlacesLibres.toDouble() / target.nbPlaces.toDouble())*100).toInt().toString() + "%"
                icon.setBitmap(  MapsUtils.createCustomMarker(
                    context!!, binding.root as ViewGroup,R.layout.pin_layout,
                    R.color.colorPrimary, prc
                ))
                val marker = CustomMarker(GeoCoordinate(target.lattitude, target.longitude),icon,
                    PlaceType.PARKING, target)
                mMap.addMapObject(marker)
                markers.add(marker)
            }
        }
    }



    fun handleAction() {
        if (data != null && actionType != null) {
            when (actionType) {
                NO_ACTION -> {

                }
                SEARCH_ACTION -> {
                    Log.d("search action", "action")
                    this.destination = data as SearchResult
                    updateLocationInfo(this.destination, false)
                    markDestination()
                    initDestinationTopInfo()
                }
                NAVIGATION_ACTION -> {
                    val target = data as Parking
                    val index = parkings.indexOf(target)
                    // show loading in carousel
                    calculateRoute(target, object : CoreRouter.Listener {
                        override fun onCalculateRouteFinished(
                            p0: MutableList<RouteResult>?,
                            p1: RoutingError
                        ) {
                            if (p1 == RoutingError.NONE) {
                                if (route != null) mMap.removeMapObject(route!!)
                                route = MapRoute(p0!![0].route)
                                mMap.addMapObject(route!!)
                                mMap.setCenter(GeoCoordinate(target.lattitude, target.longitude), Map.Animation.BOW)
                            }
                            Log.d("error route", p1?.name)
                        }

                        override fun onProgress(p0: Int) {
                            Log.d("doin0g", " soemthing")
                        }

                    })

                    Log.d("indexHERE", index.toString())
                    if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                        carousel.smoothScrollToPosition(infiniteAdapter.getClosestPosition(index))
                    } else {
                        carousel.scrollToPosition(infiniteAdapter.getClosestPosition(index))
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }
                MARK_CAR -> {
                  if (pstManager!==null) {
                      saveCarLocation()
                  } else {
                      // show message dialog
                  }
                }
                FIND_CAR -> {

                }
            }
        }
    }

    private fun updateLocationInfo(destination:SearchResult?, reset:Boolean) {
        val automobilisteId = prefManager.checkDriverProfile().toInt()
        var request = UpdateLocationRequest(automobilisteId, 0.0,0.0, true)
        if (reset) {
            prefManager.clearDestinationLocation()
            val start = prefManager.getLastLocationStr()
            val list = start?.split(',')!!
            request = UpdateLocationRequest(automobilisteId,
                list[0].toDouble() ,list[1].toDouble(), forSearch=true)
        } else {
            prefManager.writeDestinationLocation(destination!!)
            Log.d("SENDING NEW LOCATION", (destination.position[0]+destination.position[1]).toString())
            request = UpdateLocationRequest(automobilisteId,
                destination.position[0],destination.position[1], forSearch = true)
        }
        InjectorUtils.provideLocationService().updateLocation(request).enqueue(object :
            Callback<Any> {
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.d("failure update location", "failure to update")
            }

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                mParkingListViewModel.refrshFilteredParkings()
            }
        })

    }

    fun saveCarLocation() {
        val currentPos = pstManager?.position!!.coordinate
        val icone = Image()
        icone.setBitmap(MapsUtils.createCustomMarker(context!!,binding.root as ViewGroup,
            R.layout.car_marker_layout, R.color.authorized, ""))
        carMarker = CustomMarker(currentPos, icone,PlaceType.CAR_LOCATION,currentPos)
        mMap.addMapObject(carMarker!!)
    }

    fun markDestination() {
        val icon = Image()
        val title = "D"
        icon.setBitmap( MapsUtils.createCustomMarker(
            context!!, binding.root as ViewGroup,R.layout.pin_layout,
            R.color.centre_button_color, title
        ))
        val geo = GeoCoordinate(destination!!.position[0], destination!!.position[1])
        val marker = CustomMarker(geo, icon,PlaceType.DESTINATION, destination!!)
        destinationMarker = marker
        mMap.addMapObject(destinationMarker!!)
        mMap.zoomLevel = 14.0
        mMap.setCenter(
            GeoCoordinate(geo),
            Map.Animation.LINEAR
        )


    }

    fun initDestinationTopInfo() {
        if(destination!==null) {

            //mAdapter!!.setDestination()
            initCarousel()


            val bitmap =  MapsUtils.createCustomMarker(
                context!!, binding.root as ViewGroup,R.layout.pin_layout,
                R.color.centre_button_color, "D"
            )
            mView.findViewById<ImageView>(R.id.destination_ico).setImageBitmap(bitmap)
            mView.findViewById<TextView>(R.id.destination_txt).text = destination!!.title
            showDestinationTopInfo(true)
            mView.findViewById<ImageView>(R.id.destination_close).setOnClickListener {
                onCloseDestinationInfo()

            }

        }
    }


    fun onCloseDestinationInfo() {
        updateLocationInfo(null,true)
        hideDestinationTopInfo(true)
        mMap.removeMapObject(route!!)
        mMap.removeMapObject(destinationMarker!!)
        route = null
        destinationMarker = null
        destination = null
        if (pstManager!==null) {
            mMap.setCenter(GeoCoordinate(pstManager?.position?.coordinate?.latitude!!,
                pstManager?.position?.coordinate?.longitude!!), Map.Animation.BOW)
        }

        initCarousel()


        //mAdapter!!.setDestination(null)
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
        if (firstPos && (actionType==null || actionType== NO_ACTION)) {
            firstPos = false


            p1?.let {
                mMap.zoomLevel = 14.0
                mMap.setCenter(
                    GeoCoordinate(it.coordinate.latitude, it.coordinate.longitude),
                    Map.Animation.BOW
                )
            }

        }

    }

    override fun onMapObjectsSelected(p0: MutableList<ViewObject>): Boolean {
        for (viewObj in p0!!) {
            if (viewObj.baseType == ViewObject.Type.USER_OBJECT) {
                if ((viewObj as MapObject).type == MapObject.Type.MARKER) {
                        if (viewObj is CustomMarker) {
                            if (viewObj.customType == PlaceType.PARKING) {

                                val target = viewObj.data as Parking
                                val index = parkings.indexOf(target)

                                // show loading in carousel

                                calculateRoute(target, object:CoreRouter.Listener {

                                    override fun onCalculateRouteFinished(
                                        p0: MutableList<RouteResult>?,
                                        p1: RoutingError
                                    ) {
                                        if (p1 == RoutingError.NONE) {
                                            if (route!=null) mMap.removeMapObject(route!!)
                                            route = MapRoute(p0!![0].route)
                                            mMap.addMapObject(route!!)
                                        }
                                        Log.d("error route",p1?.name)
                                    }

                                    override fun onProgress(p0: Int) {
                                        Log.d("doin0g"," soemthing")
                                    }

                                })

                                Log.d("indexHERE", index.toString())
                                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                                    carousel.smoothScrollToPosition(infiniteAdapter.getClosestPosition(index))
                                } else {
                                    carousel.scrollToPosition(infiniteAdapter.getClosestPosition(index))
                                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                                }
                                mView.findViewById<ExpandableLayout>(R.id.destination_top_info).collapse()
                            } else {

                            }
                        } else {

                        }


                }
            }
        }

        return true
    }


    /**
     * Triggered when the main view is created
     * @param view The root view
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    fun initCarousel() {
        mAdapter = ParkingCarouselAdapter(parkings, this,this.destination)
        infiniteAdapter = InfiniteScrollAdapter.wrap(mAdapter!!)
        val llBottomSheet =  mView.findViewById<LinearLayout>(R.id.bottom_sheet)
// init the bottom sheet behavior
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
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

        carousel.addOnItemChangedListener { p0, p1 ->

            if (mapInitialized &&  infiniteAdapter.itemCount>0) {

                val realPos = infiniteAdapter.getRealPosition(p1)
                Log.d("position", realPos.toString())
                val target = parkings[realPos]

                calculateRoute(target, object : CoreRouter.Listener {

                        override fun onCalculateRouteFinished(
                    p0: MutableList<RouteResult>?,
                    p1: RoutingError
                ) {
                        if (p1 == RoutingError.NONE) {
                            if (route != null) mMap.removeMapObject(route!!)
                            route = MapRoute(p0!![0].route)
                            mMap.addMapObject(route!!)
                        }
                        Log.d("error route", p1?.name)
                    }

                    override fun onProgress(p0: Int) {
                        Log.d("doin0g", " soemthing")
                    }

                })
                mMap.zoomLevel = 14.0
                mMap.setCenter(
                    GeoCoordinate(target.lattitude, target.longitude),
                    Map.Animation.LINEAR
                )
            }


        }
    }


    fun hideDestinationTopInfo(animate: Boolean) {
        val expandable =  mView.findViewById<ExpandableLayout>(R.id.destination_top_info)
        expandable.collapse(animate)
    }

    fun showDestinationTopInfo(animate: Boolean) {
        val expandable =  mView.findViewById<ExpandableLayout>(R.id.destination_top_info)
        if (destination!==null && !expandable.isExpanded) {
           expandable.expand(animate)
        }
    }

    override fun onLongPressRelease() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        showDestinationTopInfo(true)

    }

    override fun onRotateEvent(p0: Float): Boolean {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        showDestinationTopInfo(true)
       return false
    }

    override fun onMultiFingerManipulationStart() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        showDestinationTopInfo(true)
    }

    override fun onPinchLocked() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        showDestinationTopInfo(true)
    }

    override fun onPinchZoomEvent(p0: Float, p1: PointF): Boolean {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        showDestinationTopInfo(true)
        return false
    }

    override fun onTapEvent(p0: PointF): Boolean {
        if (mMap.getSelectedObjects(p0).isEmpty()) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            showDestinationTopInfo(true)
        } else {
            val obj = mMap.getSelectedObjects(p0)[0]
            if (obj !is MapMarker || !markers.contains(obj)) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                showDestinationTopInfo(true)
            }
        }


        return false
    }

    override fun onPanStart() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        showDestinationTopInfo(true)
    }

    override fun onMultiFingerManipulationEnd() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        showDestinationTopInfo(true)
    }

    override fun onDoubleTapEvent(p0: PointF): Boolean {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        showDestinationTopInfo(true)
        return false
    }

    override fun onPanEnd() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        showDestinationTopInfo(true)
    }

    override fun onTiltEvent(p0: Float): Boolean {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        showDestinationTopInfo(true)
        return false
    }

    override fun onRotateLocked() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        showDestinationTopInfo(true)
    }

    override fun onLongPressEvent(p0: PointF): Boolean {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        showDestinationTopInfo(true)
        return false
    }

    override fun onTwoFingerTapEvent(p0: PointF): Boolean {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        showDestinationTopInfo(true)
        return false
    }






    override fun onCheckForUpdateComplete(
        p0: Boolean,
        p1: String?,
        p2: String?,
        p3: MapLoader.ResultCode?
    ) {

    }

    override fun onInstallMapPackagesComplete(p0: MapPackage?, p1: MapLoader.ResultCode?) {

    }

    override fun onUninstallMapPackagesComplete(p0: MapPackage?, p1: MapLoader.ResultCode?) {

    }

    override fun onGetMapPackagesComplete(p0: MapPackage?, p1: MapLoader.ResultCode?) {

    }

    override fun onPerformMapDataUpdateComplete(p0: MapPackage?, p1: MapLoader.ResultCode?) {

    }



    override fun onProgress(p0: Int) {
       Log.d("download",p0.toString())
    }

    override fun onInstallationSize(p0: Long, p1: Long) {

    }


    fun calculateRoute(target:Parking, listener: CoreRouter.Listener ) {
        if (pstManager!=null) {


            val currentCooridinates = pstManager?.position?.coordinate
            val points = arrayListOf<GeoCoordinate>(
                GeoCoordinate(currentCooridinates!!.latitude, currentCooridinates.longitude),
                GeoCoordinate(target.lattitude, target.longitude)
            )
            val routePlan = RoutePlan()
            routePlan.addWaypoint(RouteWaypoint(points[0]))
            routePlan.addWaypoint(RouteWaypoint(points[1]))
            if (destination!=null && target.isWalkable()) {
                routePlan.addWaypoint(RouteWaypoint(GeoCoordinate(destination!!.position[0],destination!!.position[1])))
            }

            val routeOptions = RouteOptions()
            routeOptions.transportMode = RouteOptions.TransportMode.CAR
            routeOptions.routeType = RouteOptions.Type.BALANCED

            routePlan.routeOptions = routeOptions

            router.calculateRoute(routePlan, listener)
        }
    }


    override fun onItemClicked(item: Parking) {
        val navController = Navigation.findNavController(activity!!,R.id.my_nav_host_fragment)
        val list = mParkingListViewModel.filteredParkings.value
        val index = list?.indexOf(item)
        val bundle = bundleOf("parking" to item, "parkingIndex" to index, "favorites" to false,"filtered" to true)
        navController.navigate(R.id.action_mainActivity2_to_parkingsDetailsContainer, bundle)
    }

    override fun showNavigationChoice() {
        NavAppBottomDialog(this).show(childFragmentManager, "CHOOSE_NAV_APP")
    }

    override fun startGMAPSNavigation()  {
        val str = route?.route?.destination?.latitude.toString()+","+route?.route?.destination?.longitude.toString()
        val gmmIntentUri = Uri.parse("google.navigation:q=$str")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        context?.startActivity(mapIntent)

    }
    private lateinit var navEventsListener : NavigationManager.NewInstructionEventListener

    override fun startMyParkingNavigation()  {



        val inst_txt = mView.findViewById<TextView>(R.id.nav_instruction)
        val inst_ico = mView.findViewById<ImageView>(R.id.nav_icon)
        val inst_dist = mView.findViewById<TextView>(R.id.nav_dist)

        val navigationManager = NavigationManager.getInstance()

        navEventsListener = object:NavigationManager.NewInstructionEventListener() {
            override fun onNewInstructionEvent() {
                val maneuver = navigationManager.nextManeuver
                if (maneuver != null) {
                    if (maneuver.action == Maneuver.Action.END) {
                        // notify the user that the route is complete
                        NavigationManager.getInstance().stop()
                        setNormalView()
                        stopForegroundService()
                    }

                    inst_txt.text = MapsUtils.getInstructionText(maneuver.action!!)
                    inst_ico.setImageResource(MapsUtils.getNavigationIcon(maneuver.icon!!))
                    inst_dist.text = "${maneuver.distanceToNextManeuver} mètres"
                    /*Log.d("man",maneuver.action.toString())
                    Log.d("man_icon",maneuver.icon.toString())
                    Log.d("man_dist",maneuver.distanceToNextManeuver.toString())
                    Log.d("roadname",maneuver.roadName.toString())*/

                }
            }
        }
        setNavigationView()

        mMap.addSchemeChangedListener {
            Log.d("nav","here schem nav")
            (childFragmentManager.findFragmentByTag("CHOOSE_NAV_APP") as DialogFragment).dismiss()
            navigationManager.setMap(mMap)


            navigationManager.addNewInstructionEventListener(WeakReference(navEventsListener))
            startForegroundService()
            val error = navigationManager.simulate(route?.route!!,60)

            Log.d("errorNav", error.toString())
        }

    }

    private fun setNavigationView() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        mMap.mapScheme = Map.Scheme.CARNAV_DAY
        parentView.findViewById<SpaceNavigationView>(R.id.nav_view).visibility = GONE
        (activity as HomeActivity).customizeToolbar("",0f,false)
        mView.findViewById<CardView>(R.id.nav_top_section).visibility = VISIBLE
        mMap.zoomLevel = 18.0
        mMap.tilt = 60.0f
        mMap.setCenter(route?.route?.start!!, Map.Animation.BOW)
        NavigationManager.getInstance().mapUpdateMode = NavigationManager.MapUpdateMode.POSITION_ANIMATION
    }

    private fun setNormalView() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        mMap.mapScheme = Map.Scheme.NORMAL_DAY
        parentView.findViewById<SpaceNavigationView>(R.id.nav_view).visibility = VISIBLE
        (activity as HomeActivity).customizeToolbar("",56f,false)
        mView.findViewById<CardView>(R.id.nav_top_section).visibility = GONE
        mMap.zoomLevel = 18.0
        mMap.tilt = 0f
        NavigationManager.getInstance().mapUpdateMode = NavigationManager.MapUpdateMode.ROADVIEW
    }




    override fun onDestroy() {
        super.onDestroy()
        stopForegroundService()
    }

    var m_foregroundServiceStarted = false
    fun startForegroundService() {
        if (!m_foregroundServiceStarted) {
            m_foregroundServiceStarted = true
            val startIntent =  Intent(context!!, ForegroundService::class.java)
            startIntent.action = ForegroundService.START_ACTION
            context!!.applicationContext.startService(startIntent)
        }
    }

    fun stopForegroundService() {
        if (m_foregroundServiceStarted) {
            m_foregroundServiceStarted = false
            val stopIntent =  Intent(context!!, ForegroundService::class.java)
            stopIntent.action = ForegroundService.STOP_ACTION
            context!!.applicationContext.startService(stopIntent)
        }
    }


    companion object {

        enum class PlaceType {
            PARKING, DESTINATION, CAR_LOCATION
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

interface NavigationListener: MyAdapter.ItemAdapterListener<Parking> {
    fun showNavigationChoice()
    fun startGMAPSNavigation()
    fun startMyParkingNavigation()
}

