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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

import com.example.myparking.R
import com.example.myparking.fragements.OnLocationListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices


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

    private fun requestPermissions(fragment: Fragment) {
        fragment.requestPermissions(
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
            requestPermissions(listener as Fragment)
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
}