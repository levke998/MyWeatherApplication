package com.sl.myweatherapplication.Server

import com.sl.myweatherapplication.model.CurrentResponseApi
import com.sl.myweatherapplication.model.ForecastResponseApi
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET("data/2.5/weather")
    fun getCurrentWeather(
        @Query("lat") lat:Double,
        @Query("lon") lon:Double,
        @Query("units") units:String,
        @Query("appid") ApiKey:String,
    ):Call<CurrentResponseApi>

    @GET("data/2.5/weather")
    fun getForecastWeather(
        @Query("lat") lat:Double,
        @Query("lon") lon:Double,
        @Query("units") units:String,
        @Query("appid") ApiKey:String,
    ):Call<ForecastResponseApi>
}
