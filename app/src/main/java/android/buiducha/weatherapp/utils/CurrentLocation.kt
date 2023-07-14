package android.buiducha.weatherapp.utils

import android.Manifest
import android.app.Activity
import android.buiducha.weatherapp.fragment.WeatherFragment
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices

object CurrentLocation {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: Location? = null

//    fun isLocationPermissionGranted(activity: Activity): Boolean {
//        return if (ActivityCompat.checkSelfPermission(
//                activity,
//                android.Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                activity,
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                activity as Activity,
//                arrayOf(
//                    android.Manifest.permission.ACCESS_FINE_LOCATION,
//                    android.Manifest.permission.ACCESS_COARSE_LOCATION
//                ),
//                WeatherFragment.REQUEST_CODE
//            )
//            false
//        } else {
//            true
//        }
//    }

    fun getCurrentLocation(context: Context) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                WeatherFragment.REQUEST_CODE
            )
       }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            currentLocation = it

            Log.d(TAG, "lat: ${currentLocation?.latitude}, lon: ${currentLocation?.longitude}")
        }
    }
    private const val TAG = "Location"
}