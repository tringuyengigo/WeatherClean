package gdsvn.tringuyen.myapplication.presentation.weather.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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
        fun newInstance() =
            CurrentWeatherFragment()
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
//        weatherViewModel.fetchWeatherCity("London")
          weatherViewModel.fetchWeatherCoordinate("")
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
        textView_temperature.text = data!!.main.temp.toString()
        (activity as? AppCompatActivity)?.supportActionBar?.title = data!!.name
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Today"
    }

}