package gdsvn.tringuyen.myapplication.presentation.weather.ui.main

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import gdsvn.tringuyen.myapplication.R
import kotlinx.android.synthetic.main.activity_main.*
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt

private const val MY_PERMISSION_ACCESS_COARSE_LOCATION = 1

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private val MENU_CURRENT: Int = 0

    private val MENU_WEEK: Int = 1

    private val MENU_SETTING: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        addMaterialTapTarget( activity = this,
            stringTitle = this!!.getString(R.string.current_weather),
            stringNotify = this!!.getString(R.string.notify_current_weather),
            indexDefine = MENU_CURRENT)


        requestLocationPermission()

        if (hasLocationPermission()) {
//            bindLocationManager()
        }
        else
            requestLocationPermission()
    }

    override fun onSupportNavigateUp(): Boolean {
//        return NavigationUI.navigateUp(navController, null)
        return true
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            MY_PERMISSION_ACCESS_COARSE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == MY_PERMISSION_ACCESS_COARSE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            else
                Toast.makeText(this, "Please, set location manually in settings", Toast.LENGTH_LONG).show()
        }
    }

    private fun addMaterialTapTarget(activity: FragmentActivity?, stringTitle: String, stringNotify: String, indexDefine: Int) {
        activity?.let {
            when(indexDefine) {
                MENU_CURRENT -> {
                    MaterialTapTargetPrompt.Builder(it)
                        .setTarget(R.id.currentWeatherFragment)
                        .setIcon(R.drawable.ic_today)
                        .setBackgroundColour(resources.getColor(R.color.colorPrimary))
                        .setPrimaryText(stringTitle)
                        .setSecondaryText(stringNotify)
                        .setCaptureTouchEventOnFocal(true)
                        .setPromptStateChangeListener(MaterialTapTargetPrompt.PromptStateChangeListener { _: MaterialTapTargetPrompt?, state: Int ->
                            if (state == MaterialTapTargetPrompt.STATE_DISMISSING || state == MaterialTapTargetPrompt.STATE_FINISHED) {
                                addMaterialTapTarget( activity = this,
                                    stringTitle = this!!.getString(R.string.forcast_weather),
                                    stringNotify = this!!.getString(R.string.notify_forcast_weather),
                                    indexDefine = MENU_WEEK)
                            }
                        })
                        .create()?.show()
                }
                MENU_WEEK -> {
                    MaterialTapTargetPrompt.Builder(it)
                        .setTarget(R.id.futureListWeatherFragment)
                        .setIcon(R.drawable.ic_calendar_week)
                        .setBackgroundColour(resources.getColor(R.color.colorPrimary))
                        .setPrimaryText(stringTitle)
                        .setSecondaryText(stringNotify)
                        .setCaptureTouchEventOnFocal(true)
                        .setPromptStateChangeListener(MaterialTapTargetPrompt.PromptStateChangeListener { _: MaterialTapTargetPrompt?, state: Int ->
                            if (state == MaterialTapTargetPrompt.STATE_DISMISSING || state == MaterialTapTargetPrompt.STATE_FINISHED) {
                                addMaterialTapTarget( activity = this,
                                    stringTitle = this!!.getString(R.string.setting),
                                    stringNotify = this!!.getString(R.string.notify_setting),
                                    indexDefine = MENU_SETTING)

                            }
                        })
                        .create()?.show()
                }
                MENU_SETTING -> {
                    MaterialTapTargetPrompt.Builder(it)
                        .setTarget(R.id.settingsFragment)
                        .setIcon(R.drawable.ic_settings)
                        .setBackgroundColour(resources.getColor(R.color.colorPrimary))
                        .setPrimaryText(stringTitle)
                        .setSecondaryText(stringNotify)
                        .setCaptureTouchEventOnFocal(true)
                        .setPromptStateChangeListener(MaterialTapTargetPrompt.PromptStateChangeListener { _: MaterialTapTargetPrompt?, state: Int ->
                            if (state == MaterialTapTargetPrompt.STATE_DISMISSING || state == MaterialTapTargetPrompt.STATE_FINISHED) {
                                navController = Navigation.findNavController(this, R.id.nav_host_fragment)
                                bottom_nav.setupWithNavController(navController)
                                NavigationUI.setupActionBarWithNavController(this, navController)

                                NavigationUI.navigateUp(navController, null)
                            }
                        })
                        .create()?.show()
                }
                else -> println("Number correct")
            }

        }
    }



}
