package com.sl.myweatherapplication.repository

import com.sl.myweatherapplication.Server.ApiServices

class CityRepository(val api: ApiServices) {
    fun getCities(q:String, limit:Int) =
        api.getCitiesList(q,limit,"fb16644f760e721545fed288c823d0dc")
}