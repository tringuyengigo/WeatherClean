package gdsvn.tringuyen.myapplication.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gdsvn.tringuyen.myapplication.data.entity.WeatherDayEntity
import gdsvn.tringuyen.myapplication.data.entity.WeatherForecastEntity
import io.reactivex.Flowable

@Dao
interface WeatherForecastDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveWeatherForecast(weatherForecast: WeatherForecastEntity)

    @Query("SELECT * FROM weather_forecast ORDER BY id DESC LIMIT 1")
    fun getWeatherForecast(): Flowable<WeatherForecastEntity>

    @Query("DELETE FROM weather_forecast")
    fun clear()

}