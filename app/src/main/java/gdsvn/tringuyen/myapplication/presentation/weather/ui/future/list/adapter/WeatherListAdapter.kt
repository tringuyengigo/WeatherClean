package gdsvn.tringuyen.myapplication.presentation.weather.ui.future.list.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gdsvn.tringuyen.myapplication.R
import gdsvn.tringuyen.myapplication.data.entity.WeatherDayEntity
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.android.synthetic.main.item_future_weather.view.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class WeatherListAdapter(metricUnit: Boolean) : RecyclerView.Adapter<WeatherListAdapter.WeatherViewHolder>() {

    var weather = mutableListOf<WeatherDayEntity>()
    var metricUnit = metricUnit

    private var listener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        Timber.v("onCreateViewHolder ")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_future_weather, parent, false)
        return WeatherViewHolder(view, listener, metricUnit = metricUnit)
    }

    fun setOnItemClickListener(listener: ItemClickListener?) {
        this.listener = listener
    }

    override fun getItemCount(): Int {
        return weather.size
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(weather[position])
        holder.position = position
        holder.setItemWeather(weather[position])
    }

    fun updateList(itemWeather: WeatherDayEntity) {
        weather.add(itemWeather)
        notifyDataSetChanged()
    }


    class WeatherViewHolder(
        itemView: View,
        listener: ItemClickListener?,
        metricUnit: Boolean?
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var view: View = itemView
        private var pos: Int = 0
        private lateinit var itemWeatherDaySelected: WeatherDayEntity
        var listener: ItemClickListener? = null
        private var metricUnit = metricUnit


        init {
            view.setOnClickListener(this)
            this.listener = listener
        }

        fun setPosition(position: Int){
            this.pos = position
        }

        fun setItemWeather(itemWeatherDay: WeatherDayEntity){
            this.itemWeatherDaySelected = itemWeatherDay
        }

        fun bind(itemWeatherDay: WeatherDayEntity) {
            with(itemView) {
                updateDate(this ,convertUnixTimeStampToDate(itemWeatherDay.dt))
                updateTemperatures(this, itemWeatherDay.main.temp)
                loadImage(this, itemWeatherDay.weather[0].main)
                updateDescription(this, itemWeatherDay.weather[0].main)
            }
        }

        private fun updateDescription(view: View, main: String) {
            view.textView_condition.text = main
        }

        override fun onClick(v: View?) {
            this.listener?.onClick(v , pos,false, itemWeatherDaySelected)
        }

        @SuppressLint("SetTextI18n")
        private fun updateTemperatures(view: View, temp: Double) {
            var temperatureConv = temp
            val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
            temperatureConv = if(unitAbbreviation == "°C") {
                convertFahrenheitToCelsius(temp)
            } else {
                temp
            }
            view.textView_temperature.text = "${temperatureConv.roundToInt()}$unitAbbreviation"
        }

        private fun updateDate(view: View, convertUnixTimeStampToDate: String) {
            view.textView_date.text = convertUnixTimeStampToDate
        }

        //Should get icon's link to show image
        private fun loadImage(view: View, icon: String) {
            var icon : String = icon.toLowerCase()
            if(icon.contains("clear")) {
                view.imageView_condition_icon.setImageResource(R.drawable.ic_weather_sunny)
            } else if (icon.contains("rain")) {
                view.imageView_condition_icon.setImageResource(R.drawable.ic_weather_sunny)
            } else if (icon.contains("cloud")) {
                view.imageView_condition_icon.setImageResource(R.drawable.ic_weather_sunny)
            } else if (icon.contains("drizzle")) {
                view.imageView_condition_icon.setImageResource(R.drawable.ic_weather_sunny)
            } else if (icon.contains("extreme")) {
                view.imageView_condition_icon.setImageResource(R.drawable.ic_weather_sunny)
            } else if (icon.contains("snow")) {
                view.imageView_condition_icon.setImageResource(R.drawable.ic_weather_sunny)
            } else if (icon.contains("thunderstorm")) {
                view.imageView_condition_icon.setImageResource(R.drawable.ic_weather_sunny)
            } else if (icon.contains("atmosphere")) {
                view.imageView_condition_icon.setImageResource(R.drawable.ic_weather_sunny)
            }  else {
                view.imageView_condition_icon.setImageResource(R.drawable.ic_weather_sunny)
            }
        }

        private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String {
            return if (this.metricUnit!!) metric else imperial
        }

        @SuppressLint("SimpleDateFormat")
        private fun convertUnixTimeStampToDate(unixTime: Int): String {
            val unixSeconds: Int = unixTime
            val date = Date(unixSeconds * 1000L)
            val sdf = SimpleDateFormat("hh:mm:ss \nE, dd MMM yyyy")
            sdf.timeZone = TimeZone.getTimeZone(TimeZone.getDefault().displayName)
            return sdf.format(date)
        }

        private fun convertCelsiusToFahrenheit(celcious: Double): Double {
            return (celcious * 9.0f / 5.0f + 32)
        }

        private fun convertFahrenheitToCelsius(fahrenheit: Double): Double {
            return (fahrenheit - 32) *  5.0f / 9.0f
        }
    }
}