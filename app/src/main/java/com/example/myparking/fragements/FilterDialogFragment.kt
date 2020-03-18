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
import com.example.myparking.utils.AnimationUtils
import net.cachapa.expandablelayout.ExpandableLayout
import android.view.View.FOCUS_DOWN
import android.widget.ScrollView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.appyvet.materialrangebar.RangeBar
import com.example.myparking.MainActivity
import com.example.myparking.models.*
import com.example.myparking.repositories.ParkingListRepository
import com.example.myparking.viewmodels.FilterParkingsViewModel
import com.example.myparking.viewmodels.ParkingListViewModel
import com.example.myparking.viewmodels.ParkingListViewModelFactory
import kotlinx.android.synthetic.main.filter_dialog.view.*



class FilterDialogFragment : DialogFragment(), Toolbar.OnMenuItemClickListener {

    val TAG1 = "FilterDialogFragment"
    private var toolbar: Toolbar? = null
    private lateinit var binding: FilterDialogBinding
/*    private var currentFilterState = FilterParkingsModel()*/
    private lateinit var currentFilterState : FilterParkingsViewModel
    private lateinit var mParkingListViewModel: ParkingListViewModel
    private lateinit var serviceAdapter: ServiceAdapter
    private var arrayEquipements=  ArrayList<Int>()
    private var filterInfoInitial = FilterInfoResponse()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = DataBindingUtil.inflate(inflater, R.layout.filter_dialog, container, false)
        //(activity as MainActivity)
        val factory = ParkingListViewModelFactory(ParkingListRepository.getInstance())
        currentFilterState = ViewModelProviders.of(this.activity!!).get(FilterParkingsViewModel::class.java)

        currentFilterState.getFilterMainInfo().observe(this, Observer<FilterInfoResponse> {
            // binding.root.distance_range_bar?.tickStart = it?.distance?.min?.toFloat()!!
            serviceAdapter?.updateList(it?.equipements!!)
            Log.d("equipements", it?.equipements?.get(0)?.designation+"bbala")
            binding.filterInfo = it!!
        })
       // binding.filterInfo = filterInfoInitial
        mParkingListViewModel = ViewModelProviders.of(this.activity!!, factory)
            .get(ParkingListViewModel::class.java)





        binding.filterViewModel = currentFilterState


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
                val currentState = currentFilterState.getFilterParkingsState().value!!

