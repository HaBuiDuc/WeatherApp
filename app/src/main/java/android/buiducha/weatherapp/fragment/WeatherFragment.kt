package android.buiducha.weatherapp.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.buiducha.weatherapp.adapter.TodayBriefAdapter
import android.buiducha.weatherapp.adapter.WeeklyBriefAdapter
import android.buiducha.weatherapp.databinding.WeatherFragmentBinding
import android.buiducha.weatherapp.model.currentweather.CurrentWeather
import android.buiducha.weatherapp.model.fivedayforecast.FiveDayWeather
import android.buiducha.weatherapp.utils.getImage
import android.buiducha.weatherapp.utils.temperatureFormat
import android.buiducha.weatherapp.viewmodel.WeatherViewModel
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.*

class WeatherFragment : Fragment() {
    private lateinit var viewBinding: WeatherFragmentBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var todayBriefAdapter: TodayBriefAdapter
    private lateinit var weeklyBriefAdapter: WeeklyBriefAdapter
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isLocationPermissionGranted(requireActivity())
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewBinding = WeatherFragmentBinding.inflate(LayoutInflater.from(context), container, false)
        return viewBinding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getWeather()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentWeather.collect {currentWeather ->
                viewBinding.apply {
                    if (currentWeather!= null) {
                        titleContainerSetup(currentWeather)
                        briefContainerSetup(currentWeather)
                        weatherStatusImg.setImageDrawable(ContextCompat.getDrawable(requireContext(), currentWeather.getImage(viewModel.currentDateTime.toInt())))
                    }
                    if (currentWeather != null) {
                        Log.d(TAG, currentWeather.name)
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fiveDayForecast.collect {fiveDayWeather ->
                recyclerViewSetup(fiveDayWeather)
            }
        }

        swipeRefreshSetup()
    }

    private fun recyclerViewSetup(fiveDayWeather: FiveDayWeather?) {
        viewBinding.apply {
            if (fiveDayWeather != null) {
                todayBriefAdapter = TodayBriefAdapter(fiveDayWeather.weatherByHour.subList(0, 8))
                todayRv.adapter = todayBriefAdapter
                todayRv.layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )

                val listSize = fiveDayWeather.weatherByHour.size
                weeklyBriefAdapter = WeeklyBriefAdapter(fiveDayWeather.weatherByHour.slice(3 .. listSize step 8))
                weeklyRv.adapter = weeklyBriefAdapter
                weeklyRv.layoutManager = LinearLayoutManager(requireContext())
            }
        }

    }

    @SuppressLint("MissingPermission")
    private fun getWeather() {
        if (isLocationPermissionGranted(requireActivity())) {
            if (isInternetConnection()) {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    viewModel.getCurrentWeather(it.latitude, it.longitude)
                    viewModel.getFiveDayForecast(it.latitude, it.longitude)
                    Log.d(TAG, "lat: ${it.latitude}, lon: ${it.longitude}")
//                    viewBinding.briefInformationContainer.isVisible = true
                }
            } else {
                Snackbar.make(requireView(), "No internet connection", Snackbar.LENGTH_INDEFINITE).show()
//                viewBinding.briefInformationContainer.isVisible = false
            }
        }
    }

    private fun briefContainerSetup(currentWeather: CurrentWeather) {
        viewBinding.apply {
            humidityRateTxt.text = currentWeather.keyInformation.humidity.toString()
            windSpeedTxt.text = currentWeather.wind.speed.toString().substringBeforeLast('.')
        }
    }

    @SuppressLint("SetTextI18n")
    private fun titleContainerSetup(currentWeather: CurrentWeather) {
        viewBinding.apply {
            temperatureTxt.text = currentWeather.temperatureFormat()
            temperatureUnitTxt.text = "\u00B0\u1d9c"

            cityNameTxt.text = currentWeather.name
            countryNameTxt.text = currentWeather.sys.country
        }
    }

    private fun swipeRefreshSetup() {
        viewBinding.apply {
            todayRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        weatherSwRf.isEnabled = false
                    } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        weatherSwRf.isEnabled = true
                    }
                }
            })
            weatherSwRf.setOnRefreshListener {
                getWeather()
                weatherSwRf.isRefreshing = false
            }
        }
    }

    private fun isLocationPermissionGranted(activity: Activity): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_CODE
            )
            false
        } else {
            true
        }
    }

    private fun isInternetConnection(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if(capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.d(TAG, "isInternetConnection: true")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.d(TAG, "isInternetConnection: true")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.d(TAG, "isInternetConnection: true")
                return true
            }
        }
        Log.d(TAG, "isInternetConnection: false")
        return false
    }

    companion object {
        const val TAG = "WeatherFragment"
        const val REQUEST_CODE = 1
    }
}