package xyz.fcampbell.rxgms.sample

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.widget.Toast
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import kotlinx.android.synthetic.main.activity_main.*
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.functions.Func1
import rx.schedulers.Schedulers
import xyz.fcampbell.rxgms.RxGms
import xyz.fcampbell.rxgms.common.exception.StatusException
import xyz.fcampbell.rxgms.sample.drive.DriveActivity
import xyz.fcampbell.rxgms.sample.location.GeofenceActivity
import xyz.fcampbell.rxgms.sample.location.MockLocationsActivity
import xyz.fcampbell.rxgms.sample.places.PlacesActivity
import xyz.fcampbell.rxgms.sample.utils.*

class MainActivity : PermittedActivity() {
    override val permissionsToRequest = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

    private val rxGms = RxGms(this)

    private var lastKnownLocationSubscription: Subscription? = null
    private var updatableLocationSubscription: Subscription? = null
    private var addressSubscription: Subscription? = null
    private var activitySubscription: Subscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onPermissionsGranted(vararg permissions: String) {
        if (!permissions.contains(Manifest.permission.ACCESS_FINE_LOCATION)) return

        getLocation()
    }

    private fun getLocation() {
        lastKnownLocationSubscription = rxGms.locationApi
                .getLastLocation()
                .map(LocationToStringFunc)
                .subscribe(DisplayTextOnViewAction(last_known_location_view), ErrorHandler())

        val locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(5)
                .setInterval(100)

        updatableLocationSubscription = rxGms.locationApi
                .checkLocationSettings(
                        LocationSettingsRequest.Builder()
                                .addLocationRequest(locationRequest)
                                .setAlwaysShow(true)  //Reference: http://stackoverflow.com/questions/29824408/google-play-services-locationservices-api-new-option-never
                                .build())
                .doOnSuccess { locationSettingsResult ->
                    val status = locationSettingsResult.status
                    if (status.statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        try {
                            status.startResolutionForResult(this@MainActivity, REQUEST_CHECK_SETTINGS)
                        } catch (th: IntentSender.SendIntentException) {
                            Log.e("MainActivity", "Error opening settings activity.", th)
                        }

                    }
                }
                .flatMapObservable { rxGms.locationApi.requestLocationUpdates(locationRequest) }
                .map(LocationToStringFunc)
                .map(object : Func1<String, String> {
                    private var count = 0

                    override fun call(s: String): String {
                        return s + " " + count++
                    }
                })
                .subscribe(DisplayTextOnViewAction(updated_location_view), ErrorHandler())


        addressSubscription = rxGms.locationApi
                .requestLocationUpdates(locationRequest)
                .flatMap { location -> rxGms.locationApi.reverseGeocode(location.latitude, location.longitude, 1) }
                .map { addresses -> if (addresses != null && !addresses.isEmpty()) addresses[0] else null }
                .map(AddressToStringFunc)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(DisplayTextOnViewAction(address_for_location_view), ErrorHandler())

        activitySubscription = rxGms.activityRecognitionApi
                .requestActivityUpdates(50)
                .map(ToMostProbableActivity)
                .map(DetectedActivityToString)
                .subscribe(DisplayTextOnViewAction(activity_recent_view), ErrorHandler())
    }

    override fun onStop() {
        super.onStop()

        updatableLocationSubscription?.unsubscribe()
        addressSubscription?.unsubscribe()
        lastKnownLocationSubscription?.unsubscribe()
        activitySubscription?.unsubscribe()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add("Geofencing").setOnMenuItemClickListener {
            startActivity(Intent(this@MainActivity, GeofenceActivity::class.java))
            return@setOnMenuItemClickListener true
        }
        menu.add("Places").setOnMenuItemClickListener {
            if (TextUtils.isEmpty(getString(R.string.API_KEY))) {
                Toast.makeText(this@MainActivity, "First you need to configure your API Key - see README.md", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this@MainActivity, PlacesActivity::class.java))
            }
            return@setOnMenuItemClickListener true
        }
        menu.add("Mock Locations").setOnMenuItemClickListener {
            startActivity(Intent(this@MainActivity, MockLocationsActivity::class.java))
            return@setOnMenuItemClickListener true
        }
        menu.add("Drive").setOnMenuItemClickListener {
            startActivity(Intent(this@MainActivity, DriveActivity::class.java))
            return@setOnMenuItemClickListener true
        }
        return true
    }

    private inner class ErrorHandler : Action1<Throwable> {
        override fun call(throwable: Throwable) {
            if (throwable is StatusException) {
                if (throwable.status.hasResolution()) {
                    throwable.status.startResolutionForResult(this@MainActivity, REQUEST_RESOLVE_ERROR)
                }
            } else {
                Toast.makeText(this@MainActivity, "Error occurred", Toast.LENGTH_SHORT).show()
                Log.d("MainActivity", "Error occurred: ${throwable.message}", throwable)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CHECK_SETTINGS ->
                //Reference: https://developers.google.com/android/reference/com/google/android/gms/location/SettingsApi
                when (resultCode) {
                    Activity.RESULT_OK ->
                        // All required changes were successfully made
                        Log.d(TAG, "User enabled location")
                    Activity.RESULT_CANCELED ->
                        // The user was asked to change settings, but chose not to
                        Log.d(TAG, "User Cancelled enabling location")
                    else -> {
                    }
                }
            REQUEST_RESOLVE_ERROR -> getLocation()
        }
    }

    companion object {
        private const val REQUEST_CHECK_SETTINGS = 0
        private const val REQUEST_RESOLVE_ERROR = 1
        private const val TAG = "MainActivity"
    }
}