                val newState = FilterParkingsModel(currentState.minPrice, currentState.maxPrice,currentState.equipements, leftPinValue?.toInt(), rightPinValue?.toInt()  )
                currentFilterState.postFilterParkingsState(newState)

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
                val currentState = currentFilterState.getFilterParkingsState().value!!
                val newState = FilterParkingsModel(leftPinValue?.toInt(), rightPinValue?.toInt() ,currentState.equipements, currentState.minDistance, currentState.maxDistance )
                currentFilterState.postFilterParkingsState(newState)

            }

            override fun onTouchStarted(rangeBar: RangeBar?) {
            }

        })
        binding.root.apply_filter_btn?.setOnClickListener {
            mParkingListViewModel?.receiveFilter(currentFilterState.getFilterParkingsState().value!!).observe(this, Observer<ArrayList<Parking>>
            {
                mParkingListViewModel?.postFilteredList(it)
                dismiss()
            })


        }
        binding.root.reset_filter_btn?.setOnClickListener {
            currentFilterState.postFilterParkingsState(FilterParkingsModel())
            binding.root.price_range_bar.setRangePinsByValue(binding.root.price_range_bar.tickStart, binding.root.price_range_bar.tickEnd)
            binding.root.distance_range_bar.setRangePinsByValue(binding.root.distance_range_bar.tickStart, binding.root.distance_range_bar.tickEnd)
            binding.root.price_check.isChecked = false
            binding.root.distance_check.isChecked = false
            binding.root.service_check.isChecked =false

            serviceAdapter?.updateList(filterInfoInitial.equipements!!)
            // just incase he closes before applying
            mParkingListViewModel?.receiveFilter(FilterParkingsModel()).observe(this, Observer<ArrayList<Parking>>
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
        distance_container.isExpanded = currentFilterState.distanceIsChecked()
        check_distance.setOnCheckedChangeListener { compoundButton, b ->
           /* check_distance.isChecked = currentFilterState.distanceIsChecked()*/
            distance_container.toggle()
        }
        val check_price = view.findViewById<CheckBox>(R.id.price_check)
        val price_container = view.findViewById<ExpandableLayout>(R.id.price_container)
        price_container.isExpanded = currentFilterState.priceIsChecked()
        if(currentFilterState.priceIsChecked()) {
            check_price.toggle()
            check_price.isChecked = currentFilterState.priceIsChecked()
        }

        //AnimationUtils.collapse(price_container)
        check_price.setOnCheckedChangeListener { compoundButton, b ->
            price_container.toggle()
        }


        val services_list = view.findViewById<RecyclerView>(R.id.services_list)
        services_list.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        // bring real services
        val services : ArrayList<Equipement> = arrayListOf()

        /* to improve **/
        if (currentFilterState.getFilterParkingsState().value!!.equipements !=null && currentFilterState.getFilterParkingsState().value!!.equipements !="") {
            val selectedIds = currentFilterState.getFilterParkingsState().value!!.equipements?.split(",")
            services.forEach{serv -> serv.checked = false}
            selectedIds?.forEach { id->
                val service = services.find{serv-> serv.idEquipement ==id}
                service?.checked = true
            }
        }
       serviceAdapter = ServiceAdapter(services, object : MyAdapter.ItemAdapterListener<Equipement> {
            override fun onItemClicked(item: Equipement) {
                // add case click to remove
                val checkedService = item.checked
                if(!checkedService) {
                    arrayEquipements.remove(item.idEquipement.toInt())
                } else arrayEquipements.add(item.idEquipement.toInt())
                var toPost: String? = arrayEquipements.joinToString(separator = ",")
                Log.d("TOPOSTT", toPost!!)
                if (toPost=="") toPost=null
                val currentState = currentFilterState.getFilterParkingsState().value!!
                currentFilterState.postFilterParkingsState(FilterParkingsModel(currentState.minPrice, currentState.maxPrice, toPost,currentState.minDistance, currentState.maxDistance ))

            }


        })
        services_list.adapter = serviceAdapter

        val check_service = view.findViewById<CheckBox>(R.id.service_check)
        val service_container = view.findViewById<ExpandableLayout>(R.id.service_container)
        service_container.isExpanded = currentFilterState.serviceIsChecked()
        if(currentFilterState.serviceIsChecked()) {
            check_service.toggle()
            check_service.isChecked = currentFilterState.serviceIsChecked()
        }
       /* if(!check_service.isChecked && (currentFilterState.value!!.equipements !=null )) {

            check_service.toggle()
            service_container.isExpanded =true
        }*/

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
            mParkingListViewModel.sortByPrice()
        }
        sort_toggle_distance = view.findViewById<TextView>(R.id.toggle_distance)
        sort_toggle_distance.setOnClickListener {
            toggleSort(true)  //true for distance
            mParkingListViewModel.sortByDistance()
        }

        // range bar initial vals
        val currentState = currentFilterState.getFilterParkingsState().value!!
        if(distance_container.isExpanded) {
           /* Log.d("currentmaxDistance", currentFilterState.value!!.maxDistance?.toString()!!)*/
            binding.root.distance_range_bar?.setRangePinsByValue(currentState.minDistance?.toFloat()!!, currentState.maxDistance?.toFloat()!!)
        }
        if(price_container.isExpanded) {
         /*   Log.d("currentmaxPrice", currentFilterState.value!!.maxPrice?.toString()!!)*/
            binding.root.price_range_bar?.setRangePinsByValue(currentState.minPrice?.toFloat()!!, currentState.maxPrice?.toFloat()!!)

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

            dialog!!.window!!.setLayout(width, height)
            dialog!!.window!!.setWindowAnimations(R.style.Slide)
            dialog!!.window!!.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            dialog!!.window!!.statusBarColor = ContextCompat.getColor(context!!,R.color.colorPrimaryDark)
            dialog!!.window!!.decorView.systemUiVisibility  = SYSTEM_UI_FLAG_LAYOUT_STABLE

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
    }
}