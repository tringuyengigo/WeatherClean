package gdsvn.tringuyen.myapplication.presentation.di

import androidx.room.Room
import gdsvn.tringuyen.myapplication.data.api.RemoteApiWeather
import gdsvn.tringuyen.myapplication.data.database.WeatherDatabase
import gdsvn.tringuyen.myapplication.data.responsitory.WeatherCurrentCacheImpl
import gdsvn.tringuyen.myapplication.data.responsitory.WeatherForecastCacheImpl
import gdsvn.tringuyen.myapplication.data.responsitory.WeatherRemoteImpl
import gdsvn.tringuyen.myapplication.data.responsitory.WeatherRepositoryImpl
import gdsvn.tringuyen.myapplication.domain.respository.WeatherRepository
import gdsvn.tringuyen.myapplication.domain.usecase.GetCurrentWeatherCityUseCase
import gdsvn.tringuyen.myapplication.domain.usecase.GetCurrentWeatherCoordinateUseCase
import gdsvn.tringuyen.myapplication.domain.usecase.GetWeatherForecastCityUseCase
import gdsvn.tringuyen.myapplication.domain.usecase.GetWeatherForecastCoordinateUseCase
import gdsvn.tringuyen.myapplication.presentation.common.AsyncFlowableTransformer
import gdsvn.tringuyen.myapplication.presentation.weather.viewmodel.CurrentWeatherViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.module
import org.koin.android.viewmodel.ext.koin.viewModel
import retrofit2.Retrofit

val mRepositoryModules = module {
    single(name = "remote") { WeatherRemoteImpl(api = get(API)) }
    single(name = "local_weather_current") {
        WeatherCurrentCacheImpl(database = get(DATABASE))
    }
    single(name = "local_weather_forecast") {
        WeatherForecastCacheImpl(database = get(DATABASE))
    }
    single { WeatherRepositoryImpl(remote = get("remote"), cacheWeatherCurrent = get("local_weather_current"), cacheWeatherForecast = get("local_weather_forecast")) as WeatherRepository }
}

val mUseCaseModules = module {
    factory(name = "GetCurrentWeatherCityUseCase") { GetCurrentWeatherCityUseCase(transformer = AsyncFlowableTransformer(), repositories = get()) }
    factory(name = "GetCurrentWeatherCoordinateUseCase") { GetCurrentWeatherCoordinateUseCase(transformer = AsyncFlowableTransformer(), repositories = get()) }
    factory(name = "GetWeatherForecastCityUseCase") { GetWeatherForecastCityUseCase(transformer = AsyncFlowableTransformer(), repositories = get()) }
    factory(name = "GetWeatherForecastCoordinateUseCase") { GetWeatherForecastCoordinateUseCase(transformer = AsyncFlowableTransformer(), repositories = get()) }
}

val mNetworkModules = module {
    single(name = RETROFIT_INSTANCE) { createNetworkClient(BASE_URL) }
    single(name = API) { (get(RETROFIT_INSTANCE) as Retrofit).create(RemoteApiWeather::class.java) }
}

val mLocalModules = module {
    single(name = DATABASE) { Room.databaseBuilder(androidApplication(), WeatherDatabase::class.java, "weather").build() }
}

val mViewModels = module {
    viewModel {
        CurrentWeatherViewModel(getCurrentWeatherCityUseCase = get("GetCurrentWeatherCityUseCase"))
    }
}

private const val BASE_URL = "https://api.openweathermap.org"
private const val RETROFIT_INSTANCE = "Retrofit"
private const val API = "Api"
private const val GET_WEATHER_USECASE = "getWeatherUseCase"
private const val REMOTE = "remote response"
private const val DATABASE = "database"