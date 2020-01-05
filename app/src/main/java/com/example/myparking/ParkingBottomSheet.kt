package com.example.myparking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

private const val ARG_PARAM1 = "param1"
private const val TAG = "ActionBottomDialog"
class ParkingBottomSheet : BottomSheetDialogFragment() {


    private var mListener: ItemClickListener? = null
    private lateinit var title: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.parking_bottom_sheet, container, false)

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.MyBottomSheetDialogTheme)
        arguments?.let {
            title=it.getString(ARG_PARAM1)!!
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.title).text = title
        //view.findViewById(R.id.textView).setOnClickListener(this)
    }



//    fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is ItemClickListener) {
//            mListener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement ItemClickListener")
//        }
//    }

//    override fun onDetach() {
//        super.onDetach()
//        mListener = null
//    }

//    fun onClick(view: View) {
//        val tvSelected = view as TextView
//        mListener!!.onItemClick(tvSelected.getText().toString())
//        dismiss()
//    }

    interface ItemClickListener {
        fun onItemClick(item: String)
    }

    companion object {

        @JvmStatic
        fun newInstance(p: ParkingModel) = ParkingBottomSheet().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1,p.name)
            }
        }
    }
}