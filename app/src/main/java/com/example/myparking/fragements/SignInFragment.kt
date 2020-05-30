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
import android.annotation.SuppressLint
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import com.facebook.*
import com.facebook.login.Login
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import kotlinx.android.synthetic.main.fragment_sign_in.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import android.util.Base64
import com.example.myparking.models.DriverProfile
import com.example.myparking.models.FbSignInModelRequest
import org.json.JSONObject
import java.lang.Exception


class SignInFragment : Fragment(), Callback<SignInModelResponse>, FacebookCallback<LoginResult> {


    //private var listener: OnFragmentInteractionListener? = null


    private lateinit var callbackManager: CallbackManager
    private lateinit var fbLogin : Button
    private lateinit var signInButton : Button
    private lateinit var singnInLoading: ProgressBar
    private lateinit var loginManager: LoginManager
    private lateinit var profileTracker: ProfileTracker
    var email=""
    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {



        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)
        rootView = view
        signInButton = view.findViewById(R.id.login_btn)
        singnInLoading = view.findViewById(R.id.signInLoading)
        signInButton.setOnClickListener {
//            val mainActivityIntent = Intent(activity, MainActivity::class.java)
//            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(mainActivityIntent)
            signIn(view)
        }

        fbLogin = view.findViewById(R.id.login_facebook)
        callbackManager = CallbackManager.Factory.create()
        val listener =  this
        profileTracker = object: ProfileTracker() {
            override fun onCurrentProfileChanged(oldProfile: Profile?, currentProfile: Profile?) {
                if (currentProfile!==null) {
                    Log.d("emailId",currentProfile.id!!)
                    Log.d("emailId",currentProfile.firstName)
                    Log.d("emailId",currentProfile.lastName)
                    Log.d("emailId",currentProfile.name)
                    val service = InjectorUtils.provideAuthService()
                    val id = currentProfile.id!!
                    val profile = DriverProfile(
                        null,currentProfile.lastName,"",currentProfile.firstName,
                        "facebook",id)
                    singnInLoading = rootView.findViewById(R.id.signInLoading)
                    showLoading()
                    service.signInWithFb(FbSignInModelRequest(email=email,username=id,driverProfile = profile))
                        .enqueue(listener)
                }

            }

        }

        profileTracker.startTracking()
        loginManager =  LoginManager.getInstance()
        /**remove this later**/
        LoginManager.getInstance().logOut()
        /** **/
        loginManager.registerCallback(callbackManager,this)
        fbLogin.setOnClickListener {
            loginManager.logInWithReadPermissions(this, listOf("email","public_profile"))
        }


        view.findViewById<Button>(R.id.sign_up)?.setOnClickListener {
            val fragment = SignUpFragment()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.login_container, fragment)?.commit()
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
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
        SignInErrorDialog("Une erreur s\'est produite, veuillez réesayez").show(childFragmentManager,"SIGN_IN_ERROR")
    }

    override fun onResponse(
        call: Call<SignInModelResponse>,
        response: Response<SignInModelResponse>
    ) {
        Log.d("signinCode",response.code().toString())
        if (response.code()==200) {
            val id = response.body()?.driverProfile?.idAutomobiliste.toString()
            val prfMgr = PreferenceManager(context!!)
            prfMgr.writeDriverProfile(response.body()?.driverProfile!!)
            prfMgr.writeInfoDriver(response.body()!!)
            startHomeActivity()
        } else {
            Log.d("error" , response.errorBody()?.string()!!)
            hideLoading()
            SignInErrorDialog("Nom d'utilisateur ou mot de passe incorrect.").show(childFragmentManager,"SIGN_IN_ERROR")
        }

    }

    fun startHomeActivity() {
        val mainActivityIntent = Intent(activity, HomeActivity::class.java)
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(mainActivityIntent)
    }

    override fun onSuccess(result: LoginResult?) {
        val accessToken = result?.accessToken
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        if (isLoggedIn) {
            Log.d("not error",isLoggedIn.toString())
        } else {
            Log.d("errorLog",isLoggedIn.toString())
        }

        val request = GraphRequest.newMeRequest(accessToken,object: GraphRequest.GraphJSONObjectCallback {
            override fun onCompleted(`object`: JSONObject?, response: GraphResponse?) {
                try {
                    val mail = `object`?.getString("email")
                    Log.i("email",mail!!)
                    email = mail
                } catch (e:Exception) {

                }
            }

        })
        val parameters = Bundle()
        parameters.putString("fields", "id,name,email,gender, birthday")
        request.parameters = parameters
        request.executeAsync()
    }

    override fun onCancel() {

    }

    override fun onError(error: FacebookException?) {
        Log.d("errorFb",error.toString())
    }

    fun showLoading() {
        signInButton.visibility = GONE
        signInLoading.visibility = VISIBLE
    }
    fun hideLoading() {
        signInButton.visibility = VISIBLE
        signInLoading.visibility = GONE
    }

    /*@SuppressLint("PackageManagerGetSignatures")
    fun generate() {
        try {
            val info = context?.packageManager?.getPackageInfo(
                "com.example.myparking",
                PackageManager.GET_SIGNATURES)
            for (signature in info?.signatures!!) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {

        } catch (e: NoSuchAlgorithmException) {

        }
    }*/

}
