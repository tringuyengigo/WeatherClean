package gdsvn.tringuyen.myapplication.presentation.weather.ui.main

import android.app.Application
import android.preference.PreferenceManager
import gdsvn.tringuyen.myapplication.BuildConfig
import gdsvn.tringuyen.myapplication.R
import gdsvn.tringuyen.myapplication.presentation.di.*
import org.koin.android.ext.android.startKoin
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        loadKoin()
    }

    private fun loadKoin() {
        startKoin(this,
                listOf( mNetworkModules,
                        mViewModels,
                        mProviders,
                        mRepositoryModules,
                        mUseCaseModules,
                        mSharePreference,
                        mLocalModules)

        )
        PreferenceManager.setDefaultValues(this,
            R.xml.preference, false)
    }
}