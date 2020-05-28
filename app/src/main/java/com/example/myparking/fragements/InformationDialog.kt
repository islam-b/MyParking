package com.example.myparking.fragements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColorStateList
import androidx.fragment.app.DialogFragment
import com.example.myparking.R


class InformationDialog(val type:TYPE, val message:String) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.info_dialog, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var title = ""
        var icon = 0
        var bgColor = 0
        when(type) {
            TYPE.INFORMATION -> {
                icon = R.drawable.ic_info_outline_white
                bgColor = R.color.information
                title= "Information"
            }
            TYPE.WARNING -> {
                icon = R.drawable.ic_warning_whit
                bgColor = R.color.warning
                title= "Avertissement"
            }
            TYPE.ERROR -> {
                icon = R.drawable.ic_error_white
                bgColor = R.color.error
                title= "Echec"
            }
            TYPE.SUCCESS -> {
                icon = R.drawable.ic_done_white
                bgColor = R.color.success
                title= "Succées"
            }
        }
        view.findViewById<ImageView>(R.id.dialog_indicator).backgroundTintList = getColorStateList(context!!,bgColor)
        view.findViewById<Button>(R.id.dialog_button).backgroundTintList = getColorStateList(context!!,bgColor)
        view.findViewById<ImageView>(R.id.dialog_indicator_icon).setImageResource(icon)
        view.findViewById<TextView>(R.id.dialog_title).text = title
        view.findViewById<TextView>(R.id.dialog_msg).text = message
        view.findViewById<Button>(R.id.dialog_button).setOnClickListener {
            dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.InfoDialog)
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
        }
    }

    companion object {
        enum class TYPE {
            INFORMATION,
            WARNING,
            ERROR,
            SUCCESS
        }
    }
}