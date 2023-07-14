package android.buiducha.weatherapp.adapter

import android.annotation.SuppressLint
import android.buiducha.weatherapp.databinding.WeeklyBriefItemBinding
import android.buiducha.weatherapp.model.fivedayforecast.GeneralInformation
import android.buiducha.weatherapp.utils.getDayOfWeek
import android.buiducha.weatherapp.utils.getImage
import android.buiducha.weatherapp.utils.temperatureFormat
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import java.text.SimpleDateFormat

class WeeklyBriefAdapter(
    private val weatherList: List<GeneralInformation>
) : Adapter<WeeklyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyHolder {
        val viewBinding = WeeklyBriefItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeeklyHolder(viewBinding, parent.context)
    }

    override fun getItemCount() = weatherList.size

    override fun onBindViewHolder(holder: WeeklyHolder, position: Int) {
        weatherList[position].let {weather ->
            holder.bind(weather)
        }
    }
}

class WeeklyHolder(
    private val viewBinding: WeeklyBriefItemBinding,
    private val context: Context
) : ViewHolder(viewBinding.root) {
    fun bind(weather: GeneralInformation) {
        viewBinding.apply {
            weeklyBriefStatusImg.setImageDrawable(
                ContextCompat.getDrawable(context, weather.getImage())
            )

            weeklyBriefTemperatureTxt.text = weather.temperatureFormat()

            daysOfWeekTxt.text = weather.getDayOfWeek()

            Log.d("This is a log", weather.weatherDate.toString())
        }
    }
}