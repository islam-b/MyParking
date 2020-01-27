package com.example.myparking.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.ConnectivityManager.EXTRA_NO_CONNECTIVITY
import android.net.Network
import android.net.NetworkCapabilities
import android.net.wifi.p2p.WifiP2pDevice.CONNECTED

import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat.getExtras
import com.example.myparking.MainActivity
import com.example.myparking.activities.NoConnexionActivity


class NetworkReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("app", "Network connectivity change")
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        if (checkNetworkState(connectivityManager!!)) {
            Log.d("a","trying to start main")
            val openMainActivity = MainActivity.newIntent(context!!)
            openMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(openMainActivity)
        }else {
            val i = NoConnexionActivity.newIntent(context!!)
            context.startActivity(i)
        }

    }

    companion object {
        fun checkNetworkState(connectivityManager : ConnectivityManager): Boolean {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val nw      = connectivityManager.activeNetwork ?: return false
                val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
                return when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            } else {
                val nwInfo = connectivityManager.activeNetworkInfo ?: return false
                return nwInfo.isConnected
            }
        }

//    val builder = NetworkRequest.Builder()
//    connectivityManager!!.registerNetworkCallback(
//    builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
//    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
//    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
//    .build(),
    }


}