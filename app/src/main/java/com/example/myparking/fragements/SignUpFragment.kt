package com.example.myparking.fragements

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.myparking.MainActivity
import com.example.myparking.R

class SignUpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)
        view.findViewById<Button>(R.id.create_account)?.setOnClickListener{
            val mainActivityIntent = Intent(activity, MainActivity::class.java)
            startActivity(mainActivityIntent)
        }

        view.findViewById<Button>(R.id.sign_in)?.setOnClickListener {
            val fragment = SignInFragment()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.login_container, fragment)?.commit()
        }

        return view
    }

}
