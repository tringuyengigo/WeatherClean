package gdsvn.tringuyen.myapplication

import android.app.Application
import gdsvn.tringuyen.myapplication.BuildConfig
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
//        startKoin(this,
//                listOf(mNetworkModules,
//                        mViewModels,
//                        mRepositoryModules,
//                        mUseCaseModules,
//                        mLocalModules)
//
//        )
    }
}