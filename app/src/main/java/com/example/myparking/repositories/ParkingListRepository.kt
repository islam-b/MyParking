package com.example.myparking.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myparking.models.FilterInfoResponse
import com.example.myparking.models.FilterParkingsModel
import com.example.myparking.models.Parking
import com.example.myparking.services.ParkingService
import com.example.myparking.utils.InjectorUtils

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.logging.Filter


class ParkingListRepository { // maybe add dao

    private var dataSet = ArrayList<Parking>()
    var mParkingList: MutableLiveData<ArrayList<Parking>>
    var filteredParkings : MutableLiveData<ArrayList<Parking>>
    var filterInfos = MutableLiveData<FilterInfoResponse>(FilterInfoResponse())
    var service: ParkingService

    init {
        service = InjectorUtils.provideParkingService()
        mParkingList = MutableLiveData()
        filteredParkings = MutableLiveData()
    }


    fun getParkingsList(filterStateLiveData: MutableLiveData<FilterParkingsModel>,idAutomobiliste: Int,start: String?, destination:String?): MutableLiveData<ArrayList<Parking>> {
        var filterState = FilterParkingsModel()
        filterStateLiveData?.let { filterState = filterStateLiveData?.value!! }
        if(!filterState.checkedEquipements) filterState.equipements=null
        if(!filterState.checkedPrice) {
            filterState.minPrice = null
            filterState.maxPrice = null
        }
        if(!filterState.checkedDistance) {
            filterState.minDistance = null
            filterState.maxDistance = null
        }
        //var data = MutableLiveData<ArrayList<Parking>>()

        Log.d("params values",idAutomobiliste.toString())
        /*if (dataSet.size == 0 || dataSet.isEmpty()) {*/
        service.findParkings(idAutomobiliste, start, destination ,
            filterState.minPrice,
            filterState.maxPrice,
            filterState.equipements,
            filterState.minDistance,
            filterState.maxDistance
        ).enqueue(object : Callback<List<Parking>> {
            override fun onFailure(call: Call<List<Parking>>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<List<Parking>>,
                response: Response<List<Parking>>
            ) {
                Log.d("code body",response.code().toString())
                Log.d("text body",response.toString())
                dataSet = ArrayList(response.body()!!)
                Log.d("ViewModelReRequest", dataSet.size.toString())
                if(filterState.sort==1) {
                    Log.d("ViewModelReRequestSort", "distance")
                    val newList =  ArrayList(dataSet.sortedWith(compareBy { it.routeInfo?.walkingDistance }))
                    filteredParkings.postValue(newList)
                }else {
                    Log.d("ViewModelReRequestSort", "price")
                    val newList = ArrayList(dataSet?.sortedWith(compareBy {it.tarifs?.get(0).prix}))
                    filteredParkings.postValue(newList)
                }


            }
        })
        return filteredParkings
        /*  }
          data.value= dataSet
          return data*/
    }

    fun getNotFilteredParkingsList(idAutomobiliste: Int,start: String?, destination:String?): MutableLiveData<ArrayList<Parking>> {
        var filterState = FilterParkingsModel()
        //var data = MutableLiveData<ArrayList<Parking>>()

        Log.d("params values",idAutomobiliste.toString())
        /*if (dataSet.size == 0 || dataSet.isEmpty()) {*/
        service.findParkings(idAutomobiliste, start, destination ,
            filterState.minPrice,
            filterState.maxPrice,
            filterState.equipements,
            filterState.minDistance,
            filterState.maxDistance
        ).enqueue(object : Callback<List<Parking>> {
            override fun onFailure(call: Call<List<Parking>>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<List<Parking>>,
                response: Response<List<Parking>>
            ) {
                Log.d("code body",response.code().toString())
                Log.d("text body",response.toString())
                dataSet = ArrayList(response.body()!!)
                Log.d("ViewModelReRequest", dataSet.size.toString())
                Log.d("ViewModelReRequestSort", "distance")
                val newList =  ArrayList(dataSet.sortedWith(compareBy { it.routeInfo?.walkingDistance }))
                mParkingList.postValue(newList)
            }
        })
        return mParkingList
        /*  }
          data.value= dataSet
          return data*/
    }


    fun getFilterInfo(idAutomobiliste:Int, start: String?) : MutableLiveData<FilterInfoResponse> {

        service.findFilterInfo(idAutomobiliste,start).enqueue(object : Callback<FilterInfoResponse> {
            override fun onFailure(call: Call<FilterInfoResponse>, t: Throwable) {

                Log.d("filters_error",t.message.toString())
            }

            override fun onResponse(
                call: Call<FilterInfoResponse>,
                response: Response<FilterInfoResponse>
            ) {
                Log.d("info params filter", idAutomobiliste.toString() +" "+start)
                Log.d("filter infos", response.body().toString())
                filterInfos.postValue(response.body())
            }
        })
        return filterInfos
    }
    /* fun getParkingItem(index: Int) : Parking {
         if (dataSet.find { parking -> parking.idParking == index })!=null) {
             getParkingsList()
         }
     }*/
    fun getParkings(): ArrayList<Parking> {
        return dataSet
    }

    companion object {
        @Volatile
        private var instance: ParkingListRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: ParkingListRepository().also { instance = it }
            }
    }
}