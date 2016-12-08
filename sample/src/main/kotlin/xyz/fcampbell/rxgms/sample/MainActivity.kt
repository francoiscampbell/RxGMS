package xyz.fcampbell.rxgms.sample

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.functions.Func1
import rx.schedulers.Schedulers
import xyz.fcampbell.rxgms.RxGms
import xyz.fcampbell.rxgms.sample.utils.*

class MainActivity : BaseActivity() {
    private lateinit var locationProvider: RxGms

    private lateinit var lastKnownLocationView: TextView
    private lateinit var updatableLocationView: TextView
    private lateinit var addressLocationView: TextView
    private lateinit var currentActivityView: TextView

    private lateinit var lastKnownLocationObservable: Observable<Location>
    private lateinit var locationUpdatesObservable: Observable<Location>
    private lateinit var activityObservable: Observable<ActivityRecognitionResult>

    private lateinit var lastKnownLocationSubscription: Subscription
    private lateinit var updatableLocationSubscription: Subscription
    private lateinit var addressSubscription: Subscription
    private lateinit var activitySubscription: Subscription
    private lateinit var addressObservable: Observable<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lastKnownLocationView = findViewById(R.id.last_known_location_view) as TextView
        updatableLocationView = findViewById(R.id.updated_location_view) as TextView
        addressLocationView = findViewById(R.id.address_for_location_view) as TextView
        currentActivityView = findViewById(R.id.activity_recent_view) as TextView

        locationProvider = RxGms(applicationContext)
        lastKnownLocationObservable = locationProvider.getLastKnownLocation()

        val locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(5)
                .setInterval(100)
        locationUpdatesObservable = locationProvider
                .checkLocationSettings(
                        LocationSettingsRequest.Builder()
                                .addLocationRequest(locationRequest)
                                .setAlwaysShow(true)  //Refrence: http://stackoverflow.com/questions/29824408/google-play-services-locationservices-api-new-option-never
                                .build()
                )
                .doOnNext { locationSettingsResult ->
                    val status = locationSettingsResult.status
                    if (status.statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        try {
                            status.startResolutionForResult(this@MainActivity, REQUEST_CHECK_SETTINGS)
                        } catch (th: IntentSender.SendIntentException) {
                            Log.e("MainActivity", "Error opening settings activity.", th)
                        }

                    }
                }
                .flatMap { locationProvider.getUpdatedLocation(locationRequest) }

        addressObservable = locationProvider.getUpdatedLocation(locationRequest)
                .flatMap { location -> locationProvider.getReverseGeocodeObservable(location.latitude, location.longitude, 1) }
                .map { addresses -> if (addresses != null && !addresses.isEmpty()) addresses[0] else null }
                .map(AddressToStringFunc())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        activityObservable = locationProvider.getDetectedActivity(50)
    }

    override fun onLocationPermissionGranted() {
        lastKnownLocationSubscription = lastKnownLocationObservable
                .map(LocationToStringFunc())
                .subscribe(DisplayTextOnViewAction(lastKnownLocationView), ErrorHandler())

        updatableLocationSubscription = locationUpdatesObservable
                .map(LocationToStringFunc())
                .map(object : Func1<String, String> {
                    internal var count = 0

                    override fun call(s: String): String {
                        return s + " " + count++
                    }
                })
                .subscribe(DisplayTextOnViewAction(updatableLocationView), ErrorHandler())


        addressSubscription = addressObservable
                .subscribe(DisplayTextOnViewAction(addressLocationView), ErrorHandler())

        activitySubscription = activityObservable
                .map(ToMostProbableActivity())
                .map(DetectedActivityToString())
                .subscribe(DisplayTextOnViewAction(currentActivityView), ErrorHandler())
    }

    override fun onStop() {
        super.onStop()
        UnsubscribeIfPresent.unsubscribe(updatableLocationSubscription)
        UnsubscribeIfPresent.unsubscribe(addressSubscription)
        UnsubscribeIfPresent.unsubscribe(lastKnownLocationSubscription)
        UnsubscribeIfPresent.unsubscribe(activitySubscription)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add("Geofencing").setOnMenuItemClickListener {
            startActivity(Intent(this@MainActivity, GeofenceActivity::class.java))
            true
        }
        menu.add("Places").setOnMenuItemClickListener {
            if (TextUtils.isEmpty(getString(R.string.API_KEY))) {
                Toast.makeText(this@MainActivity, "First you need to configure your API Key - see README.md", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this@MainActivity, PlacesActivity::class.java))
            }
            true
        }
        menu.add("Mock Locations").setOnMenuItemClickListener {
            startActivity(Intent(this@MainActivity, MockLocationsActivity::class.java))
            true
        }
        return true
    }

    private inner class ErrorHandler : Action1<Throwable> {
        override fun call(throwable: Throwable) {
            Toast.makeText(this@MainActivity, "Error occurred.", Toast.LENGTH_SHORT).show()
            Log.d("MainActivity", "Error occurred", throwable)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
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
        }
    }

    companion object {
        private const val REQUEST_CHECK_SETTINGS = 0
        private const val TAG = "MainActivity"
    }

}
