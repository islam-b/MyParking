package com.example.myparking.utils

import android.util.Log
import com.example.myparking.repositories.ParkingListRepository
import com.example.myparking.services.AuthService
import com.example.myparking.services.ParkingService
import com.example.myparking.services.ReservationService
import com.example.myparking.viewmodels.ParkingListViewModelFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object InjectorUtils {
    private val baseURL = "http://myparking.jcloud-ver-jpe.ik-server.com/"
    private val retrofit = this.provideRetrofit()

    fun provideParkingListViewModelFactory(): ParkingListViewModelFactory {
        val parkingListRepository = ParkingListRepository.getInstance( /*dao list*/)
        return ParkingListViewModelFactory(parkingListRepository)
    }

    private fun provideRetrofit(): Retrofit {
        Log.d("retrofit inst", "INSTA")
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                .readTimeout(1200,TimeUnit.SECONDS)
                .connectTimeout(1200, TimeUnit.SECONDS)
                .build()
            )
            .build()
    }
    fun provideParkingService(): ParkingService {
        return retrofit.create(ParkingService::class.java)
    }
    fun provideReservationService(): ReservationService {
        return retrofit.create(ReservationService::class.java)
    }

    fun provideAuthService(): AuthService {
        return retrofit.create(AuthService::class.java)
    }

}