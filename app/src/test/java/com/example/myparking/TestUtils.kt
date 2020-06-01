package com.example.myparking

import com.example.myparking.models.*

object TestUtils {
    fun getParkingListMock(): ArrayList<Parking> {
        val equipements = ArrayList<Equipement>()
        equipements.add(
            Equipement(
                "2",
                "H24",
                "https://image.flaticon.com/icons/png/512/68/68544.png"
            )
        )
        val horairesStatus = ArrayList<HoraireStatus>()
        horairesStatus.add(HoraireStatus(arrayListOf("Dimanche"), "07:00:00", "19:00:00"))
        val etages = ArrayList<Etage>()
        etages.add(Etage(1, 10))
        etages.add(Etage(2, 10))
        val termes = ArrayList<Terme>()
        termes.add(Terme(1, "Terme1"))
        termes.add(Terme(2, "Terme2"))
        val tarifs = ArrayList<Tarif>()
        tarifs.add(Tarif(1, "60.0", "120.0"))
        val paiments = ArrayList<Paiement>()
        val routeInfo = RouteInfo(
            travelDistance = 2,
            travelTime = 2,
            walkingDistance = 4,
            walkingTime = 4,
            canWalk = true
        )
        val routeInfo2 = RouteInfo(
            travelDistance = 1,
            travelTime = 1,
            walkingDistance = 1,
            walkingTime = 1,
            canWalk = true
        )
        paiments.add(
            Paiement(
                1,
                "CIB",
                "https://cdn1.iconfinder.com/data/icons/ios-11-glyphs/30/money_bag-512.png"
            )
        )
        paiments.add(
            Paiement(
                2,
                "Edahabiya",
                "https://cdn1.iconfinder.com/data/icons/ios-11-glyphs/30/money_bag-512.png"
            )
        )
        val parking1 = Parking(
            idParking = 2,
            nbEtages = "2",
            nbPlaces = "20",
            nbPlacesLibres = "6",
            nom = "Parking Said hamdine",
            adresse = "Alger",
            imageUrl = "https://www.miresparis.com/wp-content/uploads/2018/06/C1577-bis-241.jpg",
            lattitude = 36.7275239,
            longitude = 3.0448747,
            etages = etages,
            equipements = equipements,
            tarifs = tarifs,
            paiments = paiments,
            ouvert = "Ouvert",
            routeInfo = routeInfo,
            horairesStatus = horairesStatus,
            termes = termes
        )
        val parking2 = Parking(
            idParking = 3,
            nbEtages = "2",
            nbPlaces = "20",
            nbPlacesLibres = "6",
            nom = "Parking Said hamdine",
            adresse = "Alger",
            imageUrl = "https://www.miresparis.com/wp-content/uploads/2018/06/C1577-bis-241.jpg",
            lattitude = 36.7275239,
            longitude = 3.0448747,
            etages = etages,
            equipements = equipements,
            tarifs = tarifs,
            paiments = paiments,
            ouvert = "Ouvert",
            routeInfo = routeInfo2,
            horairesStatus = horairesStatus,
            termes = termes
        )
        return arrayListOf(parking1, parking2)
    }

}