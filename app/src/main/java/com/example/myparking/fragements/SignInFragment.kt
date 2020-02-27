package com.example.myparking.fragements

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.myparking.R
import com.example.myparking.*
import com.example.myparking.models.SignInModelRequest
import com.example.myparking.models.SignInModelResponse
import com.example.myparking.utils.InjectorUtils
import com.example.myparking.utils.PreferenceManager
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.R.string.cancel
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.fragment_sign_in.*


class SignInFragment : Fragment(), Callback<SignInModelResponse> {

    //private var listener: OnFragmentInteractionListener? = null

    private lateinit var signInButton : Button
    private lateinit var singnInLoading: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)
        signInButton = view.findViewById(R.id.login_btn)
        singnInLoading = view.findViewById(R.id.signInLoading)
        signInButton.setOnClickListener {
//            val mainActivityIntent = Intent(activity, MainActivity::class.java)
//            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(mainActivityIntent)
            signIn(view)

        }

        view.findViewById<Button>(R.id.sign_up)?.setOnClickListener {
            val fragment = SignUpFragment()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.login_container, fragment)?.commit()
        }

        return view
    }

    fun signIn(view: View) {
        val username = view.findViewById<TextInputEditText>(R.id.user_name).text.toString()
        val password = view.findViewById<TextInputEditText>(R.id.user_password).text.toString()
        val service = InjectorUtils.provideAuthService()
        showLoading()
        service.signIn(SignInModelRequest(username,password)).enqueue(this)
    }

    override fun onFailure(call: Call<SignInModelResponse>, t: Throwable) {
        Log.d("error", t.printStackTrace().toString())
        hideLoading()
        SignInErrorDialog().show(activity?.supportFragmentManager,"SIGN_IN_ERROR")
    }

    override fun onResponse(
        call: Call<SignInModelResponse>,
        response: Response<SignInModelResponse>
    ) {
        Log.d("signinCode",response.code().toString())
        if (response.code()==200) {
           /* val id = response.body()?.driverProfile?.idAutomobiliste.toString()
            PreferenceManager(context!!).writeDriverId(id)*/
            val mainActivityIntent = Intent(activity, HomeActivity::class.java)
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(mainActivityIntent)
        } else {
            hideLoading()
            SignInErrorDialog().show(activity?.supportFragmentManager,"SIGN_IN_ERROR")
        }

    }

    fun showLoading() {
        signInButton.visibility = GONE
        signInLoading.visibility = VISIBLE
    }
    fun hideLoading() {
        signInButton.visibility = VISIBLE
        signInLoading.visibility = GONE
    }
}
