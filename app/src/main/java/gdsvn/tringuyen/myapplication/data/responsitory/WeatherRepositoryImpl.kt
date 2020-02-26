package gdsvn.tringuyen.myapplication.data.responsitory

import gdsvn.tringuyen.myapplication.data.entity.WeatherDayEntity
import gdsvn.tringuyen.myapplication.domain.respository.WeatherRepository
import io.reactivex.Flowable

class WeatherRepositoryImpl(private val remote: WeatherRemoteImpl,
                            private val cacheWeatherCurrent: WeatherCurrentCacheImpl,
                            private val cacheWeatherForecast: WeatherForecastCacheImpl
) : WeatherRepository {

    override fun getCurrentWeatherCity(city: String): Flowable<WeatherDayEntity?> {
        val updateItemCurrentWeatherFlowable = remote.getCurrentWeatherByCity(city = city)
        return cacheWeatherCurrent.getCurrentWeatherCity().mergeWith(updateItemCurrentWeatherFlowable.doOnNext {
            cacheWeatherCurrent.saveCurrentWeather(it)
        })
    }

    override fun getCurrentWeatherCoordinates(
        lon: String,
        lat: String
    ): Flowable<WeatherDayEntity> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getWeatherForecastCity(city: String): Flowable<List<WeatherDayEntity>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getWeatherForecastCoordinates(
        lon: String,
        lat: String
    ): Flowable<List<WeatherDayEntity>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}