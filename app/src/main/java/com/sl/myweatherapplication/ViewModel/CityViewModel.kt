package com.sl.myweatherapplication.ViewModel

import androidx.lifecycle.ViewModel
import com.sl.myweatherapplication.Server.ApiClient
import com.sl.myweatherapplication.Server.ApiServices
import com.sl.myweatherapplication.repository.CityRepository

class CityViewModel(val repository: CityRepository):ViewModel() {
    constructor():this(CityRepository(ApiClient().getClient().create(ApiServices::class.java)))

    fun loadCity(q:String,limit:Int)=
        repository.getCities(q, limit)

}