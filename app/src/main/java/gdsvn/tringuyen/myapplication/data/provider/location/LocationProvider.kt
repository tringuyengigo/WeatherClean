package gdsvn.tringuyen.myapplication.data.provider.location

import gdsvn.tringuyen.myapplication.data.entity.WeatherDayEntity


interface LocationProvider {
    fun hasLocationChanged(lastWeatherLocation: WeatherDayEntity): Boolean
    fun getPreferredLocationString(): String
}