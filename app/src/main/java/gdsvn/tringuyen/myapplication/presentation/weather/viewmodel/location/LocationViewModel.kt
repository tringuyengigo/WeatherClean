package gdsvn.tringuyen.myapplication.presentation.weather.viewmodel.location

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import gdsvn.tringuyen.myapplication.data.provider.location.LocationLiveData
import gdsvn.tringuyen.myapplication.presentation.common.BaseViewModel
import timber.log.Timber


class LocationViewModel(application: Application) : BaseViewModel() {

    private val locationData = LocationLiveData(application)

    fun getLocationData(): LocationLiveData {
        Timber.e("getLocationData() -- ${locationData.value}")
        return locationData
    }

    fun stopLocationData() {
        locationData.stopLocationLiveData()
    }


}