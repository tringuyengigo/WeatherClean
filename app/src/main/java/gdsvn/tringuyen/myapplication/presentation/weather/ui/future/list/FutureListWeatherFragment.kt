package gdsvn.tringuyen.myapplication.presentation.weather.ui.future.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import gdsvn.tringuyen.myapplication.R
import gdsvn.tringuyen.myapplication.data.entity.WeatherDayEntity
import gdsvn.tringuyen.myapplication.data.entity.WeatherForecastEntity
import gdsvn.tringuyen.myapplication.presentation.common.Status
import gdsvn.tringuyen.myapplication.presentation.weather.ui.future.list.adapter.ItemClickListener
import gdsvn.tringuyen.myapplication.presentation.weather.ui.future.list.adapter.WeatherListAdapter
import gdsvn.tringuyen.myapplication.presentation.weather.viewmodel.future.FutureListWeatherViewModel
import gdsvn.tringuyen.myapplication.presentation.weather.viewmodel.future.sharedata.SharedViewModel
import kotlinx.android.synthetic.main.current_weather_fragment.group_loading
import kotlinx.android.synthetic.main.future_list_weather_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class FutureListWeatherFragment : Fragment(), ItemClickListener {

    companion object {
        fun newInstance() = FutureListWeatherFragment()
    }

    private val futureListWeatherViewModel: FutureListWeatherViewModel by viewModel()
    private lateinit var shareDataVM: SharedViewModel
    private lateinit var mWeatherAdapter: WeatherListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_list_weather_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shareDataVM = ViewModelProviders.of(activity!!).get(SharedViewModel::class.java)
        mWeatherAdapter = WeatherListAdapter(futureListWeatherViewModel.isMetricUnit)
        mWeatherAdapter.setOnItemClickListener(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = mWeatherAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        futureListWeatherViewModel.fetchWeatherForecast()
    }

    override fun onStart() {
        super.onStart()
        futureListWeatherViewModel.getWeatherForecastLiveData().observe(this, Observer { data ->
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
                Timber.v("Data Weather at FutureListWeatherFragment ${Gson().toJson(it.city)}")
                updateWeatherForecastUI(it)
                for (itemWeatherDay in it.weather_forecast) {
                    Timber.v("Data item  ${Gson().toJson(itemWeatherDay)}")
                    updateListWeatherForecast(itemWeatherDay)
                }
            }
        })
    }



    private fun updateWeatherForecastUI(data: WeatherForecastEntity) {
        updateLocation(data.city.name)
        updateDateToNextWeek()
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
        shareDataVM.updateLocation(location)
    }

    private fun updateDateToNextWeek() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Next 5 days"
    }

    private fun updateListWeatherForecast(itemWeather: WeatherDayEntity) {
        mWeatherAdapter.updateList(itemWeather)
    }

    override fun onClick(
        view: View?,
        position: Int,
        isLongClick: Boolean,
        dataOfRow: WeatherDayEntity
    ) {
        showWeatherDetail(dataOfRow, view)
    }

    private fun showWeatherDetail(dataOfRow: WeatherDayEntity, view: View?) {
        shareDataVM.updateData(dataOfRow)
        shareDataVM.updateMetricUnit(futureListWeatherViewModel.isMetricUnit)

        NavHostFragment.findNavController(this).navigate(R.id.actionDetail)
    }


}