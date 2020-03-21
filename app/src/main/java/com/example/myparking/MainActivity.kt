package com.example.myparking

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CursorAdapter
import android.widget.ListAdapter
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.myparking.activities.FavoriteParkingsActivity
import com.example.myparking.activities.MesReservationsActivity
import com.example.myparking.adapters.OnSearchListener
import com.example.myparking.fragements.FilterDialogFragment
import com.example.myparking.fragements.ParkingsList
import com.example.myparking.fragements.ParkingsMap
import com.example.myparking.models.ParkingModel


import com.example.myparking.models.SearchModel
import com.example.myparking.models.SearchResult
import com.example.myparking.utils.MapsUtils
import com.example.myparking.utils.NetworkReceiver
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.luseen.spacenavigation.SpaceItem
import com.luseen.spacenavigation.SpaceNavigationView
import com.luseen.spacenavigation.SpaceOnClickListener
import com.google.android.libraries.places.widget.AutocompleteActivity
/**
 * Main activity
 * This activity contains two view: Map view and List view*
 * @author BOUAYACHE
 * @property drawer the drawer layout of tha main activity
 * @property toggle the Toggle action of the drawer layout
 * @property currentItem the current index of the view (0 or 1)
 * @property networkReceiver the Broadcast Receiver, it receives the connectivity state
 */

class MainActivity : Fragment(),SpaceOnClickListener {



    private var currentItem = LIST_VIEW
    private lateinit var spaceNavigationView : SpaceNavigationView
    private lateinit var rootView: View



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.activity_main2,container,false)

        spaceNavigationView = rootView.findViewById(R.id.nav_view)
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState)
        spaceNavigationView.addSpaceItem(SpaceItem(LIST_VIEW,LIST_VIEW_NAME, R.drawable.list_view))
        spaceNavigationView.addSpaceItem(SpaceItem(MAP_VIEW,MAP_VIEW_NAME, R.drawable.map_view))
        spaceNavigationView.setCentreButtonIconColorFilterEnabled(false)
        spaceNavigationView.setSpaceOnClickListener(this)
        //spaceNavigationView.changeCurrentItem(LIST_VIEW)


        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spaceNavigationView.changeCurrentItem(currentItem)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentItem = arguments?.getInt("viewType")!!
//        bundle?.let {
//            bundle.apply {
//                currentItem = getInt("CURRENT_VIEW")
//                Log.d("curreenTTT", currentItem.toString())
//            }
//        }
    }


    /**
     * This function opens the filter dialog, it is triggered when the user click on the filter button
     */
    override fun onCentreButtonClick() {
        val dialog = FilterDialogFragment()
        dialog.show(childFragmentManager, dialog.TAG1)

    }



    /**
     * This function opens one of the views (list or map) depending on the clicked item
     * @param itemIndex the index of the view selected
     * @param itemName the name (tag) of the view selected
     */
    override fun onItemClick(itemIndex: Int, itemName: String?) {
        var fragment: Fragment

        Log.d("item ",itemIndex.toString())
        when (itemName) {
            LIST_VIEW_NAME-> {
                Log.d("item clicked","item 0")
                fragment = ParkingsList()
                currentItem = itemIndex
            }
            MAP_VIEW_NAME-> {
                val actionType = arguments?.getInt("actionType")
                val data = arguments?.get("data")
                fragment = ParkingsMap(actionType, data)
                currentItem = itemIndex
            }
            else -> {
                fragment = ParkingsList()
                currentItem = itemIndex
            }
        }
        childFragmentManager.beginTransaction().replace(R.id.nav_host,fragment).commit()
    }



    override fun onItemReselected(itemIndex: Int, itemName: String?) {
        onItemClick(itemIndex,itemName)
    }



    companion object {
        val LIST_VIEW=0
        val LIST_VIEW_NAME="Liste"
        val MAP_VIEW=1
        val MAP_VIEW_NAME="Carte"
    }

}
