package com.example.myparking.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

import com.example.myparking.R
import com.example.myparking.fragements.OnLocationListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.here.android.mpa.common.ApplicationContext
import com.here.android.mpa.routing.Maneuver
import com.here.android.mpa.routing.Maneuver.Action.*


object MapsUtils {

    var PERMISSION_ID=44
    @SuppressLint("StaticFieldLeak")
    private lateinit  var mFusedLocationClient: FusedLocationProviderClient

    fun initLocationProvider(context:Context) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    private fun checkPermissions(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions(app: AppCompatActivity) {
            app.requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }
    private fun isLocationEnabled(context:Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    fun getLastLocation(context:Context, locationCallback: LocationCallback, listener: OnLocationListener) {

        if (checkPermissions(context)) {
            if (isLocationEnabled(context)) {
                var location: Location?
                mFusedLocationClient.lastLocation.addOnCompleteListener { task ->
                    location = task.result
                    if (location == null) {
                        requestNewLocationData(
                            context,
                            locationCallback
                        )
                    } else {
                        listener.onLocationReady(location!!)
                    }
                }
            } else {
                Toast.makeText(context, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                ContextCompat.startActivity(context, intent, null)
            }
        } else {
            requestPermissions(context as AppCompatActivity)
        }
    }

    private fun requestNewLocationData(context:Context, listener: LocationCallback) {

        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, listener, Looper.myLooper()
        )

    }

    fun createCustomMarker(context: Context, parent: ViewGroup,@ColorRes color: Int, text: String): Bitmap {

        val marker = LayoutInflater.from(context).inflate(
            R.layout.pin_layout,
                parent, false
            )

        val markerImage = marker.findViewById<TextView>(R.id.pin_text)
        markerImage.backgroundTintList = ContextCompat.getColorStateList(context,color)
        markerImage.text = text

        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        marker.layoutParams = ViewGroup.LayoutParams(40,40)
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        //marker.buildDrawingCache()

        val bitmap = Bitmap.createBitmap(
            marker.measuredWidth,
            marker.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        marker.draw(canvas)


        return bitmap

    }

    @DrawableRes
    fun getNavigationIcon(icon: Maneuver.Icon):  Int {
        when(icon) {
            Maneuver.Icon.CHANGE_LINE -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.UNDEFINED -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.GO_STRAIGHT -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.UTURN_RIGHT -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.UTURN_LEFT -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.KEEP_RIGHT -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.LIGHT_RIGHT -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.QUITE_RIGHT -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.HEAVY_RIGHT -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.KEEP_MIDDLE -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.KEEP_LEFT -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.LIGHT_LEFT -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.QUITE_LEFT -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.HEAVY_LEFT -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.ENTER_HIGHWAY_RIGHT_LANE -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.ENTER_HIGHWAY_LEFT_LANE -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.LEAVE_HIGHWAY_RIGHT_LANE -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.LEAVE_HIGHWAY_LEFT_LANE -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.HIGHWAY_KEEP_RIGHT -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.HIGHWAY_KEEP_LEFT -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.ROUNDABOUT_1 -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.ROUNDABOUT_2 -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.ROUNDABOUT_3 -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.ROUNDABOUT_4 -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.ROUNDABOUT_5 -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.ROUNDABOUT_6 -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.ROUNDABOUT_7 -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.ROUNDABOUT_8 -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.ROUNDABOUT_9 -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.ROUNDABOUT_10 -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.ROUNDABOUT_11 -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.ROUNDABOUT_12 -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.ROUNDABOUT_1_LH -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.ROUNDABOUT_2_LH -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.ROUNDABOUT_3_LH -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.ROUNDABOUT_4_LH -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.ROUNDABOUT_5_LH -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.ROUNDABOUT_6_LH -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.ROUNDABOUT_7_LH -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.ROUNDABOUT_8_LH -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.ROUNDABOUT_9_LH -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.ROUNDABOUT_10_LH -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.ROUNDABOUT_11_LH -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.ROUNDABOUT_12_LH -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.START -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.END -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.FERRY -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.PASS_STATION -> {
                return R.drawable.triplearrow
            }
            Maneuver.Icon.HEAD_TO -> {
                return R.drawable.triplearrow
            }
        }
    }

    fun getInstructionText(action: Maneuver.Action?) : String {
        var txt = ""
        when (action) {
            UNDEFINED -> {
                txt = ""
            }
            NO_ACTION -> {
                txt = ""
            }
            END -> {
                txt = "Vous avez atteint la destination."
            }
            STOPOVER -> {
                txt = "Arrétez vous."
            }
            JUNCTION -> {
                txt = "Attention intersection"
            }
            ROUNDABOUT -> {
                txt = "Attention rond point."
            }
            UTURN -> {
                txt = "Faites demi-tour."
            }
            ENTER_HIGHWAY_FROM_RIGHT -> {
                txt = "Entrez sur l'autouroute de la droite."
            }
            ENTER_HIGHWAY_FROM_LEFT -> {
                txt = "Entrez sur l'autouroute de la gauche."
            }
            ENTER_HIGHWAY -> {
                txt = "Entrez sur l'autouroute."
            }
            LEAVE_HIGHWAY -> {
                txt = "Quitez l'autouroute."
            }
            CHANGE_HIGHWAY -> {
                txt = "Changez l'autouroute."
            }
            CONTINUE_HIGHWAY -> {
                txt = "Continuez sur la route."
            }
            FERRY -> {
                txt = "Attention, chemin de fer."
            }
            PASS_JUNCTION -> {
                txt = "Continuez sur la route."
            }
            HEAD_TO -> {
                txt = "Continuez droit devant."
            }
            PASS_STATION -> {
                txt = "Continuez sur la route."
            }
            CHANGE_LINE -> {
                txt = "Changez la file"
            }
            INVALID -> {
                txt = ""
            }
        }
        return txt
    }
}