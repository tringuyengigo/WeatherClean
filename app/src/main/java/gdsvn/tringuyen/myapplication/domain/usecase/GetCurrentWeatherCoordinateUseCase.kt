package gdsvn.tringuyen.myapplication.domain.usecase

import android.util.Log
import gdsvn.tringuyen.myapplication.data.entity.WeatherDayEntity
import gdsvn.tringuyen.myapplication.domain.common.BaseFlowableUseCase
import gdsvn.tringuyen.myapplication.domain.common.FlowableRxTransformer
import gdsvn.tringuyen.myapplication.domain.entity.WeatherDay
import gdsvn.tringuyen.myapplication.domain.respository.WeatherRepository

import io.reactivex.Flowable


class GetCurrentWeatherCoordinateUseCase(private val transformer: FlowableRxTransformer<WeatherDayEntity>,
                                         private val repositories: WeatherRepository
) : BaseFlowableUseCase<WeatherDayEntity>(transformer){


    companion object {
        private val TAG = "GetCurrentWeatherCoordinateUseCase"
    }
    override fun createFlowable(data: Map<String, Any>?): Flowable<WeatherDayEntity> {
        return repositories.getCurrentWeatherCoordinates(data?.get("lon") as String, data?.get("lat") as String)
    }

    fun getWeatherByCoordinate(lon: String, lat: String) : Flowable<WeatherDayEntity> {
        val data = HashMap<String, String>()
        data["lon"] = lon
        data["lat"] = lat
        return single(data)
    }

}