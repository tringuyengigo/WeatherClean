package gdsvn.tringuyen.myapplication.data.entity.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import gdsvn.tringuyen.myapplication.data.entity.*
import java.lang.reflect.Type
import java.util.*
import java.util.Collections.emptyList


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
    fun windDataToString(data: Wind): String = Gson().toJson(data)

    @TypeConverter
    fun windStringToWind(value: String): Wind = Gson().fromJson(value, Wind::class.java)

    @TypeConverter
    fun weatherStringToSomeObjectList(data: String?): List<Weather?>? {
        if (data == null) {
            return emptyList()
        }
        val listType: Type =
            object : TypeToken<List<Weather?>?>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun weatherObjectListToString(someObjects: List<Weather?>?): String? {
        return Gson().toJson(someObjects)
    }

    @TypeConverter
    fun weatherDayEntityStringToSomeObjectList(data: String?): List<WeatherDayEntity?>? {
        if (data == null) {
            return emptyList()
        }
        val listType: Type =
            object : TypeToken<List<WeatherDayEntity?>?>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun weatherDayEntityObjectListToString(someObjects: List<WeatherDayEntity?>?): String? {
        return Gson().toJson(someObjects)
    }

}


