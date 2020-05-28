package com.example.myparking.utils

import com.pusher.pushnotifications.auth.AuthData
import com.pusher.pushnotifications.auth.AuthDataGetter


object  BeamsTokenProvider {

    fun getTokenProvider(driverId: String):com.pusher.pushnotifications.auth.BeamsTokenProvider {
        return com.pusher.pushnotifications.auth.BeamsTokenProvider(
            "https://myparking-backend.herokuapp.com/pusher/beams_auth/driver",
            object: AuthDataGetter {
                override fun getAuthData(): AuthData {
                    return AuthData(
                        // Headers and URL query params your auth endpoint needs to
                        // request a Beams Token for a given user
                        headers = hashMapOf(
                            // for example:
                            // "Authorization" to sessionToken
                        ),
                        queryParams = hashMapOf(
                            Pair("user_id",driverId)
                        )
                    )
                }
            }
        )
    }

}