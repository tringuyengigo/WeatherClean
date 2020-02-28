package gdsvn.tringuyen.myapplication.presentation.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gdsvn.tringuyen.myapplication.data.entity.WeatherDayEntity
import gdsvn.tringuyen.myapplication.domain.usecase.GetCurrentWeatherCityUseCase
import gdsvn.tringuyen.myapplication.presentation.common.BaseViewModel
import gdsvn.tringuyen.myapplication.presentation.common.Data
import gdsvn.tringuyen.myapplication.presentation.common.Status
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

class CurrentWeatherViewModel(private val getCurrentWeatherCityUseCase: GetCurrentWeatherCityUseCase) : BaseViewModel() {

    private var mWeather = MutableLiveData<Data<WeatherDayEntity>>()

    fun getWeatherLiveData() = mWeather

    fun fetchWeather(city: String) {

        Timber.d("On fetchWeather() ")
        val disposable = getCurrentWeatherCityUseCase.getWeatherByCity(city = city)
                .subscribe({ response ->
                    mWeather.value = Data(responseType = Status.SUCCESSFUL, data = response)
                }, { error ->
                    mWeather.value = Data(responseType = Status.ERROR, error = Error(error.message))
                }, {
                    Timber.d("On Complete Called")

                })
        addDisposable(disposable)
    }


}