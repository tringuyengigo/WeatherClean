package gdsvn.tringuyen.myapplication.presentation.weather.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gdsvn.tringuyen.myapplication.R
import gdsvn.tringuyen.myapplication.presentation.weather.viewmodel.CurrentWeatherViewModel
import gdsvn.tringuyen.myapplication.presentation.weather.viewmodel.FutureListWeatherViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class FutureListWeatherFragment : Fragment() {

    companion object {
        fun newInstance() =
            FutureListWeatherFragment()
    }

    private val futureListWeatherViewModel: FutureListWeatherViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_list_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        futureListWeatherViewModel.fetchWeatherCity("")
    }

}