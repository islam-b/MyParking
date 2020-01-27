package com.example.myparking.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myparking.MainActivity
import com.example.myparking.R
import com.example.myparking.utils.NetworkReceiver
import kotlinx.android.synthetic.main.no_connection_layout.*

class NoConnexionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.no_connection_layout)

        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        try_again.setOnClickListener {
            if (NetworkReceiver.checkNetworkState(connectivityManager!!)) {
                startActivity(MainActivity.newIntent(this).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                })
            }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, NoConnexionActivity::class.java)
            return intent
        }
    }
}
