package com.example.myparking.DI

import com.example.myparking.AppModule
import com.example.myparking.fragements.ParkingsList
import com.example.myparking.repositories.ParkingListRepository
import com.example.myparking.viewmodels.ParkingListViewModel
import com.example.myparking.viewmodels.ParkingListViewModelFactory
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class,APIModule::class])
interface APIComponent  {
    fun inject(retrofitRepository: ParkingListRepository)
    fun inject(parkingListViewModel: ParkingListViewModel)
    fun inject(parkingList : ParkingsList)
    fun inject(parkingListViewModelFactory: ParkingListViewModelFactory)
}