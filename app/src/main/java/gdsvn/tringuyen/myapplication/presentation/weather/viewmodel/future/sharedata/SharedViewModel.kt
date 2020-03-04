package gdsvn.tringuyen.myapplication.presentation.weather.viewmodel.future.sharedata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gdsvn.tringuyen.myapplication.data.entity.WeatherDayEntity
import gdsvn.tringuyen.myapplication.presentation.common.BaseViewModel

class SharedViewModel() : BaseViewModel(){

    val dataToShare = MutableLiveData<WeatherDayEntity>()
    var isMetricUnit: Boolean = false
    lateinit var location: String
    val dataStringToShare = MutableLiveData<String>()

    fun updateData(data: WeatherDayEntity) {
        dataToShare.value = data
    }

    fun updateMetricUnit(isMetricUnit: Boolean) {
        this.isMetricUnit = isMetricUnit
    }

    fun updateLocation(location: String) {
        this.location = location
    }

    fun updateData(data: String) {
        dataStringToShare.value = data
    }
}