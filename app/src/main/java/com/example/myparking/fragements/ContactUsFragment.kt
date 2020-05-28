package com.example.myparking.fragements


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.myparking.R
import com.example.myparking.repositories.ContactUsRepository
import com.example.myparking.repositories.NotificationsRepository
import com.example.myparking.utils.PreferenceManager
import com.example.myparking.viewmodels.ContactUsViewModel
import com.example.myparking.viewmodels.ContactUsViewModelFactory
import com.example.myparking.viewmodels.NotificationViewModel
import com.example.myparking.viewmodels.NotificationViewModelFactory
import kotlinx.android.synthetic.main.contact_us_layout.*

/**
 * A simple [Fragment] subclass.
 */
class ContactUsFragment : Fragment() {

    private lateinit var contactUsViewModel: ContactUsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.contact_us_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val automobilisteId = PreferenceManager(context!!).checkDriverProfile().toInt()
        val factory = ContactUsViewModelFactory(automobilisteId, ContactUsRepository.getInstance())
        contactUsViewModel = ViewModelProviders.of(this.activity!!, factory)
            .get(ContactUsViewModel::class.java)

        contact_obj.addTextChangedListener(Listener(contactUsViewModel.objetTxt))
        contact_msg.addTextChangedListener(Listener(contactUsViewModel.msgTxt))
        contactUsViewModel.objetTxt.observe(this, Observer<String> {
            checkInputs()
        })
        contactUsViewModel.msgTxt.observe(this, Observer<String> {
            checkInputs()
        })
        contac_send_btn.setOnClickListener {
            showLoading()
            contactUsViewModel.sendMessage().observe(this, Observer<Pair<Boolean,String>> {
                if (it!=null){
                    hideLoading()
                    if (it.first) {
                        InformationDialog(InformationDialog.Companion.TYPE.SUCCESS, it.second).show(childFragmentManager,"SENT")
                    } else {
                        InformationDialog(InformationDialog.Companion.TYPE.ERROR, it.second).show(childFragmentManager,"SEND_ERROR")
                    }
                }
            })
        }
    }



    private fun showLoading() {
        contact_loading.visibility = VISIBLE
        contac_send_btn.visibility = GONE
    }
    private fun hideLoading() {
        contac_send_btn.visibility = VISIBLE
        contact_loading.visibility = GONE
    }

    fun checkInputs() {
        val objet = contactUsViewModel.objetTxt.value
        val msg = contactUsViewModel.msgTxt.value
        contac_send_btn.isEnabled = objet!=null && msg!=null && objet!="" && msg!=""
    }

    private inner class Listener(val liveData:MutableLiveData<String>) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            liveData.postValue(s.toString())
        }

    }
}
