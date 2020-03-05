package gdsvn.tringuyen.myapplication.presentation.weather.ui.greeting

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import gdsvn.tringuyen.myapplication.R
import gdsvn.tringuyen.myapplication.data.provider.units.SharedPreference
import gdsvn.tringuyen.myapplication.data.provider.units.UnitProviderImpl
import gdsvn.tringuyen.myapplication.presentation.di.mSharePreference
import gdsvn.tringuyen.myapplication.presentation.weather.viewmodel.current.CurrentWeatherViewModel
import kotlinx.android.synthetic.main.fragment_greeting.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val MENU_CURRENT: Int = 0

private const val MENU_WEEK: Int = 1

private const val MENU_SETTING: Int = 2

private const val DOUBLE_TAP: Int = 3

/**
 * A simple [Fragment] subclass.
 * Use the [GreetingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GreetingFragment : Fragment() {

    private var param1: String? = null

    private var param2: String? = null

    private val sharedPreferences: SharedPreference by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.imageView_condition_icon.setImageResource(R.drawable.ic_launcher_web)

        if(sharedPreferences.getValueBoolien("IS_SHOW", true)) {
            addMaterialTapTarget( activity = this.activity,
                stringTitle = this!!.getString(R.string.current_weather),
                stringNotify = this!!.getString(R.string.notify_current_weather),
                indexDefine = MENU_CURRENT)
        } else {
            Handler().postDelayed({
                NavHostFragment.findNavController(this).navigate(R.id.action_greetingFragment_to_currentWeatherFragment)
            }, 500)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_greeting, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GreetingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GreetingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
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
                                sharedPreferences.save("IS_SHOW", false)
                                addMaterialTapTarget( activity = this.activity,
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
                                addMaterialTapTarget( activity = this.activity,
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
                                NavHostFragment.findNavController(this).navigate(R.id.action_greetingFragment_to_currentWeatherFragment)
                            }
                        })
                        .create()?.show()
                }
                else -> println("Number correct")
            }

        }
    }
}