package gdsvn.tringuyen.myapplication.presentation.weather.ui.future.detail

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import gdsvn.tringuyen.myapplication.R
import gdsvn.tringuyen.myapplication.data.entity.WeatherDayEntity
import gdsvn.tringuyen.myapplication.presentation.weather.viewmodel.future.FutureDetailWeatherViewModel
import gdsvn.tringuyen.myapplication.presentation.weather.viewmodel.future.sharedata.SharedViewModel
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.android.synthetic.main.future_detail_weather_fragment.*
import kotlinx.android.synthetic.main.future_detail_weather_fragment.imageView_condition_icon
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_condition
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_humidity
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_pressure
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_sunrise
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_sunset
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_temperature
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_visibility
import kotlinx.android.synthetic.main.future_detail_weather_fragment.textView_wind
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class FutureDetailWeatherFragment : Fragment() {

    companion object {
        fun newInstance() = FutureDetailWeatherFragment()
    }

    private lateinit var shareDataVM: SharedViewModel
    var isMetricUnit : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_detail_weather_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.e("On Create FutureDetailWeatherFragment")
        shareDataVM = ViewModelProviders.of(activity!!).get(SharedViewModel::class.java)
        this.isMetricUnit = shareDataVM.isMetricUnit
        updateLocation(shareDataVM.location)
        shareDataVM.dataToShare.observe(this, Observer<WeatherDayEntity> { data ->
            Timber.e("Data at FutureDetailWeatherFragment ShareViewModel $data")
            loadImage(data.weather[0].main)
            updateDate(convertUnixTimeStampToDate(data.dt))
            updateTemperatures(data.main.temp)
            updateTemperaturesMaxMin(data.main.temp_max, data.main.temp_min)
            updateDescription(stringCapitalizeFirstLetter(data.weather[0].description))
            updateVisibility(data.visibility)
            updatePressure(data.main.pressure)
            updateWind(data.wind.speed)
            updateHumidity(data.main.humidity)
//            updateSunrise(data)
//            updateSunset(data)
        })
    }

    private fun updateDate(convertUnixTimeStampToDate: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = convertUnixTimeStampToDate
    }

    private fun updateDescription(stringCapitalizeFirstLetter: String) {
        textView_condition.text = stringCapitalizeFirstLetter
    }

    @SuppressLint("SetTextI18n")
    private fun updateTemperaturesMaxMin(tempMax: Double, tempMin: Double) {
        var tempMaxConv = tempMax
        var tempMinConv = tempMin
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
        if(unitAbbreviation == "°C") {
            tempMaxConv = convertFahrenheitToCelsius(tempMax)
            tempMinConv = convertFahrenheitToCelsius(tempMin)
        } else {
            tempMaxConv = tempMax
            tempMinConv = tempMin
        }
        textView_min_max_temperature.text = "Min: ${tempMinConv.roundToInt()}$unitAbbreviation, Max: ${tempMaxConv.roundToInt()}$unitAbbreviation"

    }

    private fun updateTemperatures(temperature: Double) {
        var temperatureConv = temperature
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
        if(unitAbbreviation == "°C") {
            temperatureConv = convertFahrenheitToCelsius(temperature)
        } else {
            temperatureConv = temperature
        }
        textView_temperature.text = "${Math.round(temperatureConv)}$unitAbbreviation"
    }

    @SuppressLint("SetTextI18n")
    private fun updateSunrise(data: WeatherDayEntity) {
        textView_sunrise.text = "Sunrise: ${convertUnixTimeStampToDate(data.sys.sunrise)}"
    }

    @SuppressLint("SetTextI18n")
    private fun updateSunset(data: WeatherDayEntity) {
        textView_sunset.text = "Sunset: ${convertUnixTimeStampToDate(data.sys.sunset)}"
    }

    private fun updateLocation(data: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = data

    }

    @SuppressLint("SetTextI18n")
    private fun updateHumidity(humidity: Int) {
        textView_humidity.text = "Humidity: $humidity %"
    }

    @SuppressLint("SetTextI18n")
    private fun updatePressure(pressure: Int) {
        textView_pressure.text = "Pressure: $pressure hpa"
    }

    @SuppressLint("SetTextI18n")
    private fun updateWind(windSpeed: Double) {
        textView_wind.text = "Wind: $windSpeed m/s"
    }

    @SuppressLint("SetTextI18n")
    private fun updateVisibility(visibilityDistance: Int) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("km", "mil.")
        Timber.e("updateVisibility $unitAbbreviation")
        if(unitAbbreviation == "km")  {
            textView_visibility.text = "Visibility: ${convertMilToKm(visibilityDistance)} $unitAbbreviation"
        } else {
            textView_visibility.text = "Visibility: $visibilityDistance $unitAbbreviation"
        }
    }

    //Should get icon's link to show image
    private fun loadImage(icon: String) {
        var icon : String = icon.toLowerCase()
        if(icon.contains("clear")) {
            imageView_condition_icon.setImageResource(R.drawable.ic_clear_web);
        } else if (icon.contains("rain")) {
            imageView_condition_icon.setImageResource(R.drawable.ic_rain_web);
        } else if (icon.contains("cloud")) {
            imageView_condition_icon.setImageResource(R.drawable.ic_cloudy_web);
        } else if (icon.contains("drizzle")) {
            imageView_condition_icon.setImageResource(R.drawable.ic_drizzle_web);
        } else if (icon.contains("extreme")) {
            imageView_condition_icon.setImageResource(R.drawable.ic_drizzle_web);
        } else if (icon.contains("snow")) {
            imageView_condition_icon.setImageResource(R.drawable.ic_snow_web);
        } else if (icon.contains("thunderstorm")) {
            imageView_condition_icon.setImageResource(R.drawable.ic_thunderstorm_web);
        } else if (icon.contains("atmosphere")) {
            imageView_condition_icon.setImageResource(R.drawable.ic_atmosphere_web);
        }  else {
            imageView_condition_icon.setImageResource(R.drawable.ic_weather_sunny);
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Detail Weather Of Day"
    }



    //Should create Tool Class for this function
    private fun stringCapitalizeFirstLetter(str: String) : String {
        val str = str
        val words = str.split(" ").toMutableList()
        var output = ""
        for(word in words){
            output += word.capitalize() +" "
        }
        output = output.trim()
        return output
    }

    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String {
        return if (this.isMetricUnit) metric else imperial
    }

    private fun convertCelsiusToFahrenheit(celcious: Double): Double {
        return (celcious * 9.0f / 5.0f + 32)
    }

    private fun convertFahrenheitToCelsius(fahrenheit: Double): Double {
        return (fahrenheit - 32) *  5.0f / 9.0f
    }

    private fun convertMilToKm(met: Int): Int {
        return (met / 1000).toDouble().roundToInt()
    }

    private fun convertMilPerHourToKmPerHour(metPerHour: Double): Double {
        return 3.6 * metPerHour
    }

    private fun convertUnixTimeStampToDate(unixTime: Int): String {
        val unixSeconds: Int = unixTime
        val date = Date(unixSeconds * 1000L)
        val sdf = SimpleDateFormat("hh:mm:ss \nE, dd MMM yyyy")
        sdf.timeZone = TimeZone.getTimeZone("GMT-4")
        return sdf.format(date)
    }



}