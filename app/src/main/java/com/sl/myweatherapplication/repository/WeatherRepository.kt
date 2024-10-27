package com.sl.myweatherapplication.repository

import com.sl.myweatherapplication.Server.ApiServices

class WeatherRepository(val api:ApiServices) {
    fun getCurrentWeather(lat:Double, lng:Double, unit:String)=
        api.getCurrentWeather(lat,lng,unit, "fb16644f760e721545fed288c823d0dc")
    fun getForecastWeather(lat: Double, lng:Double, unit:String)=
        api.getForecastWeather(lat,lng,unit, "fb16644f760e721545fed288c823d0dc")
}