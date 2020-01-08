package com.example.myparking.fragements

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.myparking.R


class FilterDialogFragment: DialogFragment()  {

    val TAG1 = "FilterDialogFragment"
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            val view = inflater.inflate(R.layout.filter_dialog, null)
            builder.setView(view)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }
}