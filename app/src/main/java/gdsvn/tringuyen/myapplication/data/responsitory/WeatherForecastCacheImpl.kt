package gdsvn.tringuyen.myapplication.data.responsitory


import android.database.SQLException
import com.google.gson.Gson
import gdsvn.tringuyen.myapplication.data.database.WeatherCurrentDao
import gdsvn.tringuyen.myapplication.data.database.WeatherDatabase
import gdsvn.tringuyen.myapplication.data.database.WeatherForecastDao
import gdsvn.tringuyen.myapplication.data.entity.WeatherDayEntity
import gdsvn.tringuyen.myapplication.data.entity.WeatherForecastEntity
import io.reactivex.Flowable
import io.reactivex.Observable
import timber.log.Timber

class WeatherForecastCacheImpl(private val database: WeatherDatabase)  {

    private val dao: WeatherForecastDao = database.getWeatherForecastDao()

    fun saveWeatherForecast(data: WeatherForecastEntity) {
        try {
            dao.saveWeatherForecast(weatherForecast = data)
        } catch (e: SQLException) {
            Timber.e("saveWeatherForecast SQLException ${e.message}")
        }
    }

    fun getWeatherForecast(): Flowable<WeatherForecastEntity> {
        return dao.getWeatherForecast()
    }


}