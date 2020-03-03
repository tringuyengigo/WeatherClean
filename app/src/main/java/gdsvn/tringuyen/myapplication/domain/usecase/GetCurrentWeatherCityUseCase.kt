package gdsvn.tringuyen.myapplication.domain.usecase

import android.util.Log
import gdsvn.tringuyen.myapplication.data.entity.WeatherDayEntity
import gdsvn.tringuyen.myapplication.domain.common.BaseFlowableUseCase
import gdsvn.tringuyen.myapplication.domain.common.FlowableRxTransformer
import gdsvn.tringuyen.myapplication.domain.respository.WeatherRepository

import io.reactivex.Flowable


class GetCurrentWeatherCityUseCase(private val transformer: FlowableRxTransformer<WeatherDayEntity?>,
                                   private val repositories: WeatherRepository
) : BaseFlowableUseCase<WeatherDayEntity?>(transformer){


    companion object {
        private val TAG = "GetCurrentWeatherUseCase"
    }
    override fun createFlowable(data: Map<String, Any>?): Flowable<WeatherDayEntity?> {
        return repositories.getCurrentWeatherCity(data?.get("city") as String)
    }

    fun getWeatherByCity(city: String) : Flowable<WeatherDayEntity?> {
        val data = HashMap<String, String>()
        data["city"] = city
        return single(data)
    }

}