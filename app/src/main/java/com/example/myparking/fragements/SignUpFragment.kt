package com.example.myparking.fragements

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.example.myparking.HomeActivity
import com.example.myparking.MainActivity
import com.example.myparking.R
import com.example.myparking.models.DriverProfile
import com.example.myparking.models.SignInModelRequest
import com.example.myparking.models.SignInModelResponse
import com.example.myparking.models.SignUpModelRequest
import com.example.myparking.services.AuthService
import com.example.myparking.utils.InjectorUtils
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_sign_in.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern
import android.text.TextUtils.isDigitsOnly



class SignUpFragment : Fragment(), Callback<Any> {

    private lateinit var signUpButton : Button
    private lateinit var signUpLoading: ProgressBar
    private lateinit var service: AuthService
    private lateinit var username: String
    private lateinit var password: String
    private lateinit var validationHelper: ValidationHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)
        validationHelper = ValidationHelper()
        signUpButton = view.findViewById(R.id.singnup_btn)
        signUpLoading = view.findViewById(R.id.signUpLoading)
        signUpButton.setOnClickListener {
            signUp(view)
        }

//        view.findViewById<Button>(R.id.create_account)?.setOnClickListener{
//            val mainActivityIntent = Intent(activity, MainActivity::class.java)
//            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(mainActivityIntent)
//        }

        view.findViewById<Button>(R.id.sign_in)?.setOnClickListener {
            val fragment = SignInFragment()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.login_container, fragment)?.commit()
        }

        return view
    }

    fun validateForm(view: View):Boolean {
        val emailInput = view.findViewById<TextInputEditText>(R.id.user_mail)
        val numTelInput = view.findViewById<TextInputEditText>(R.id.user_num)
        val passwordInput = view.findViewById<TextInputEditText>(R.id.user_password)
        val passwordConfInput = view.findViewById<TextInputEditText>(R.id.user_password_conf)
        val lastNameInput = view.findViewById<TextInputEditText>(R.id.textInputEditText)
        val firstNameInput = view.findViewById<TextInputEditText>(R.id.user_fname)
        val nom = lastNameInput.text.toString()
        if (validationHelper.isNullOrEmpty(nom)) {
            lastNameInput.error = "Nom manquant"
            return false
        } else {
            lastNameInput.error = null
        }
        val prenom = firstNameInput.text.toString()
        if (validationHelper.isNullOrEmpty(prenom)) {
            firstNameInput.error = "Prenom manquant"
            return false
        } else {
            firstNameInput.error = null
        }
        val email = emailInput.text.toString()
        if (validationHelper.isNullOrEmpty(email) || !validationHelper.isValidEmail(email)) {
            emailInput.error = "Adresse mail non valide"
            return false
        } else {
            emailInput.error = null
        }
        val numTel = numTelInput.text.toString()
        if (validationHelper.isNullOrEmpty(numTel) || !validationHelper.isNumeric(numTel)) {
            numTelInput.error = "Numéro non valide"
            return false
        }else {
            numTelInput.error = null
        }
        password = passwordInput.text.toString()
        if (validationHelper.isNullOrEmpty(password)) {
            passwordInput.error = "Mot de passe manquant"
            return false
        } else {
            passwordInput.error = null
        }
        val password_conf = passwordConfInput.text.toString()
        if (validationHelper.isNullOrEmpty(password_conf) || password!==password_conf) {
            passwordConfInput.error = "Le mot de passe ne correnspond pas"
            return false
        } else {
            passwordConfInput.error = null
        }
        username = email
        return true
//        val username = view.findViewById<TextInputEditText>(R.id.user_name).text.toString()


    }

    fun signUp(view: View) {
        if (validateForm(view)) {
            val email = view.findViewById<TextInputEditText>(R.id.user_mail).text.toString()
            val numTel = view.findViewById<TextInputEditText>(R.id.user_num).text.toString()
//        val username = view.findViewById<TextInputEditText>(R.id.user_name).text.toString()
            username = email
            password = view.findViewById<TextInputEditText>(R.id.user_password).text.toString()
            val password_conf = view.findViewById<TextInputEditText>(R.id.user_password_conf).text.toString()
            val nom = view.findViewById<TextInputEditText>(R.id.textInputEditText).text.toString()
            val prenom = view.findViewById<TextInputEditText>(R.id.user_fname).text.toString()

            service = InjectorUtils.provideAuthService()
            showLoading()
            service.signUp(SignUpModelRequest(
                username, password, email, DriverProfile(null,nom,numTel,prenom,null,null)
            )).enqueue(this)
        }
    }

    override fun onFailure(call: Call<Any>, t: Throwable) {
        hideLoading()
        SignInErrorDialog("Une erreur s\'est produite, veuillez réesayez").show(childFragmentManager,"SIGN_UP_ERROR")
    }

    override fun onResponse(call: Call<Any>, response: Response<Any>) {
        if (response.code()==201) {
            service.signIn(SignInModelRequest(username,password)).enqueue(object: Callback<SignInModelResponse> {
                override fun onFailure(call: Call<SignInModelResponse>, t: Throwable) {
                    hideLoading()
                    SignInErrorDialog("Une erreur s\'est produite, veuillez réesayez").show(childFragmentManager,"SIGN_IN_ERROR")
                }

                override fun onResponse(
                    call: Call<SignInModelResponse>,
                    response: Response<SignInModelResponse>
                ) {
                    if (response.code()==200) {
                        /* val id = response.body()?.driverProfile?.idAutomobiliste.toString()
                         PreferenceManager(context!!).writeDriverId(id)*/
                        val mainActivityIntent = Intent(activity, HomeActivity::class.java)
                        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(mainActivityIntent)
                    } else {
                        hideLoading()
                        SignInErrorDialog("Nom d'utilisateur ou mot de passe incorrect.").show(childFragmentManager,"SIGN_IN_ERROR")
                    }
                }


            })
        } else {
            hideLoading()
            SignInErrorDialog("Une erreur s\'est produite, veuillez réesayez").show(childFragmentManager,"SIGN_UP_ERROR")
        }
    }


    fun showLoading() {
        signUpButton.visibility = View.GONE
        signUpLoading.visibility = View.VISIBLE
    }
    fun hideLoading() {
        signUpButton.visibility = View.VISIBLE
        signUpLoading.visibility = View.GONE
    }


    inner class ValidationHelper {
        fun isValidEmail(string: String): Boolean {
            val EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
            val pattern = Pattern.compile(EMAIL_PATTERN)
            val matcher = pattern.matcher(string)
            return matcher.matches()
        }
        fun isValidPassword(string: String, allowSpecialChars: Boolean): Boolean {
            val PATTERN: String
            if (allowSpecialChars) {
                //PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
                PATTERN = "^[a-zA-Z@#$%]\\w{5,19}$"
            } else {
                //PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";
                PATTERN = "^[a-zA-Z]\\w{5,19}$"
            }


            val pattern = Pattern.compile(PATTERN)
            val matcher = pattern.matcher(string)
            return matcher.matches()
        }
        fun isNullOrEmpty(string: String): Boolean {
            return TextUtils.isEmpty(string)
        }

        fun isNumeric(string: String): Boolean {
            return isDigitsOnly(string)
        }

    }


}
