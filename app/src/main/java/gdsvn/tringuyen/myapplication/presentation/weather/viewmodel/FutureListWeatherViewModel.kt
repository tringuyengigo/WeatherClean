package gdsvn.tringuyen.myapplication.presentation.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import gdsvn.tringuyen.myapplication.data.entity.WeatherDayEntity
import gdsvn.tringuyen.myapplication.domain.usecase.GetWeatherForecastCityUseCase
import gdsvn.tringuyen.myapplication.presentation.common.BaseViewModel
import gdsvn.tringuyen.myapplication.presentation.common.Data
import gdsvn.tringuyen.myapplication.data.provider.units.UnitProviderImpl
import timber.log.Timber


class FutureListWeatherViewModel(
    private val unitProvider: UnitProviderImpl,
    private val getWeatherForecastCityUseCase: GetWeatherForecastCityUseCase
) : BaseViewModel() {
    private var mWeather = MutableLiveData<Data<WeatherDayEntity>>()
    private val unitSystem = unitProvider.getUnitSystem()

    fun getWeatherLiveData() = mWeather


    fun fetchWeatherCity(city: String) {
        Timber.d("On fetchWeatherCity() ")
        val disposable = getWeatherForecastCityUseCase.getWeatherForecastByCity(city = "London").subscribe {
            Timber.d("On fetchWeatherCity() ${it}")
        }
        addDisposable(disposable)
    }
}