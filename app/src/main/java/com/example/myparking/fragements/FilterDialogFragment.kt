package com.example.myparking.fragements

import com.example.myparking.databinding.FilterDialogBinding
import android.app.Dialog
import android.content.DialogInterface
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
import com.example.myparking.utils.PreferenceManager
import com.example.myparking.viewmodels.FilterParkingViewModelFactory
import com.example.myparking.viewmodels.FilterParkingsViewModel
import com.example.myparking.viewmodels.ParkingListViewModel
import com.example.myparking.viewmodels.ParkingListViewModelFactory
import kotlinx.android.synthetic.main.filter_dialog.*
import kotlinx.android.synthetic.main.filter_dialog.view.*


class FilterDialogFragment : DialogFragment(), Toolbar.OnMenuItemClickListener {

    val TAG1 = "FilterDialogFragment"
    private var toolbar: Toolbar? = null
    private lateinit var binding: FilterDialogBinding
    /*    private var currentFilterState = FilterParkingsModel()*/
    private lateinit var currentFilterState: FilterParkingsViewModel
    private lateinit var mParkingListViewModel: ParkingListViewModel
    private lateinit var serviceAdapter: ServiceAdapter
    private var arrayEquipements = ArrayList<Int>()
//    private var filterInfoInitial = FilterInfoResponse()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState);
//        PreferenceManager(context!!).destroyFiltersInit()
        val prefManager = PreferenceManager(context!!)
        val idDriver = prefManager.checkDriverProfile().toInt()
        binding = DataBindingUtil.inflate(inflater, R.layout.filter_dialog, container, false)
        //(activity as MainActivity)
        val factory = ParkingListViewModelFactory(ParkingListRepository.getInstance(),idDriver,prefManager)
        val filterVMFactory = FilterParkingViewModelFactory(idDriver,prefManager)
        currentFilterState =
            ViewModelProviders.of(this.activity!!,filterVMFactory).get(FilterParkingsViewModel::class.java)
        val filtersStored = PreferenceManager(context!!).getFilterInitialInfo()
        currentFilterState.postFilterParkingsState(filtersStored)
        Log.d("readFilterState", currentFilterState.getFilterParkingsState().value!!.toString())

        binding.root.price_range_bar.tickEnd = currentFilterState.getFilterMainInfo().value!!.getPriceMax()
        binding.root.distance_range_bar.tickEnd = currentFilterState.getFilterMainInfo().value!!.getDistanceMax()

