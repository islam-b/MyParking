package com.example.myparking

import dagger.Module
import dagger.Provides

@Module
class AppModule constructor(myParkingApplication: MyParkingApplication){

    var myParkingApplication: MyParkingApplication = myParkingApplication


    @Provides
    fun provideMyParkingApplication():MyParkingApplication{
        return myParkingApplication
    }
}