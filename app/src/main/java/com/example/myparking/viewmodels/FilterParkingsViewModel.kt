package com.example.myparking.viewmodels

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.myparking.models.Automobiliste
import com.example.myparking.models.FilterInfoResponse
import com.example.myparking.models.FilterParkingsModel
import com.example.myparking.repositories.ParkingListRepository
import com.example.myparking.utils.PreferenceManager
import java.util.*

class FilterParkingsViewModel(var idAutomobiliste: Int, val  prfManager: PreferenceManager): ViewModel(){



    private var filterMainInfo : MutableLiveData<FilterInfoResponse>// for equipemnt list , from data base min and max a discuter
    private var filterParkingsState = MutableLiveData<FilterParkingsModel> ()
//    var distanceCheck = filterParkingsState.value?.minDistance!=null || filterParkingsState.value?.maxDistance!= null
    init {
        val start = prfManager.getLastLocationStr()
        filterMainInfo = ParkingListRepository.getInstance().getFilterInfo(idAutomobiliste,start)
        filterParkingsState.value = FilterParkingsModel()
    }
   /* fun setInitialFilters(filters: FilterParkingsModel) {
        filterParkingsState.value = filter
    }*/
    fun getFilterMainInfo(): LiveData<FilterInfoResponse> {
        return filterMainInfo
    }
    fun getFilterParkingsState(): LiveData<FilterParkingsModel> {
        return filterParkingsState
    }

    fun postFilterParkingsState(filterParkingsModel: FilterParkingsModel) {
        filterParkingsState.value = filterParkingsModel
        Log.d("postedFilterState", filterParkingsModel.toString())
       // distanceCheck =  filterParkingsState.value?.minDistance!=null || filterParkingsState.value?.maxDistance!= null
    }

//    fun distanceIsChecked() : Boolean {
//        if (filterParkingsState.value?.minDistance!=null || filterParkingsState.value?.maxDistance!= null) return true
//        return false
//        return distanceCheck
//    }
//    fun priceIsChecked() : Boolean {
//        if (filterParkingsState.value?.minPrice!=null || filterParkingsState.value?.maxPrice!= null) return true
//        return false
//    }
//    fun serviceIsChecked(): Boolean {
//        if (filterParkingsState.value?.equipements == null ||filterParkingsState.value?.equipements == "" ) return false
//        return true
//    }
//    fun distanceIsCheckedContainer(): String {
//        if (distanceIsChecked()) return "true"
//        return "false"
//    }
//    fun priceIsCheckedContainer(): String {
//        if(priceIsChecked()) return "true"
//        return "false"
//    }
//    fun serviceIsCheckedContainer(): String {
//        if(serviceIsChecked()) return "true"
//        return "false"
//    }
//    fun changeDistanceChecked(isChecked: Boolean) {
//        distanceCheck= isChecked
//    }
  /*  @Bindable
    fun getDistanceChecked(): Boolean {
        return chec
    }

    fun setRememberMe(value: Boolean) {
        // Avoids infinite loops.
        if (data.rememberMe != value) {
            data.rememberMe = value

            // React to the change.
            saveData()

            // Notify observers of a new value.
            notifyPropertyChanged(BR.remember_me)
        }
    }*/

}