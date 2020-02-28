package com.example.myparking.fragements

import com.example.myparking.databinding.FilterDialogBinding
import android.app.Dialog
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myparking.R

import com.example.myparking.adapters.MyAdapter
import com.example.myparking.adapters.ServiceAdapter
import com.example.myparking.models.Service
import com.example.myparking.utils.AnimationUtils
import net.cachapa.expandablelayout.ExpandableLayout
import android.view.View.FOCUS_DOWN
import android.widget.ScrollView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.appyvet.materialrangebar.RangeBar
import com.example.myparking.MainActivity
import com.example.myparking.models.FilterParkingsModel
import com.example.myparking.models.Parking
import com.example.myparking.repositories.ParkingListRepository
import com.example.myparking.viewmodels.ParkingListViewModel
import com.example.myparking.viewmodels.ParkingListViewModelFactory
import kotlinx.android.synthetic.main.filter_dialog.view.*


class FilterDialogFragment : DialogFragment(), Toolbar.OnMenuItemClickListener {

    val TAG1 = "FilterDialogFragment"
    private var toolbar: Toolbar? = null
    private lateinit var binding: FilterDialogBinding
/*    private var currentFilterState = FilterParkingsModel()*/
    private var currentFilterState = MutableLiveData<FilterParkingsModel>()
    private lateinit var mParkingListViewModel: ParkingListViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = DataBindingUtil.inflate(inflater, R.layout.filter_dialog, container, false)
        (activity as MainActivity)
        val factory = ParkingListViewModelFactory(ParkingListRepository.getInstance())
        mParkingListViewModel = ViewModelProviders.of(this.activity!!, factory)
            .get(ParkingListViewModel::class.java)
        /**/
        currentFilterState = mParkingListViewModel.getFilterState()
        /*mParkingListViewModel?.getParkingsList().observe(this, Observer<ArrayList<Parking>> {
            mParkingListViewModel?.receiveFilter(currentFilterState)
        })*/
        binding.lifecycleOwner = this.activity


        binding.root.distance_range_bar?.setOnRangeBarChangeListener(object :
            RangeBar.OnRangeBarChangeListener {
            override fun onTouchEnded(rangeBar: RangeBar?) {
            }

            override fun onRangeChangeListener(
                rangeBar: RangeBar?,
                leftPinIndex: Int,
                rightPinIndex: Int,
                leftPinValue: String?,
                rightPinValue: String?
            ) {
                currentFilterState?.value!!.maxDistance = rightPinValue?.toInt()
                currentFilterState?.value!!.minDistance = leftPinValue?.toInt()
            }

            override fun onTouchStarted(rangeBar: RangeBar?) {
            }

        })

        binding.root.price_range_bar?.setOnRangeBarChangeListener(object :
            RangeBar.OnRangeBarChangeListener {
            override fun onTouchEnded(rangeBar: RangeBar?) {
            }

            override fun onRangeChangeListener(
                rangeBar: RangeBar?,
                leftPinIndex: Int,
                rightPinIndex: Int,
                leftPinValue: String?,
                rightPinValue: String?
            ) {
                currentFilterState?.value!!.maxPrice = rightPinValue?.toInt()
                currentFilterState?.value!!.minPrice = leftPinValue?.toInt()
            }

            override fun onTouchStarted(rangeBar: RangeBar?) {
            }

        })
        binding.root.apply_filter_btn?.setOnClickListener {
            mParkingListViewModel?.receiveFilter(currentFilterState.value!!).observe(this, Observer<ArrayList<Parking>>
            {
                mParkingListViewModel?.postFilteredList(it)
                dismiss()
            })


        }
        binding.root.reset_filter_btn?.setOnClickListener {
            currentFilterState.value = FilterParkingsModel()
            // just incase he closes before applying
            mParkingListViewModel?.receiveFilter(currentFilterState.value!!).observe(this, Observer<ArrayList<Parking>>
            {
                mParkingListViewModel?.postFilteredList(it)
            })
        }
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

        val scroll_filters = view.findViewById<NestedScrollView>(R.id.scroll_filters)


        val check_distance = view.findViewById<CheckBox>(R.id.distance_check)
        val distance_container = view.findViewById<ExpandableLayout>(R.id.distance_container)
        if(!check_distance.isChecked && (currentFilterState.value!!.maxDistance !=null || currentFilterState.value!!.minDistance !=null)) {

            check_distance.toggle()
            distance_container.isExpanded =true
        }
        check_distance.setOnCheckedChangeListener { compoundButton, b ->
            distance_container.toggle()
        }
        val check_price = view.findViewById<CheckBox>(R.id.price_check)
        val price_container = view.findViewById<ExpandableLayout>(R.id.price_container)
        if(!check_price.isChecked && (currentFilterState.value!!.maxPrice !=null || currentFilterState.value!!.minPrice !=null)) {

            check_price.toggle()
            price_container.isExpanded =true
        }
        //AnimationUtils.collapse(price_container)
        check_price.setOnCheckedChangeListener { compoundButton, b ->
            price_container.toggle()
        }


