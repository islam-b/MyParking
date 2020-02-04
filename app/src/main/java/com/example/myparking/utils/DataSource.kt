package com.example.myparking.utils

import com.example.myparking.R
import com.example.myparking.models.*

object DataSource {
    var parkingsList = ArrayList<Parking>()

    init {
        val list = ArrayList<Horaire>()
        list.add(Horaire("Dim - Jeu", "9h 21 h"))
        list.add(Horaire("Ven - Sam", "8h 19 h"))

        val tarifs = ArrayList<Tarif>()
        tarifs.add(Tarif("1 heure", "100DA"))
        tarifs.add(Tarif("2 heures", "150 DA"))
        tarifs.add(Tarif("3 ou plus", "200 DA"))

        val paiements = ArrayList<Int>()
        paiements.add(1)
        paiements.add(2)

        val terms = ArrayList<String>()
        terms.add("Le fait de laisser une voiture dans le parking implique l’acceptation pleine et entière par l’Usager des conditions du présent règlement dont unexemplaire sera affiché visiblement à l’entrée")
        terms.add("L'Usager aura accès au parking pendant les heures d'ouverture de celui-ci sauf dispositions contraires prévues dans la convention liant l'Usagerabonné au gestionnaire")
        terms.add("L’utilisation  du  parking  est  soumise au paiement d’une redevance dont le montant est fixé")
        var parking1 = Parking(
            "Ouvert",
            " - 50%",
            "Metropolis at Metrotown",
            "4700 kingsway Burnaby, BC V5H 2C3",
            "0,5 Km",
            " - 5 min de marche",
            "https://fastly.4sqi.net/img/general/200x200/57514184_AJ0MhpiJ4fYLsflNcSXVypv51JLQeRftM-WqFSMc93Y.jpg",
            list,
            tarifs,
            paiements,
            paiements,
            terms
        )
        val parking2 = Parking(
            "Ouvert",
            " - 10%",
            "Jefferson Blvd Garage",
            "700-754 W Jefferson Los Angelos, CA 90007, US, ",
            "1,5 Km",
            " - 15 min de marche",
            "https://d13esfgglb25od.cloudfront.net/lot_img/84834/b033e04379dc4be0abfa251b6759148a.jpg",
            null,
            tarifs,
            arrayListOf(2, 3),
            arrayListOf(1, 2, 3),
            terms
        )
        val parking3 = Parking(
            "Ouvert",
            " - 12%",
            "Exposition Park",
            "243 Exposition Park Dr Los Angelos, CA 90037, US",
            "1,0 Km",
            " - 10 min de marche",
            "https://fastly.4sqi.net/img/general/200x200/57514184_AJ0MhpiJ4fYLsflNcSXVypv51JLQeRftM-WqFSMc93Y.jpg",
            list,
            tarifs, null, null, terms
        )
        val parking4 = Parking(
            "Ouvert",
            " - 14%",
            "Parking cool",
            "243 Exposition Park Dr Los Angelos, CA 90037, US",
            "1,0 Km",
            " - 10 min de marche",
            "https://fastly.4sqi.net/img/general/200x200/57514184_AJ0MhpiJ4fYLsflNcSXVypv51JLQeRftM-WqFSMc93Y.jpg",
            list,
            tarifs, null, null, terms
        )
        val parking5 = Parking(
            "Ouvert",
            " - 15%",
            "Exposition Park2",
            "243 Exposition Park Dr Los Angelos, CA 90037, US",
            "1,0 Km",
            " - 10 min de marche",
            "https://fastly.4sqi.net/img/general/200x200/57514184_AJ0MhpiJ4fYLsflNcSXVypv51JLQeRftM-WqFSMc93Y.jpg",
            list,
            tarifs, null, null, terms
        )


        parkingsList.add(parking1)
        parkingsList.add(parking2)
        parkingsList.add(parking3)
        parkingsList.add(parking4)
        parkingsList.add(parking5)

    }

    fun getParkings(): ArrayList<Parking> {
        return parkingsList
    }

    fun getHoraire(parking: Parking?): ArrayList<Horaire> {
        val list = ArrayList<Horaire>()
        parking?.let {
            parking?.horaire?.let {
                return parking.horaire
            }

        }
        return list
    }

    fun getTarifs(parking: Parking?): ArrayList<Tarif> {
        val list = ArrayList<Tarif>()
        parking?.let {
            parking?.tarifs?.let {
                return parking.tarifs
            }

        }
        return list
    }

    fun getPaiement(parking: Parking?): ArrayList<Paiement> {
        var newList = ArrayList<Paiement>()
        val paiements = parking?.paiements
        paiements?.let {
            for (i in paiements) {
                when (i) {
                    1 -> newList.add(Paiement("Cheques", R.drawable.ic_check))
                    2 -> newList.add(Paiement("Liquide", R.drawable.ic_liquide))
                    3 -> newList.add(
                        Paiement(
                            "Carte Bancaire",
                            R.drawable.ic_debitcard
                        )
                    )
                }
            }
        }
        return newList
    }

    fun getEquipement(parking: Parking?): ArrayList<Equipement> {
        var newList = ArrayList<Equipement>()
        val equipements = parking?.equipements
        equipements?.let {
            for (i in equipements) {
                when (i) {
                    1 -> newList.add(
                        Equipement(
                            "Surveillance CCTV",
                            R.drawable.ic_cctv
                        )
                    )
                    2 -> newList.add(
                        Equipement(
                            "Espace pour handicapés",
                            R.drawable.ic_handicap
                        )
                    )
                    3 -> newList.add(
                        Equipement(
                            "Sanitaires",
                            R.drawable.ic_sanitaire
                        )
                    )
                }
            }
        }
        return newList
    }

    fun getReservations(): ArrayList<Reservation> {
        var list = ArrayList<Reservation>()
        val reservation1 =
            Reservation("DZ-1245869", "Lun, Déc 16 12.40", "Lun, Déc 16 13.40", parkingsList[0])
        val reservation2 =
            Reservation("DZ-1478574", "Lun, Déc 16 15.40", "Lun, Déc 16 18.40", parkingsList[1])
        list.add(reservation1)
        list.add(reservation2)
        return list

    }

    fun getTerms(currentParking: Parking?): ArrayList<String> {
        return currentParking?.termes!!
    }
}