package com.example.myparking.services

import com.example.myparking.models.*
import retrofit2.Call
import retrofit2.http.*


interface ParkingService {
    @GET("parking/")
    fun findParkings(@Query("automobiliste") idAutomobilistte: Int?,
                     @Query("start") start: String?,
                     @Query("destination") destination: String?,
                    @Query("minPrice") minPrice: Int?,
                     @Query("maxPrice") maxPrice: Int?,
                     @Query(value ="equipements", encoded = true) equipements: String?,
                     @Query("minDistance") minDistance: Int?,
                     @Query("maxDistance") maxDistance: Int?
    ): Call<List<Parking>>
    @GET("search")
    fun searchPlaces(@Query("query") keyword:String): Call<SearchModel>

    @GET("filterInfos/")
    fun findFilterInfo(@Query("automobiliste") idAutomobilistte: Int?,
                       @Query("start") start: String?): Call<FilterInfoResponse>



    @GET("favoris/")
    fun getFavorites(@Query("automobiliste") idAutomobilistte:Int,
                     @Query("start") start: String?,
                     @Query("destination") destination: String?): Call<FavoritesResponse>

    @POST("favoris/")
    fun addFavorite(@Query("automobiliste") idAutomobilistte: Int, @Body body: AddFavRequest): Call<Parking>

    @HTTP(method = "DELETE", path = "favoris/", hasBody = true)
    fun deleteFavorite(@Query("automobiliste") idAutomobilistte: Int, @Body body: AddFavRequest): Call<Parking>

}
