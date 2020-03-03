package gdsvn.tringuyen.myapplication.data.provider.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task
import gdsvn.tringuyen.myapplication.data.entity.Coord
import gdsvn.tringuyen.myapplication.data.entity.WeatherDayEntity
import gdsvn.tringuyen.myapplication.data.provider.LocationPermissionNotGrantedException
import gdsvn.tringuyen.myapplication.data.provider.units.PreferenceProvider
import io.reactivex.Flowable
import timber.log.Timber
import kotlin.math.abs


const val USE_DEVICE_LOCATION = "USE_DEVICE_LOCATION"
const val CUSTOM_LOCATION = "CUSTOM_LOCATION"

class LocationProviderImpl(context: Context) : PreferenceProvider(context) {

    private val appContext = context.applicationContext

    fun isUsingDeviceLocation(): Boolean {
        return preferences.getBoolean(USE_DEVICE_LOCATION, true)
    }

    fun getCustomLocationName(): String? {
        return preferences.getString(CUSTOM_LOCATION, null)
    }
}

