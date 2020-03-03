package gdsvn.tringuyen.myapplication.data.responsitory


import gdsvn.tringuyen.myapplication.data.api.ApiWeather
import gdsvn.tringuyen.myapplication.data.entity.WeatherDayEntity
import gdsvn.tringuyen.myapplication.data.entity.WeatherForecastEntity
import io.reactivex.Flowable

class WeatherRemoteImpl constructor(private val api: ApiWeather) {

    fun getCurrentWeatherByCity(city: String): Flowable<WeatherDayEntity> {
        return api.getCurrentWeatherByCity(city = city)
    }

    fun getCurrentWeatherByCoordinate(lon: String, lat: String): Flowable<WeatherDayEntity> {
        return api.getCurrentWeatherByCoordinate(lon = lon, lat = lat)
    }

    fun getWeatherForecastByCoordinate(lon: String, lat: String): Flowable<WeatherForecastEntity> {
        return api.getWeatherForecastByCoordinate(lon = lon, lat = lat)
    }

    fun getWeatherForecastByCity(city: String): Flowable<WeatherForecastEntity> {
        return api.getWeatherForecastByCity(city = city)
    }


}