package com.example.myparking.fragements


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.pusher.pushnotifications.BeamsCallback
import com.pusher.pushnotifications.PushNotifications
import com.pusher.pushnotifications.PusherCallbackError
import android.content.Context.ACTIVITY_SERVICE
import android.app.ActivityManager
import android.content.Intent
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.appcompat.app.AlertDialog
import com.example.myparking.R
import com.example.myparking.utils.BeamsTokenProvider
import com.example.myparking.utils.PreferenceManager



class SettingsFragment : Fragment() {

    private lateinit var prefMgr : PreferenceManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Paramètres"
        prefMgr = PreferenceManager(context!!)
        val switch = root.findViewById<Switch>(R.id.settings_swith_auth)
        switch.isChecked = prefMgr.isNotifActivated()
        switch.setOnClickListener {
            toggleNotifAuth(switch.isChecked)
        }
        root.findViewById<ConstraintLayout>(R.id.settings_reset_app).setOnClickListener {
            resetApp()
        }
        root.findViewById<ConstraintLayout>(R.id.settings_more).setOnClickListener {
            moreSettings()
        }

        return root
    }

    fun toggleNotifAuth(isAthorized:Boolean) {
        if(isAthorized) {
            prefMgr.activateNotifications()
            initPusher()
        }
        else {
            prefMgr.desactivateNotifications()
            PushNotifications.clearAllState()
            PushNotifications.stop()
        }
    }
    fun resetApp() {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle("Confirmation")
        builder.setMessage("Etes vous sûre de vouloir réinitialiser l'application ?")
        builder.setIcon(R.drawable.ic_warning_red)
        builder.setPositiveButton("Oui") { dialog, which ->
            //clear app data
            (context?.getSystemService(ACTIVITY_SERVICE) as ActivityManager)
                .clearApplicationUserData()
            //restart app
            val i =activity?.baseContext?.packageManager
                ?.getLaunchIntentForPackage(activity?.baseContext?.packageName!!)
            i?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
            activity?.finish()
        }
        builder.setNegativeButton("Non") { dialog, which ->
            dialog.dismiss()
        }
        builder.create().show()

    }
    fun moreSettings() {
        val intent = Intent()
        intent.setAction(ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity?.packageName, null)
        intent.data = uri
        context?.startActivity(intent)
    }

    fun initPusher() {
        val driverId = prefMgr.checkDriverProfile()
        try {
            PushNotifications.stop()
        } catch (e:Exception) {

        }

        PushNotifications.start(context!!, "68987f6c-1b73-4e06-8515-b8db77033090")
        PushNotifications.clearAllState()
        PushNotifications.addDeviceInterest("driver_notifs")
        PushNotifications.addDeviceInterest("debug-testdriver")
        PushNotifications.addDeviceInterest("drivers")
        PushNotifications.setUserId(
            "driver$driverId",
            BeamsTokenProvider.getTokenProvider(driverId),
            object : BeamsCallback<Void, PusherCallbackError> {
                override fun onFailure(error: PusherCallbackError) {
                    Log.e("BeamsAuth", "Could not login to Beams: ${error.message}")
                }

                override fun onSuccess(vararg values: Void) {
                    Log.i("BeamsAuth", "Beams login success")
                }
            }
        )

    }


}
