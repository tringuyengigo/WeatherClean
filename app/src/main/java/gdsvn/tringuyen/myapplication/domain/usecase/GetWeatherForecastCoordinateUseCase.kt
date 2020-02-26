package gdsvn.tringuyen.myapplication.domain.usecase

import android.util.Log
import gdsvn.tringuyen.myapplication.data.entity.WeatherDayEntity
import gdsvn.tringuyen.myapplication.domain.common.BaseFlowableUseCase
import gdsvn.tringuyen.myapplication.domain.common.FlowableRxTransformer
import gdsvn.tringuyen.myapplication.domain.entity.WeatherDay
import gdsvn.tringuyen.myapplication.domain.respository.WeatherRepository

import io.reactivex.Flowable


class GetWeatherForecastCoordinateUseCase(private val transformer: FlowableRxTransformer<List<WeatherDayEntity>>,
                                          private val repositories: WeatherRepository
) : BaseFlowableUseCase<List<WeatherDayEntity>>(transformer){


    companion object {
        private val TAG = "GetWeatherForecastCoordinateUseCase"
    }
    override fun createFlowable(data: Map<String, Any>?): Flowable<List<WeatherDayEntity>> {
        return repositories.getWeatherForecastCoordinates(data?.get("lon") as String, data?.get("lat") as String)
    }

    fun getWeatherForecastByCoordinate(lon: String, lat: String) : Flowable<List<WeatherDayEntity>> {
        val data = HashMap<String, String>()
        data["lon"] = lon
        data["lat"] = lat
        return single(data)
    }

}