        mParkingListViewModel = ViewModelProviders.of(this.activity!!, factory)
            .get(ParkingListViewModel::class.java)
        // apply filter stored in initial filter sotred
        mParkingListViewModel.getFilteredParkings(currentFilterState.getFilterParkingsState().value!!)
            .observe(viewLifecycleOwner, Observer<ArrayList<Parking>> { parkings ->
                /*var sortedParkings =
                    ArrayList(parkings.sortedWith(compareBy { it.routeInfo?.walkingDistance }))
                val sort = currentFilterState.getFilterParkingsState().value!!.sort
                if (sort == 2) sortedParkings =
                    ArrayList(parkings.sortedWith(compareBy { it.tarifs?.get(0).prix }))
                mParkingListViewModel?.postFilteredList(sortedParkings)*/

            })
        // set values in view
        binding.filterInfo = filtersStored







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
                currentState.minDistance = leftPinValue?.toInt()
                currentState.maxDistance = rightPinValue?.toInt()

//                val newState = FilterParkingsModel(
//                    currentState.minPrice,
//                    currentState.maxPrice,
//                    currentState.equipements,
//                    leftPinValue?.toInt(),
//                    rightPinValue?.toInt(),
//                    currentState.sort,
//                    currentState.checkedDistance
//                    ,
//                    currentState.checkedPrice,
//                    currentState.checkedEquipements
//                )
                currentFilterState.postFilterParkingsState(currentState)

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
                currentState.minPrice =  leftPinValue?.toInt()
                currentState.maxPrice = rightPinValue?.toInt()
//                val newState = FilterParkingsModel(
//                    leftPinValue?.toInt(),
//                    rightPinValue?.toInt(),
//                    currentState.equipements,
//                    currentState.minDistance,
//                    currentState.maxDistance,
//                    currentState.sort,
//                    currentState.checkedDistance
//                    ,
//                    currentState.checkedPrice,
//                    currentState.checkedEquipements
//                )
                currentFilterState.postFilterParkingsState(currentState)

            }

            override fun onTouchStarted(rangeBar: RangeBar?) {
            }

        })
        binding.root.apply_filter_btn?.setOnClickListener {
            mParkingListViewModel.getFilteredParkings(currentFilterState.getFilterParkingsState().value!!)
                .observe(viewLifecycleOwner, Observer<ArrayList<Parking>>
                {
                    //mParkingListViewModel?.postFilteredList(it)
                    dismiss()
                })


        }
        binding.root.reset_filter_btn?.setOnClickListener {
            currentFilterState.postFilterParkingsState(FilterParkingsModel())
            dismiss() // to change by resetting values of filter components
            mParkingListViewModel.getFilteredParkings(FilterParkingsModel())
                .observe(viewLifecycleOwner, Observer<ArrayList<Parking>>
                {
                    //mParkingListViewModel?.postFilteredList(it)
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
        distance_container?.isExpanded =
            currentFilterState.getFilterParkingsState().value!!.checkedDistance
        check_distance.setOnCheckedChangeListener { compoundButton, b ->
            distance_container.toggle()
            val currentState = currentFilterState.getFilterParkingsState().value!!
            currentState.checkedDistance =  !currentState.checkedDistance
           /* val newState = FilterParkingsModel(
                currentState.minPrice,
                currentState.maxPrice,
                currentState.equipements,
                currentState.minDistance,
                currentState.maxDistance,
                currentState.sort,
                !currentState.checkedDistance
                ,
                currentState.checkedPrice,
                currentState.checkedEquipements
            )*/
            currentFilterState.postFilterParkingsState(currentState)
        }
        val check_price = view.findViewById<CheckBox>(R.id.price_check)
        val price_container = view.findViewById<ExpandableLayout>(R.id.price_container)
        price_container?.isExpanded =
            currentFilterState.getFilterParkingsState().value!!.checkedPrice


        check_price.setOnCheckedChangeListener { compoundButton, b ->
            price_container.toggle()
            val currentState = currentFilterState.getFilterParkingsState().value!!
            currentState.checkedPrice =  !currentState.checkedPrice
           /* val newState = FilterParkingsModel(
                currentState.minPrice,
                currentState.maxPrice,
                currentState.equipements,
                currentState.minDistance,
                currentState.maxDistance,
                currentState.sort,
                currentState.checkedDistance
                ,
                !currentState.checkedPrice,
                currentState.checkedEquipements
            )*/
            currentFilterState.postFilterParkingsState(currentState)
        }


        val services_list = view.findViewById<RecyclerView>(R.id.services_list)
        services_list.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        // bring real services
        val services: ArrayList<Equipement> = currentFilterState.getFilterMainInfo().value!!.equipements!!
        Log.d("real services from db", services.toString())
        /* to improve **/
        if (currentFilterState.getFilterParkingsState().value!!.equipements != null && currentFilterState.getFilterParkingsState().value!!.equipements != "") {
            val selectedIds =
                currentFilterState.getFilterParkingsState().value!!.equipements?.split(",")
            Log.d("selectedids", selectedIds.toString())
            services.forEach { serv -> serv.checked = false }

            selectedIds?.forEach { id ->
                arrayEquipements.add(id.toInt())
                val service = services.find { serv -> serv.idEquipement == id }
                service?.checked = true
            }
        }else {
            services.forEach { serv -> serv.checked = false }
        }
        serviceAdapter =
            ServiceAdapter(services, object : MyAdapter.ItemAdapterListener<Equipement> {
                override fun onItemClicked(item: Equipement) {
                    // add case click to remove
                    val checkedService = item.checked
                    if (!checkedService) {
                        arrayEquipements.remove(item.idEquipement.toInt())
                    } else arrayEquipements.add(item.idEquipement.toInt())
                    var toPost: String? = arrayEquipements.joinToString(separator = ",")
                    Log.d("TOPOSTT", toPost!!)
                    if (toPost == "") toPost = null
                    val currentState = currentFilterState.getFilterParkingsState().value!!
                    currentState.equipements = toPost
                    currentFilterState.postFilterParkingsState(
                        currentState
                        /*FilterParkingsModel(
                            currentState.minPrice,
                            currentState.maxPrice,
                            toPost,
                            currentState.minDistance,
                            currentState.maxDistance,
                            currentState.sort,
                            currentState.checkedDistance,
                            currentState.checkedPrice,
                            currentState.checkedEquipements
                        )*/
                    )

                }


            })
        services_list.adapter = serviceAdapter

        val check_service = view.findViewById<CheckBox>(R.id.service_check)
        val service_container = view.findViewById<ExpandableLayout>(R.id.service_container)
        service_container?.isExpanded =
            currentFilterState.getFilterParkingsState().value!!.checkedEquipements

        service_container.setOnExpansionUpdateListener { expansionFraction, state ->
            val expansionFr = state
            if (state == 3) {
                scroll_filters.smoothScrollTo(0, scroll_filters.bottom)
            }
        }

        check_service.setOnCheckedChangeListener { compoundButton, b ->
            service_container.toggle()
            val currentState = currentFilterState.getFilterParkingsState().value!!
            currentState.checkedEquipements =  !currentState.checkedEquipements
//            val newState = FilterParkingsModel(
//                currentState.minPrice,
//                currentState.maxPrice,
//                currentState.equipements,
//                currentState.minDistance,
//                currentState.maxDistance,
//                currentState.sort,
//                currentState.checkedDistance
//                ,
//                currentState.checkedPrice,
//                !currentState.checkedEquipements
//            )
            currentFilterState.postFilterParkingsState(currentState)
        }

        sort_toggle_price = view.findViewById<TextView>(R.id.toggle_price)
        sort_toggle_price.setOnClickListener {
            toggleSort(false)  //false for price
            Log.d("sort by price", "sorting")
            mParkingListViewModel.sortByPrice()
            val currentState = currentFilterState.getFilterParkingsState().value!!
            currentState.sort = 2
//            val newState = FilterParkingsModel(
//                currentState.minPrice,
//                currentState.maxPrice,
//                currentState.equipements,
//                currentState.minDistance,
//                currentState.maxDistance,
//                2,
//                currentState.checkedDistance
//                ,
//                currentState.checkedPrice,
//                currentState.checkedEquipements
//            )
            currentFilterState.postFilterParkingsState(currentState)
        }
        sort_toggle_distance = view.findViewById<TextView>(R.id.toggle_distance)
        sort_toggle_distance.setOnClickListener {
            toggleSort(true)  //true for distance
            Log.d("sort by distance", "sorting")
            mParkingListViewModel.sortByDistance()
            val currentState = currentFilterState.getFilterParkingsState().value!!
            currentState.sort = 1
//            val newState = FilterParkingsModel(
//                currentState.minPrice,
//                currentState.maxPrice,
//                currentState.equipements,
//                currentState.minDistance,
//                currentState.maxDistance,
//                1,
//                currentState.checkedDistance
//                ,
//                currentState.checkedPrice,
//                currentState.checkedEquipements
//            )
            currentFilterState.postFilterParkingsState(currentState)
        }
        val currentS = currentFilterState?.getFilterParkingsState()?.value!!

        // fix setting pins initially check not null and stuff
        if (currentS.checkedPrice && currentS.minPrice!=null && currentS.maxPrice!=null) price_range_bar?.setRangePinsByValue(
            currentS.minPrice?.toFloat()!!,
            currentS.maxPrice?.toFloat()!!
        )
        if (currentS.checkedDistance && currentS.minDistance!=null && currentS.maxDistance!=null) distance_range_bar?.setRangePinsByValue(
            currentS.minDistance?.toFloat()!!,
            currentS.maxDistance?.toFloat()!!
        )


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
            dialog!!.window!!.statusBarColor =
                ContextCompat.getColor(context!!, R.color.colorPrimaryDark)
            dialog!!.window!!.decorView.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_STABLE

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
    }

    override fun onDismiss(dialog: DialogInterface) {
        val c = currentFilterState.getFilterParkingsState().value!!
        Log.d("savingUpDismissing", c.toString())
        currentFilterState.resetFilterInfos()
        PreferenceManager(context!!).writeFilterInfo(currentFilterState.getFilterParkingsState().value!!)
        super.onDismiss(dialog)
    }
    private fun postCurrentState(currentState :  FilterParkingsModel) {

    }
}