        val services_list = view.findViewById<RecyclerView>(R.id.services_list)
        services_list.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        // bring real services
        val services = arrayListOf(
            Service("1","24/7", R.drawable.ic_timer),
            Service("2", "Caméra", R.drawable.ic_cctv),
            Service("3", "24/7", R.drawable.ic_timer),
            Service("4", "Caméra", R.drawable.ic_cctv),
            Service("5", "24/7", R.drawable.ic_timer)
        )
        if (currentFilterState.value!!.equipements !=null && currentFilterState.value!!.equipements !="") {
            val selectedIds = currentFilterState.value!!.equipements?.split(",")
            selectedIds?.forEach { id->
                val service = services.find{serv-> serv.id ==id}
                service?.checked = true
            }
        }
        val adapter = ServiceAdapter(services, object : MyAdapter.ItemAdapterListener<Service> {
            override fun onItemClicked(item: Service) {
                    var toConcat = ","+ item.id
                if (currentFilterState.value!!.equipements == null) {
                    currentFilterState.value!!.equipements =""
                    toConcat = item.id
                }

                currentFilterState.value!!.equipements = currentFilterState.value!!.equipements + ""+  toConcat
            }


        })
        services_list.adapter = adapter

        val check_service = view.findViewById<CheckBox>(R.id.service_check)
        val service_container = view.findViewById<ExpandableLayout>(R.id.service_container)
        if(!check_service.isChecked && (currentFilterState.value!!.equipements !=null )) {

            check_service.toggle()
            service_container.isExpanded =true
        }

        service_container.setOnExpansionUpdateListener { expansionFraction, state ->
            val expansionFr = state
            if (state == 3) {
                scroll_filters.smoothScrollTo(0, scroll_filters.bottom)
            }
        }

        check_service.setOnCheckedChangeListener { compoundButton, b ->
            service_container.toggle()
        }

        sort_toggle_price = view.findViewById<TextView>(R.id.toggle_price)
        sort_toggle_price.setOnClickListener {
            toggleSort(false)  //false for price
        }
        sort_toggle_distance = view.findViewById<TextView>(R.id.toggle_distance)
        sort_toggle_distance.setOnClickListener {
            toggleSort(true)  //true for distance
        }

        // range bar initial vals
        if(distance_container.isExpanded) {
            Log.d("currentmaxDistance", currentFilterState.value!!.maxDistance?.toString()!!)
            binding.root.distance_range_bar?.setRangePinsByValue(currentFilterState.value!!.minDistance?.toFloat()!!, currentFilterState.value!!.maxDistance?.toFloat()!!)
        }
        if(price_container.isExpanded) {
            Log.d("currentmaxPrice", currentFilterState.value!!.maxPrice?.toString()!!)
            binding.root.price_range_bar?.setRangePinsByValue(currentFilterState.value!!.minPrice?.toFloat()!!, currentFilterState.value!!.maxPrice?.toFloat()!!)

        }
      /*  if (service_container.isExpanded) {

        }*/

    }

    private lateinit var sort_toggle_price: TextView
    private lateinit var sort_toggle_distance: TextView

    fun toggleSort(b: Boolean) {
        val checkedV = if (b) {
            sort_toggle_distance
        } else {
            sort_toggle_price
        }
        val uncheckedV = if (b) {
            sort_toggle_price
        } else {
            sort_toggle_distance
        }
        val checkedIdRes = if (b) {
            R.drawable.checked_distance_btn
        } else {
            R.drawable.checked_price_btn
        }
        val uncheckedIdRes = if (b) {
            R.drawable.unchecked_price_btn
        } else {
            R.drawable.unchecked_distance_btn
        }
        checkedV.setTextColor(ContextCompat.getColor(context!!, R.color.white))
        checkedV.setBackgroundResource(checkedIdRes)

        uncheckedV.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
        uncheckedV.setBackgroundResource(uncheckedIdRes)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.dismiss_btn -> {
                dismiss()
            }
            else -> {
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
            dialog.window!!.statusBarColor =
                ContextCompat.getColor(context!!, R.color.colorPrimaryDark)
            dialog.window!!.decorView.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
    }
}