package gdsvn.tringuyen.myapplication.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gdsvn.tringuyen.myapplication.data.entity.WeatherDayEntity
import io.reactivex.Flowable

@Dao
interface WeatherCurrentDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCurrentWeather(itemWeather: WeatherDayEntity?)

    @Query("SELECT * FROM weather_day ORDER BY `index` DESC LIMIT 1")
    fun getLatestCurrentWeather(): Flowable<WeatherDayEntity?>

    @Query("DELETE FROM weather_day")
    fun clear()

}