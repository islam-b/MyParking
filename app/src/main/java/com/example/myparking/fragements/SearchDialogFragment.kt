package com.example.myparking.fragements

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myparking.R
import com.example.myparking.adapters.OnSearchListener
import com.example.myparking.adapters.SearchAdapter
import com.example.myparking.models.SearchModel
import com.example.myparking.models.SearchResult
import com.example.myparking.services.ParkingService
import com.example.myparking.utils.InjectorUtils
import kotlinx.android.synthetic.main.search_dialog_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchDialogFragment(val listener: OnSearchListener): DialogFragment(), OnSearchListener {

    override fun onSearchClick(searchResult: SearchResult) {
        dismiss()
        listener.onSearchClick(searchResult)
    }

    val TAG1 = "SearchDialogFragment"
    lateinit var service: ParkingService
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.search_dialog_layout,container,false)
        return view
    }
    private var toolbar: Toolbar? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar = view.findViewById<Toolbar>(R.id.search_toolbar)
        toolbar?.let {
            //            it.setTitle("Some Title")
            //it.inflateMenu(R.menu.filter_dialog_menu)
            it.setNavigationOnClickListener { v -> dismiss() }
        }

        var searchView=view.findViewById<SearchView>(R.id.search_view)
        val searchViewIcon =
            searchView.findViewById(androidx.appcompat.R.id.search_mag_icon) as ImageView
        val linearLayoutSearchView = searchViewIcon.parent as ViewGroup
        linearLayoutSearchView.removeView(searchViewIcon)
        recyclerView = view.findViewById<RecyclerView>(R.id.search_list)
        recyclerView.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        loading.visibility = GONE
        val dialog = this
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            Callback<SearchModel> {


            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                recyclerView.adapter = null
                if(newText!==null && newText.length>0) {
                    service.searchPlaces(newText).enqueue(this)
                    loading.visibility = VISIBLE
                }

                return false
            }
            override fun onFailure(call: Call<SearchModel>, t: Throwable) {
                Log.d("errorSearch",t.printStackTrace().toString())
                loading?.visibility = GONE
            }

            override fun onResponse(call: Call<SearchModel>, response: Response<SearchModel>) {
                loading?.visibility = GONE
                val list = response.body()?.results!!
                list.let {
                    recyclerView.adapter = SearchAdapter(it,dialog)
                }
            }

        })

    }
    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
            dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            dialog.window!!.statusBarColor = ContextCompat.getColor(context!!,R.color.colorPrimaryDark)
            dialog.window!!.decorView.systemUiVisibility  = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        service = InjectorUtils.provideParkingService()
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog)
    }
}