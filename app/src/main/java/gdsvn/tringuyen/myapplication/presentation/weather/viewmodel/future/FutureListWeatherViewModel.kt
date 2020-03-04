package gdsvn.tringuyen.myapplication.presentation.weather.viewmodel.future

import androidx.lifecycle.MutableLiveData
import gdsvn.tringuyen.myapplication.data.entity.WeatherDayEntity
import gdsvn.tringuyen.myapplication.data.entity.WeatherForecastEntity
import gdsvn.tringuyen.myapplication.data.provider.location.LocationLiveData
import gdsvn.tringuyen.myapplication.data.provider.location.LocationProviderImpl
import gdsvn.tringuyen.myapplication.domain.usecase.GetWeatherForecastCityUseCase
import gdsvn.tringuyen.myapplication.presentation.common.BaseViewModel
import gdsvn.tringuyen.myapplication.presentation.common.Data
import gdsvn.tringuyen.myapplication.data.provider.units.UnitProviderImpl
import gdsvn.tringuyen.myapplication.data.provider.units.UnitSystem
import gdsvn.tringuyen.myapplication.domain.usecase.GetCurrentWeatherCoordinateUseCase
import gdsvn.tringuyen.myapplication.domain.usecase.GetWeatherForecastCoordinateUseCase
import gdsvn.tringuyen.myapplication.presentation.common.Status
import gdsvn.tringuyen.myapplication.presentation.weather.ui.future.list.adapter.WeatherListAdapter
import gdsvn.tringuyen.myapplication.presentation.weather.viewmodel.location.LocationViewModel
import timber.log.Timber


class FutureListWeatherViewModel(
    private val unitProvider: UnitProviderImpl,
    private val locationProvider: LocationProviderImpl,
    private val getWeatherForecastCityUseCase: GetWeatherForecastCityUseCase,
    private val getWeatherForecastCoordinateUseCase: GetWeatherForecastCoordinateUseCase,
    private val locationViewModel: LocationViewModel
) : BaseViewModel() {

    private var mWeatherForecast = MutableLiveData<Data<WeatherForecastEntity>>()
    private val unitSystem = unitProvider.getUnitSystem()
    fun getWeatherForecastLiveData() = mWeatherForecast

    fun fetchWeatherForecast(){
        Timber.d("On fetchWeatherForecast()")
        if(locationProvider.isUsingDeviceLocation()) {
            startLocationUpdate()
        } else {
            fetchWeatherForecastCity(locationProvider.getCustomLocationName().toString())
        }
    }

    private fun fetchWeatherForecastCity(city: String) {
        Timber.d("On fetchWeatherForecastCity() ")
        val disposable = getWeatherForecastCityUseCase.getWeatherForecastByCity(city = city)
            .subscribe({ response ->
                mWeatherForecast.value = Data(responseType = Status.SUCCESSFUL, data = response)
            }, { error ->
                mWeatherForecast.value = Data(responseType = Status.ERROR, error = Error(error.message))
            }, {
                Timber.d("On fetchWeatherForecastCity() Complete Called")
                this.onCleared()
            })
        addDisposable(disposable)
    }

    private fun fetchWeatherForecastCoordinate(lon: String, lat: String) {
        val disposable = getWeatherForecastCoordinateUseCase.getWeatherForecastByCoordinate(lon = lon, lat = lat)
            .subscribe({ response ->
                mWeatherForecast.value = Data(responseType = Status.SUCCESSFUL, data = response)
            }, { error ->
                mWeatherForecast.value = Data(responseType = Status.ERROR, error = Error(error.message))
            }, {
                Timber.d("On fetchWeatherCoordinate() Complete Called")
                this.onCleared()
            })
        addDisposable(disposable)
    }


    private fun startLocationUpdate() {
        locationViewModel.getLocationData().observeForever {
            Timber.e("Latitude: ${it.latitude} - Longitude: ${it.longitude}")
            fetchWeatherForecastCoordinate(it.longitude.toString(), it.latitude.toString())
        }
    }

    val isMetricUnit: Boolean get() = unitSystem == UnitSystem.METRIC
}