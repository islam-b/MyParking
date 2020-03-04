package com.example.myparking.models

import com.here.android.mpa.mapping.Map
import com.here.android.mpa.routing.Route

data class RouteDetail(val parking: Parking, val route: Route, val map: Map)