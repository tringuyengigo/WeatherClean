package gdsvn.tringuyen.myapplication.presentation.weather.viewmodel.current

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import gdsvn.tringuyen.myapplication.data.entity.WeatherDayEntity
import gdsvn.tringuyen.myapplication.data.provider.location.LocationModel
import gdsvn.tringuyen.myapplication.data.provider.location.LocationProviderImpl
import gdsvn.tringuyen.myapplication.domain.usecase.GetCurrentWeatherCityUseCase
import gdsvn.tringuyen.myapplication.domain.usecase.GetCurrentWeatherCoordinateUseCase
import gdsvn.tringuyen.myapplication.presentation.common.BaseViewModel
import gdsvn.tringuyen.myapplication.presentation.common.Data
import gdsvn.tringuyen.myapplication.presentation.common.Status
import gdsvn.tringuyen.myapplication.data.provider.units.UnitProviderImpl
import gdsvn.tringuyen.myapplication.data.provider.units.UnitSystem
import gdsvn.tringuyen.myapplication.presentation.weather.viewmodel.location.LocationViewModel
import timber.log.Timber

class CurrentWeatherViewModel(
    private val unitProvider: UnitProviderImpl,
    private val locationProvider: LocationProviderImpl,
    private val getCurrentWeatherCityUseCase: GetCurrentWeatherCityUseCase,
    private val getCurrentWeatherCoordinateUseCase: GetCurrentWeatherCoordinateUseCase,
    private val locationViewModel: LocationViewModel
) : BaseViewModel() {

    private var mWeather = MutableLiveData<Data<WeatherDayEntity>>()

    private val unitSystem = unitProvider.getUnitSystem()

    fun getWeatherLiveData() = mWeather

    val isMetricUnit: Boolean get() = unitSystem == UnitSystem.METRIC

    fun fetchWeather(){
        Timber.d("On fetchWeather()")
        if(locationProvider.isUsingDeviceLocation()) {
            startLocationUpdate()
        } else {
            fetchWeatherCity(locationProvider.getCustomLocationName().toString())
        }
    }

    private fun fetchWeatherCity(city: String) {
        val disposable = getCurrentWeatherCityUseCase.getWeatherByCity(city = city)
                .subscribe({ response ->
                    mWeather.value = Data(responseType = Status.SUCCESSFUL, data = response)
                }, { error ->
                    mWeather.value = Data(responseType = Status.ERROR, error = Error(error.message))
                }, {
                    Timber.d("On fetchWeatherCity() Complete Called")
                    this.onCleared()
                })
        addDisposable(disposable)
    }

    private fun fetchWeatherCoordinate(lon: String, lat: String) {
        val disposable = getCurrentWeatherCoordinateUseCase.getWeatherByCoordinate(lon = lon, lat = lat)
            .subscribe({ response ->
                mWeather.value = Data(responseType = Status.SUCCESSFUL, data = response)
            }, { error ->
                mWeather.value = Data(responseType = Status.ERROR, error = Error(error.message))
            }, {
                Timber.d("On fetchWeatherCoordinate() Complete Called")
                locationViewModel.stopLocationData()
                this.onCleared()
            })
        addDisposable(disposable)
    }

    private fun startLocationUpdate() {
        locationViewModel.getLocationData().observeForever {
            Timber.e("Latitude: ${it.latitude} - Longitude: ${it.longitude}")
            fetchWeatherCoordinate(lon = it.longitude.toString(), lat = it.latitude.toString())
        }
    }

}