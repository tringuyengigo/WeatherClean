package gdsvn.tringuyen.myapplication.presentation.weather.ui.future.list.adapter

import android.view.View
import gdsvn.tringuyen.myapplication.data.entity.WeatherDayEntity

interface ItemClickListener {
    fun onClick(view: View?, position: Int, isLongClick: Boolean, dataOfRow: WeatherDayEntity)
}