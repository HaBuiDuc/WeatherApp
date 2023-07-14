package android.buiducha.weatherapp.adapter

import android.annotation.SuppressLint
import android.buiducha.weatherapp.databinding.TodayBriefItemBinding
import android.buiducha.weatherapp.model.fivedayforecast.GeneralInformation
import android.buiducha.weatherapp.utils.getDayOfWeek
import android.buiducha.weatherapp.utils.getImage
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class TodayBriefAdapter(
    private val weatherList: List<GeneralInformation>
) : Adapter<TodayHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayHolder {
        val viewBinding = TodayBriefItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodayHolder(viewBinding, parent.context)
    }

    override fun getItemCount() = weatherList.size

    override fun onBindViewHolder(holder: TodayHolder, position: Int) {
        weatherList[position].let { weather ->
            holder.bind(weather)
        }
    }

}

class TodayHolder(
    private val viewBinding: TodayBriefItemBinding,
    private val context: Context
) : ViewHolder(viewBinding.root) {
    @SuppressLint("SimpleDateFormat")
    fun bind(weather: GeneralInformation) {
        viewBinding.apply {
            val time = weather.weatherDateTxt.substringAfterLast(" ").substring(0, 5)
            todayTimeTxt.text = time

            todayBriefStatusImg.setImageDrawable(ContextCompat.getDrawable(context, weather.getImage()))

            todayBriefTemperatureTxt.text = weather.main.temp.toString().substringBeforeLast('.')
        }
    }
}