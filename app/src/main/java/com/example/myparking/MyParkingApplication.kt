package com.example.myparking

import android.app.Application
import android.content.Context
import com.example.myparking.DI.APIComponent
import com.example.myparking.DI.APIModule
import com.example.myparking.DI.DaggerAPIComponent
import com.example.myparking.repositories.APIURL

class MyParkingApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        ctx = applicationContext
        apiComponent = initDaggerComponent()

    }

    fun getMyComponent(): APIComponent {
        return apiComponent
    }

    fun initDaggerComponent():APIComponent{
        apiComponent =   DaggerAPIComponent
            .builder()
            .aPIModule(APIModule(APIURL.BASE_URL))
            .build()
        return  apiComponent

    }

    companion object {
        var ctx: Context? = null
        lateinit var apiComponent: APIComponent
    }
}