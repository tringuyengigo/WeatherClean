package gdsvn.tringuyen.myapplication.data.api

import gdsvn.tringuyen.myapplication.data.entity.WeatherDayEntity
import gdsvn.tringuyen.myapplication.data.entity.WeatherForecastEntity
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiWeather {

    @GET("/data/2.5/weather")
    fun getCurrentWeatherByCity(@Query("q") city: String) : Flowable<WeatherDayEntity>

    @GET("/data/2.5/weather")
    fun getCurrentWeatherByCoordinate(@Query("lat") lat: String,
                                      @Query("lon") lon: String): Flowable<WeatherDayEntity>

    @GET("/data/2.5/forecast")
    fun getWeatherForecastByCity(@Query("q") city: String): Flowable<WeatherForecastEntity>

    @GET("/data/2.5/forecast")
    fun getWeatherForecastByCoordinate(@Query("lat") lat: String,
                                       @Query("lon") lon: String): Flowable<WeatherForecastEntity>
}