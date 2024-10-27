package com.sl.myweatherapplication.ViewModel

import androidx.lifecycle.ViewModel
import com.sl.myweatherapplication.Server.ApiClient
import com.sl.myweatherapplication.Server.ApiServices
import com.sl.myweatherapplication.repository.WeatherRepository

class WeatherViewModel(val repository: WeatherRepository):ViewModel(){

    constructor():this(WeatherRepository(ApiClient().getClient().create(ApiServices::class.java)))

    fun loadCurrentWeather(lat:Double,lng:Double,unit:String)=
        repository.getCurrentWeather(lat,lng,unit)
    fun loadForecastWeather(lat:Double,lng:Double,unit:String)=
        repository.getForecastWeather(lat,lng,unit)

}