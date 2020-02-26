package gdsvn.tringuyen.myapplication.domain.respository

import gdsvn.tringuyen.myapplication.data.entity.WeatherDayEntity
import gdsvn.tringuyen.myapplication.domain.entity.WeatherDay
import io.reactivex.Flowable

interface WeatherRepository {

    fun getCurrentWeatherCity(city: String): Flowable<WeatherDayEntity?>
    fun getCurrentWeatherCoordinates(lon: String, lat: String): Flowable<WeatherDayEntity>

    fun getWeatherForecastCity(city: String): Flowable<List<WeatherDayEntity>>
    fun getWeatherForecastCoordinates(lon: String, lat: String): Flowable<List<WeatherDayEntity>>

}


