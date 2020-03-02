package gdsvn.tringuyen.myapplication.presentation.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gdsvn.tringuyen.myapplication.data.entity.WeatherDayEntity
import gdsvn.tringuyen.myapplication.domain.usecase.GetCurrentWeatherCityUseCase
import gdsvn.tringuyen.myapplication.domain.usecase.GetCurrentWeatherCoordinateUseCase
import gdsvn.tringuyen.myapplication.domain.usecase.GetWeatherForecastCoordinateUseCase
import gdsvn.tringuyen.myapplication.presentation.common.BaseViewModel
import gdsvn.tringuyen.myapplication.presentation.common.Data
import gdsvn.tringuyen.myapplication.presentation.common.Status
import gdsvn.tringuyen.myapplication.presentation.common.provider.UnitProviderImpl
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

class CurrentWeatherViewModel(
    private val unitProvider: UnitProviderImpl,
    private val getCurrentWeatherCityUseCase: GetCurrentWeatherCityUseCase,
    private val getCurrentWeatherCoordinateUseCase: GetCurrentWeatherCoordinateUseCase

) : BaseViewModel() {

    private var mWeather = MutableLiveData<Data<WeatherDayEntity>>()
    private val unitSystem = unitProvider.getUnitSystem()

    fun getWeatherLiveData() = mWeather


    fun fetchWeatherCity(city: String) {
        Timber.d("On fetchWeatherCity() ")
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

    fun fetchWeatherCoordinate(city: String) {

        Timber.d("On fetchWeatherCoordinate() ${unitSystem.name}" )
        val disposable = getCurrentWeatherCoordinateUseCase.getWeatherByCoordinate(lon = "-0.13", lat = "51.51")
            .subscribe({ response ->
                mWeather.value = Data(responseType = Status.SUCCESSFUL, data = response)
            }, { error ->
                mWeather.value = Data(responseType = Status.ERROR, error = Error(error.message))
            }, {
                Timber.d("On fetchWeatherCoordinate() Complete Called")
                this.onCleared()
            })
        addDisposable(disposable)
    }


}