package com.example.myparking.fragements

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myparking.R
import com.example.myparking.models.NotificationModel
import com.example.myparking.repositories.NotificationsRepository
import com.example.myparking.utils.NotificationsListAdapter
import com.example.myparking.viewmodels.NotificationViewModel
import com.example.myparking.viewmodels.NotificationViewModelFactory
import kotlinx.android.synthetic.main.fragment_notifications.*
import com.google.android.material.snackbar.Snackbar




class NotificationsFragment : Fragment(), NotificationsListAdapter.OnNotifClickListener {


    private lateinit var notificationViewModel: NotificationViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = NotificationViewModelFactory(NotificationsRepository.getInstance(context!!))
        notificationViewModel = ViewModelProviders.of(this.activity!!, factory)
            .get(NotificationViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Notifications"
        val adapter = NotificationsListAdapter(ArrayList(), this)
        notifs_list.layoutManager = LinearLayoutManager(context!!,RecyclerView.VERTICAL,false)
        notifs_list.adapter = adapter
        notificationViewModel.getAllNotifs().observe(activity!!, Observer<ArrayList<NotificationModel>>{
            Log.d("RECEIVED_NEW_notif","RECEIVE")
            if (it==null || it.size==0) {
                no_notif_msg.visibility = VISIBLE
            } else {
                no_notif_msg.visibility = GONE
            }
            adapter.updateList(it)
        })

    }

    override fun onDelete(notif: NotificationModel) {
        val temp = notif.copy(id_notif = 0)
        notificationViewModel.deleteNotif(notif)
        val rootView = activity?.window?.decorView?.findViewById<View>(android.R.id.content)
        val snackbar = Snackbar
            .make(rootView!!, "Notification supprimée.", Snackbar.LENGTH_LONG)
        snackbar.setAction("ANNULER") {
            notificationViewModel.undoDelete(temp)
            snackbar.dismiss()
        }
        snackbar.show()
    }

    override fun onRead(notif: NotificationModel) {
        notificationViewModel.readNotif(notif)
    }








}