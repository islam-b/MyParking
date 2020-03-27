package com.example.myparking.services

import com.example.myparking.models.*
import retrofit2.Call
import retrofit2.http.*


interface ParkingService {
    @GET("parking/?start=36.734473%2C3.152525")
    fun findParkings(@Query("minPrice") minPrice: Int?,
                     @Query("maxPrice") maxPrice: Int?,
                     @Query(value ="equipements", encoded = true) equipements: String?,
                     @Query("minDistance") minDistance: Int?,
                     @Query("maxDistance") maxDistance: Int?
    ): Call<List<Parking>>
    @GET("search")
    fun searchPlaces(@Query("query") keyword:String): Call<SearchModel>

    @GET("filterInfos/?start=36.705039%2C3.173912")
    fun findFilterInfo(): Call<FilterInfoResponse>



    @GET("favoris/")
    fun getFavorites(@Query("automobiliste") idAutomobilistte:Int): Call<FavoritesResponse>

    @POST("favoris/")
    fun addFavorite(@Query("automobiliste") idAutomobilistte: Int, @Body body: AddFavRequest): Call<Parking>

    @HTTP(method = "DELETE", path = "favoris/", hasBody = true)
    fun deleteFavorite(@Query("automobiliste") idAutomobilistte: Int, @Body body: AddFavRequest): Call<Parking>

}
