package gdsvn.tringuyen.myapplication.data.responsitory


import android.database.SQLException
import gdsvn.tringuyen.myapplication.data.database.WeatherCurrentDao
import gdsvn.tringuyen.myapplication.data.database.WeatherDatabase
import gdsvn.tringuyen.myapplication.data.entity.WeatherDayEntity
import io.reactivex.Flowable
import io.reactivex.Observable
import timber.log.Timber

class WeatherCurrentCacheImpl(private val database: WeatherDatabase)  {

    private val dao: WeatherCurrentDao = database.getCurrentWeatherDao()

    fun saveCurrentWeather(data: WeatherDayEntity) {
        try {
            Timber.e("saveCurrentWeather $data")
            dao.saveCurrentWeather(itemWeather = data)
        } catch (e: SQLException) {
            Timber.e("saveCurrentWeather SQLException ${e.message}")
        }
    }

    fun getCurrentWeatherCity(): Flowable<WeatherDayEntity?> {
        return dao.getLatestCurrentWeather()
    }


}