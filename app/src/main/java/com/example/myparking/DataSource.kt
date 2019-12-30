package com.example.myparking

import com.example.myparking.models.Parking

object DataSource {
    var parkingsList = ArrayList<Parking>()

    init {
        val parking1 = Parking("Ouvert", " - 50%", "Metropolis at Metrotown", "4700 kingsway Burnaby, BC V5H 2C3", "0,5 Km", " - 5 min de marche", "https://fastly.4sqi.net/img/general/200x200/57514184_AJ0MhpiJ4fYLsflNcSXVypv51JLQeRftM-WqFSMc93Y.jpg")
        val parking2 = Parking("Ouvert", " - 10%", "Jefferson Blvd Garage", "700-754 W Jefferson Los Angelos, CA 90007, US, ","1,5 Km", " - 15 min de marche", "")
        val parking3 = Parking("Ouvert", " - 12%", "Exposition Park", "243 Exposition Park Dr Los Angelos, CA 90037, US","1,0 Km", " - 10 min de marche","")
        val parking4 = Parking("Ouvert", " - 14%", "Parking cool", "243 Exposition Park Dr Los Angelos, CA 90037, US","1,0 Km", " - 10 min de marche","")
        val parking5 = Parking("Ouvert", " - 15%", "Exposition Park", "243 Exposition Park Dr Los Angelos, CA 90037, US","1,0 Km", " - 10 min de marche","")

        parkingsList.add(parking1)
        parkingsList.add(parking2)
        parkingsList.add(parking3)
        parkingsList.add(parking4)
        parkingsList.add(parking5)
    }

    fun getParkings(): ArrayList<Parking>{
        return parkingsList
    }


}