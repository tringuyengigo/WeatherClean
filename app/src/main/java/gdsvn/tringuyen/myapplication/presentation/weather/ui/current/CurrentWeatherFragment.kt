package gdsvn.tringuyen.myapplication.presentation.weather.ui.current

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.view.GestureDetector.OnDoubleTapListener
import android.view.GestureDetector.SimpleOnGestureListener
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.gson.Gson
import gdsvn.tringuyen.myapplication.R
import gdsvn.tringuyen.myapplication.data.entity.WeatherDayEntity
import gdsvn.tringuyen.myapplication.presentation.common.Status
import gdsvn.tringuyen.myapplication.presentation.weather.viewmodel.current.CurrentWeatherViewModel
import kotlinx.android.synthetic.main.current_weather_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class CurrentWeatherFragment : Fragment()  {

    companion object {
        fun newInstance() = CurrentWeatherFragment()
    }

    private val weatherViewModel: CurrentWeatherViewModel by viewModel()
    var gestureDetector: GestureDetector? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.current_weather_fragment, container, false)
        gestureDetector = GestureDetector(
            context,
            GestureListener()
        )
        gestureDetector!!.setOnDoubleTapListener(object : OnDoubleTapListener {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                Timber.e("onDoubleTap()")
                return false
            }

            override fun onDoubleTapEvent(e: MotionEvent): Boolean {
                Timber.e("onDoubleTapEvent()")
                group_loading.visibility = View.VISIBLE
                weatherViewModel.fetchWeather()
                return false
            }

            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                Timber.e("onSingleTapConfirmed()")
                return false;
            }
        })
        view.setOnTouchListener { _, event ->
            gestureDetector?.onTouchEvent(event)!!
            true
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        weatherViewModel.fetchWeather()
    }

    override fun onStart() {
        super.onStart()
        weatherViewModel.getWeatherLiveData().observe(this, Observer { data ->
            when (data?.responseType) {
                Status.ERROR -> {
                    Timber.v("Loading data........... ERROR ${data.error}")
                }
                Status.LOADING -> {
                    Timber.v("Loading data........... LOADING")
                    group_loading.visibility = View.VISIBLE
                }
                Status.SUCCESSFUL -> {
                    Timber.v("Loading data........... SUCCESSFUL")
                    group_loading.visibility = View.GONE
                }
            }
            data?.data?.let {
                Timber.v("Data Weather at WeatherActivity ${Gson().toJson(it)}")
                updateCurrentWeatherUI(it)
            }
        })
    }

    private fun updateCurrentWeatherUI(data: WeatherDayEntity) {
        updateLocation(data)
        loadImage(data.weather[0].main)
        updateTemperatures(data.main.temp, data.main.feels_like)
        updateDescription(stringCapitalizeFirstLetter(data.weather[0].description))
        updateVisibility(data.visibility)
        updatePressure(data.main.pressure)
        updateWind(data.wind.speed)
        updateHumidity(data.main.humidity)
        updateSunrise(data)
        updateSunset(data)
    }

    @SuppressLint("SetTextI18n")
    private fun updateSunrise(data: WeatherDayEntity) {
        textView_sunrise.text = "Sunrise: ${convertUnixTimeStampToDate(data.sys.sunrise)}"
    }

    @SuppressLint("SetTextI18n")
    private fun updateSunset(data: WeatherDayEntity) {
        textView_sunset.text = "Sunset: ${convertUnixTimeStampToDate(data.sys.sunset)}"
    }

    private fun updateLocation(data: WeatherDayEntity) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = "${data.name}, ${data.sys.country}"
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Today"
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
            textView_visibility.text = "Visibility: ${visibilityDistance} $unitAbbreviation"
        }
    }

    private fun updateDescription(stringCapitalizeFirstLetter: String) {
        textView_condition.text = stringCapitalizeFirstLetter
    }

    private fun updateTemperatures(temperature: Double, feelsLike: Double) {
        var temperatureConv = temperature
        var feelsLikeConv = feelsLike
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
        if(unitAbbreviation == "°C") {
            temperatureConv = convertFahrenheitToCelsius(temperature)
            feelsLikeConv = convertFahrenheitToCelsius(feelsLike)
        } else {
            temperatureConv = temperature
            feelsLikeConv = feelsLike
        }
        textView_temperature.text = "${Math.round(temperatureConv)}$unitAbbreviation"
        textView_feels_like_temperature.text = "Feels like ${Math.round(feelsLikeConv)}$unitAbbreviation"
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
        return if (weatherViewModel.isMetricUnit) metric else imperial
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
        val sdf = SimpleDateFormat("HH:mm:ss")
        sdf.timeZone = TimeZone.getTimeZone("GMT-4")
        return sdf.format(date)
    }

    class GestureListener() : SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            Timber.v("onDoubleTap $e")
            return true
        }
    }
}

