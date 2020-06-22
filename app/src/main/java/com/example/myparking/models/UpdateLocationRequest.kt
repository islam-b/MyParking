package com.example.myparking.models

data class UpdateLocationRequest(val driverId:Int,val lat:Double,val long:Double, val forSearch: Boolean) {
}