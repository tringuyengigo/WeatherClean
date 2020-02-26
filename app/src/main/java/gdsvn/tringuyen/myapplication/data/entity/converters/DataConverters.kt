package gdsvn.tringuyen.myapplication.data.entity.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import gdsvn.tringuyen.myapplication.data.entity.*
import java.lang.reflect.Type
import java.util.*


class DataConverters {

    @TypeConverter
    fun cloudsToString(data: Clouds): String = Gson().toJson(data)

    @TypeConverter
    fun cloudsStringToClouds(value: String): Clouds = Gson().fromJson(value, Clouds::class.java)

    @TypeConverter
    fun coordDataToString(data: Coord): String = Gson().toJson(data)

    @TypeConverter
    fun coordStringToCoord(value: String): Coord = Gson().fromJson(value, Coord::class.java)

    @TypeConverter
    fun mainDataToString(data: Main): String = Gson().toJson(data)

    @TypeConverter
    fun mainStringToMain(value: String): Main = Gson().fromJson(value, Main::class.java)

    @TypeConverter
    fun sysDataToString(data: Sys): String = Gson().toJson(data)

    @TypeConverter
    fun sysStringToSys(value: String): Sys = Gson().fromJson(value, Sys::class.java)

    @TypeConverter
    fun weatherDataToString(data: Weather): String = Gson().toJson(data)

    @TypeConverter
    fun weatherStringToWeather(value: String): Weather = Gson().fromJson(value, Weather::class.java)

    @TypeConverter
    fun windDataToString(data: Wind): String = Gson().toJson(data)

    @TypeConverter
    fun windStringToWind(value: String): Wind = Gson().fromJson(value, Wind::class.java)

    @TypeConverter
    fun weatherForecastDataToString(data: WeatherForecastEntity): String = Gson().toJson(data)

    @TypeConverter
    fun weatherForecastStringToWeatherForecast(value: String): WeatherForecastEntity = Gson().fromJson(value, WeatherForecastEntity::class.java)

    @TypeConverter
    fun weatherDayEntityDataToString(data: WeatherDayEntity): String = Gson().toJson(data)

    @TypeConverter
    fun weatherDayEntityStringToWeatherDayEntity(value: String): WeatherDayEntity = Gson().fromJson(value, WeatherDayEntity::class.java)

    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<String?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type =
            object : TypeToken<List<String?>?>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<String?>?): String? {
        return Gson().toJson(someObjects)
    }
}

