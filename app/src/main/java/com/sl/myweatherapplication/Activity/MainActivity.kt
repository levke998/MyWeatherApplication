@file:Suppress("DEPRECATION")

package com.sl.myweatherapplication.Activity


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.matteobattilana.weather.PrecipType
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sl.myweatherapplication.Adapter.ForecastAdapter
import com.sl.myweatherapplication.R
import com.sl.myweatherapplication.ViewModel.WeatherViewModel
import com.sl.myweatherapplication.databinding.ActivityMainBinding
import com.sl.myweatherapplication.model.CurrentResponseApi
import com.sl.myweatherapplication.model.ForecastResponseApi
import eightbitlab.com.blurview.RenderScriptBlur
import retrofit2.Call
import retrofit2.Response
import java.util.Calendar
import java.util.*
import android.provider.Settings


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val weatherViewModel:WeatherViewModel by viewModels()
    private val calendar by lazy { Calendar.getInstance() }
    private val forecastAdapter by lazy {  ForecastAdapter()}
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2
    private var lat =0.0
    private var lon =0.0
    private var name = "-"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }

        binding.apply {
            lat = intent.getDoubleExtra("lat", 0.0)
            lon = intent.getDoubleExtra("lon", 0.0)
            name = intent.getStringExtra("name").toString()

            if (lat == 0.0) {
                getLocation()
            }

            addCity.setOnClickListener{
                startActivity(Intent(this@MainActivity, CityListActivity::class.java))
            }
            extraBtn.setOnClickListener{
                startActivity(Intent(this@MainActivity, CountDownActivity::class.java))
            }


            cityTxt.text = name

            progressBar.visibility=View.VISIBLE
            loadDatas(lat,lon,"metric")

            //Blue view
            var radius = 10f
            val decorView = window.decorView
            val rootView = (decorView.findViewById(android.R.id.content) as ViewGroup?)
            val windowBackground = decorView.background
            rootView?.let{
                blueView.setupWith(it, RenderScriptBlur(this@MainActivity))
                    .setFrameClearDrawable(windowBackground)
                    .setBlurRadius(radius)
                blueView.outlineProvider=ViewOutlineProvider.BACKGROUND
                blueView.clipToOutline = true
            }
        }
    }
    private  fun isNight():Boolean{
        return calendar.get(Calendar.HOUR_OF_DAY)>=18
    }
    private  fun setDinamicallyWallpaper(icon:String):Int{
        return when(icon.dropLast(1)){
            "01" -> {
                initWeatherView(PrecipType.CLEAR)
                R.drawable.sunny_bg
            }
            "02","03","04" -> {
                initWeatherView(PrecipType.CLEAR)
                R.drawable.cloudy_bg
            }
            "09","10","11" -> {
                initWeatherView(PrecipType.RAIN)
                R.drawable.rainy_bg
            }
            "13"-> {
                initWeatherView(PrecipType.SNOW)
                R.drawable.snow_bg
            }
            "50" -> {
                initWeatherView(PrecipType.CLEAR)
                R.drawable.haze_bg
            }
            else ->0


        }
    }
    private  fun setEffectRainSnow(icon:String){
        when(icon.dropLast(1)){
            "01" -> {
                initWeatherView(PrecipType.CLEAR)

            }
            "02","03","04" -> {
                initWeatherView(PrecipType.CLEAR)

            }
            "09","10","11" -> {
                initWeatherView(PrecipType.RAIN)

            }
            "13"-> {
                initWeatherView(PrecipType.SNOW)

            }
            "50" -> {
                initWeatherView(PrecipType.CLEAR)

            }
            else ->0


        }
    }
    private fun initWeatherView(type: PrecipType){
        binding.weatherView.apply {
            setWeatherData(type)
            angle = -20
            emissionRate=100.0f
        }
    }

    private fun loadDatas(lat: Double, lon: Double, s: String) {
        //Curent
        weatherViewModel.loadCurrentWeather(this.lat, this.lon,"metric").enqueue(object :
            retrofit2.Callback<CurrentResponseApi> {
            override fun onResponse(
                call: Call<CurrentResponseApi>,
                response: Response<CurrentResponseApi>
            ) {
                if (response.isSuccessful){
                    val data = response.body()
                    binding.progressBar.visibility = View.GONE
                    binding.detailLayout.visibility=View.VISIBLE
                    data?.let{
                        binding.statusTxt.text=it.weather?.get(0)?.main ?: "-"
                        binding.windTxt.text=it.wind?.speed?.let { Math.round(it).toString() }+" km/h"
                        binding.hummidityTxt.text = it.main?.humidity?.toString()+"%"
                        binding.currentTempTxt.text=it.main?.temp?.let{Math.round(it).toString()}+"°"
                        binding.maxTempTxt.text=it.main?.tempMax?.let{Math.round(it).toString()}+"°"
                        binding.minTempTxt.text=it.main?.tempMin?.let{Math.round(it).toString()}+"°"
                        binding.pressureTxt.text=it.main?.pressure?.toString()+" hPa"
                        binding.feelsTxt.text=it.main?.feelsLike?.let{Math.round(it).toString()}+"°"

                        val drawable = if(isNight()) {R.drawable.night_bg}
                        else{
                            setDinamicallyWallpaper(it.weather?.get(0)?.icon?:"-")
                        }
                        binding.bgImage.setImageResource(drawable)
                        setEffectRainSnow(it.weather?.get(0)?.icon?:"-")
                    }
                }
            }
            override fun onFailure(call: Call<CurrentResponseApi>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_SHORT).show()
            }
        })
        //Forecast
        weatherViewModel.loadForecastWeather(lat,lon, "metric").enqueue(object : retrofit2.Callback<ForecastResponseApi>{
            override fun onResponse(
                call: Call<ForecastResponseApi>,
                response: Response<ForecastResponseApi>
            ) {
                if (response.isSuccessful){
                    val data = response.body()
                    binding.blueView.visibility = View.VISIBLE

                    data?.let {
                        forecastAdapter.differ.submitList(it.list)
                        binding.forecastView.apply {
                            layoutManager=LinearLayoutManager(
                                this@MainActivity,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                            adapter = forecastAdapter
                        }
                    }
                }
            }
            override fun onFailure(call: Call<ForecastResponseApi>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }


    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                val locationRequest = LocationRequest.create().apply {
                    interval = 5000 // 5 másodpercenként frissít
                    fastestInterval = 2000 // 2 másodperc a leggyorsabb frissítés
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                }

                val locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        val location = locationResult.lastLocation
                        if (location != null) {
                            lat = location.latitude
                            lon = location.longitude
                            Toast.makeText(this@MainActivity, "Updated Location: $lat, $lon", Toast.LENGTH_SHORT).show()
                            binding.cityTxt.text = getCityName(lat, lon) // A város neve is frissül
                            loadDatas(lat,lon, "metric")
                            // Leiratkozunk a további frissítésekről
                            mFusedLocationClient.removeLocationUpdates(this)

                        } else {
                            Toast.makeText(this@MainActivity, "Error: Location is still null.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    // A városnév lekérdezése Geocoder segítségével
    private fun getCityName(lat: Double, lon: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address>? = geocoder.getFromLocation(lat, lon, 1)
        return addresses?.get(0)?.locality ?: "Unknown location"
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }

}
