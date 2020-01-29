package com.example.myparking.fragements

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.myparking.R
import com.example.myparking.*


class SignInFragment : Fragment() {

    //private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)
        view.findViewById<Button>(R.id.login)?.setOnClickListener {
            val mainActivityIntent = Intent(activity, MainActivity::class.java)
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(mainActivityIntent)
        }

        view.findViewById<TextView>(R.id.sign_up)?.setOnClickListener {
            val fragment = SignUpFragment()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.login_container, fragment)?.commit()
        }

        return view
    }
}
