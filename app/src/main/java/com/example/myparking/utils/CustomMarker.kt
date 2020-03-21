package com.example.myparking.utils

import com.example.myparking.fragements.ParkingsMap
import com.here.android.mpa.common.GeoCoordinate
import com.here.android.mpa.common.Image
import com.here.android.mpa.mapping.MapMarker

class CustomMarker(geo: GeoCoordinate, image: Image, val customType: ParkingsMap.Companion.PlaceType, val data: Any): MapMarker(geo,image)