package gdsvn.tringuyen.myapplication.presentation.weather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.gson.Gson
import gdsvn.tringuyen.myapplication.R
import gdsvn.tringuyen.myapplication.data.entity.WeatherDayEntity
import gdsvn.tringuyen.myapplication.presentation.common.Status
import gdsvn.tringuyen.myapplication.presentation.weather.viewmodel.CurrentWeatherViewModel
import kotlinx.android.synthetic.main.current_weather_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber


class CurrentWeatherFragment : Fragment() {

    companion object {
        fun newInstance() = CurrentWeatherFragment()
    }

    private val weatherViewModel: CurrentWeatherViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
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



        updateTemperatures(data!!.main.temp, data!!.main.feels_like)

        (activity as? AppCompatActivity)?.supportActionBar?.title = data!!.name
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Today"
        textView_condition.text = stringCapitalizeFirstLetter(data.weather[0].description)
        loadImage(data?.weather[0].main)
    }

    private fun updateTemperatures(temperature: Double, feelsLike: Double) {
        var temperatureConv = 0
        var feelsLikeConv = 0

        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
        Timber.e("updateTemperatures $unitAbbreviation")

        if(unitAbbreviation == "°C") {
            temperatureConv = convertFahrenheitToCelsius(temperature).toInt()
            feelsLikeConv = convertFahrenheitToCelsius(feelsLike).toInt()
        } else {
            temperatureConv = temperature.toInt()
            feelsLikeConv = feelsLike.toInt()
        }

        textView_temperature.text = "${temperatureConv}$unitAbbreviation"
        textView_feels_like_temperature.text = "Feels like $feelsLikeConv$unitAbbreviation"
    }

    private fun convertCelsiusToFahrenheit(celcious: Double): Double {
        return (celcious * 9.0f / 5.0f + 32)
    }

    private fun convertFahrenheitToCelsius(fahrenheit: Double): Double {
        return (fahrenheit - 32) *  5.0f / 9.0f
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


}