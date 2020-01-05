package com.example.myparking.fragements

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myparking.models.Parking
import android.util.Log
import com.example.myparking.utils.DataSource
import com.example.myparking.activities.ParkingDetailsActivity
import com.example.myparking.R
import com.example.myparking.adapters.ListAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ParkingsList.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ParkingsList.newInstance] factory method to
 * create an instance of this fragment.
 */
class ParkingsList : Fragment(),  ListAdapter.OnItemClickListener{
    override fun OnItemClick(item: Any) {
        Log.d("PARKING INFO", (item as Parking).name)
        val intent = ParkingDetailsActivity.newIntent(
            this.activity as Context,
            item
        )
        startActivity(intent)
    }
    /* override fun OnParkingClick(parking: Parking) {
         Log.d("PARKING INFO", parking.name)
         val intent = ParkingDetailsActivity.newIntent(this.activity as Context, parking)
             startActivity(intent)
     }*/

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: ListAdapter.OnItemClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_parkings_list, container, false)
        initParkings(view)
        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {

    }

    fun initParkings(view: View) {
        var recyclerview = view.findViewById<RecyclerView>(R.id.parkings_list)
        recyclerview.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val adapter = ListAdapter(DataSource.getParkings(), this)
        recyclerview.adapter = adapter
        /*val adapter = ParkingsListAdapter(DataSource.getParkings(), this)
        recyclerview.adapter = adapter*/

    }

   /* override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ListAdapter.OnItemClickListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }*/

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ParkingsList.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ParkingsList().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
