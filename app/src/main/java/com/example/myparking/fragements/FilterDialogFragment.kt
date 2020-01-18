package com.example.myparking.fragements

import com.example.myparking.databinding.FilterDialogBinding
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myparking.R

import com.example.myparking.adapters.MyAdapter
import com.example.myparking.adapters.ServiceAdapter
import com.example.myparking.models.Service
import com.example.myparking.utils.AnimationUtils


class FilterDialogFragment: DialogFragment(), Toolbar.OnMenuItemClickListener {

    val TAG1 = "FilterDialogFragment"
    private var toolbar: Toolbar? = null
    private lateinit var  binding: FilterDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = DataBindingUtil.inflate(inflater,R.layout.filter_dialog,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar = view.findViewById<Toolbar>(R.id.filter_toolbar)
        toolbar?.let {
//            it.setNavigationOnClickListener { v -> dismiss() }
//            it.setTitle("Some Title")
            it.inflateMenu(R.menu.filter_dialog_menu)
            it.setOnMenuItemClickListener(this)
        }
        val check_distance= view.findViewById<CheckBox>(R.id.distance_check)
        val distance_container= view.findViewById<ConstraintLayout>(R.id.distance_container)
        AnimationUtils.collapse(distance_container)
        check_distance.setOnCheckedChangeListener { compoundButton, b ->
            if(b) AnimationUtils.expand(distance_container)
            else AnimationUtils.collapse(distance_container)
        }
        val check_price= view.findViewById<CheckBox>(R.id.price_check)
        val price_container= view.findViewById<ConstraintLayout>(R.id.price_container)
        AnimationUtils.collapse(price_container)
        check_price.setOnCheckedChangeListener { compoundButton, b ->
            if(b) AnimationUtils.expand(price_container)
            else AnimationUtils.collapse(price_container)
        }


        val services_list = view.findViewById<RecyclerView>(R.id.services_list)
        services_list.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        val services = arrayListOf(
            Service("24/7", R.drawable.ic_timer),
            Service("Caméra", R.drawable.ic_cctv),
            Service("24/7", R.drawable.ic_timer),
            Service("Caméra", R.drawable.ic_cctv),
            Service("24/7", R.drawable.ic_timer)
        )
        val adapter = ServiceAdapter(services, object : MyAdapter.ItemAdapterListener<Service> {
            override fun onItemClicked(item: Service) {
                Log.d("AM ", "BLA BLA HERE TO HANDLE CHECK EVENT")
            }


        })
        services_list.adapter = adapter

        val check_service= view.findViewById<CheckBox>(R.id.service_check)
        val service_container= view.findViewById<LinearLayout>(R.id.service_container2)
        //AnimationUtils.collapse(service_container)
        check_service.setOnCheckedChangeListener { compoundButton, b ->
            if(b) {
                AnimationUtils.expand(service_container)
                val services_list = view.findViewById<RecyclerView>(R.id.services_list)
                services_list.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                services_list.adapter = adapter
            }
            else AnimationUtils.collapse(service_container)
        }

    }
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.dismiss_btn -> {
                dismiss()
            }
            else->{
                return false
            }
        }
        return true
    }


    override fun onStart() {
        super.onStart()
        super.onStart()
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.setWindowAnimations(R.style.Slide)
            dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            dialog.window!!.statusBarColor = ContextCompat.getColor(context!!,R.color.colorPrimaryDark)
            dialog.window!!.decorView.systemUiVisibility  = SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
    }
}