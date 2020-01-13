package com.example.myparking.fragements

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.example.myparking.R
import com.example.myparking.utils.AnimationUtils


class FilterDialogFragment: DialogFragment(), Toolbar.OnMenuItemClickListener {

    val TAG1 = "FilterDialogFragment"
    private var toolbar: Toolbar? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState);
        val view = inflater.inflate(R.layout.filter_dialog,  container, false)
        toolbar = view.findViewById<Toolbar>(R.id.filter_toolbar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar?.let {
            it.setNavigationOnClickListener { v -> dismiss() }
            it.setTitle("Some Title")
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

    }
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStart() {
        super.onStart()
        super.onStart()
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.setWindowAnimations(R.style.Slide)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
    }
}