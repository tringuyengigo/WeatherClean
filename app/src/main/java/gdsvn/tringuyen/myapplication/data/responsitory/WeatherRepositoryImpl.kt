package gdsvn.tringuyen.myapplication.data.responsitory

import gdsvn.tringuyen.myapplication.data.entity.WeatherDayEntity
import gdsvn.tringuyen.myapplication.data.entity.WeatherForecastEntity
import gdsvn.tringuyen.myapplication.domain.respository.WeatherRepository
import io.reactivex.Flowable
import timber.log.Timber

class WeatherRepositoryImpl(private val remote: WeatherRemoteImpl,
                            private val cacheWeatherCurrent: WeatherCurrentCacheImpl,
                            private val cacheWeatherForecast: WeatherForecastCacheImpl
) : WeatherRepository {

//    override fun getCurrentWeatherCity(city: String): Flowable<WeatherDayEntity?> {
//        val updateItemCurrentWeatherFlowable = remote.getCurrentWeatherByCity(city = city)
//        return cacheWeatherCurrent.getCurrentWeatherCity().mergeWith(updateItemCurrentWeatherFlowable.doOnNext {
//            cacheWeatherCurrent.saveCurrentWeather(it)
//        })
//    }

    override fun getCurrentWeatherCity(city: String): Flowable<WeatherDayEntity?> {
        return remote.getCurrentWeatherByCity(city = city).map { it }
    }

    override fun getCurrentWeatherCoordinates(
        lon: String,
        lat: String
    ): Flowable<WeatherDayEntity> {
        return remote.getCurrentWeatherByCoordinate(lon = lon, lat = lat).map { it }
    }

    override fun getWeatherForecastCity(city: String): Flowable<WeatherForecastEntity> {
       return remote.getWeatherForecastByCity(city).map {
           Timber.e("getWeatherForecastCity ${it.weather_forecast}")
           it
       }
    }

    override fun getWeatherForecastCoordinates(
        lon: String,
        lat: String
    ): Flowable<List<WeatherDayEntity>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
