package gdsvn.tringuyen.myapplication.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import gdsvn.tringuyen.myapplication.data.entity.WeatherDayEntity
import gdsvn.tringuyen.myapplication.data.entity.WeatherForecastEntity
import gdsvn.tringuyen.myapplication.data.entity.converters.DataConverters


@Database(entities = [WeatherDayEntity::class, WeatherForecastEntity::class], version = 1)
@TypeConverters(DataConverters::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun getCurrentWeatherDao(): WeatherCurrentDao
    abstract fun getWeatherForecastDao(): WeatherForecastDao
}