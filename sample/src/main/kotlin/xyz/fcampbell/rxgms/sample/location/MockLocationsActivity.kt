package xyz.fcampbell.rxgms.sample.location

import android.Manifest
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationRequest
import kotlinx.android.synthetic.main.activity_mocklocations.*
import rx.Observable
import rx.Subscription
import rx.functions.Action1
import rx.functions.Func1
import rx.functions.Func2
import rx.subjects.PublishSubject
import xyz.fcampbell.rxgms.location.RxLocation
import xyz.fcampbell.rxgms.sample.PermittedActivity
import xyz.fcampbell.rxgms.sample.R
import xyz.fcampbell.rxgms.sample.utils.DisplayTextOnViewAction
import xyz.fcampbell.rxgms.sample.utils.LocationToStringFunc
import java.util.*

class MockLocationsActivity : PermittedActivity() {
    override val permissionsToRequest = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

    private val fusedLocationApi = RxLocation.FusedLocationApi(this)

    private lateinit var mockLocationObservable: Observable<Location>
    private var mockLocationSubscription: Subscription? = null
    private var updatedLocationSubscription: Subscription? = null

    private lateinit var mockLocationSubject: PublishSubject<Location>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mocklocations)

        mockLocationSubject = PublishSubject.create<Location>()

        mockLocationObservable = mockLocationSubject.asObservable()

        initViews()
    }

    private fun initViews() {
        toggle_button.setOnCheckedChangeListener { buttonView, isChecked ->
            setMockMode(isChecked)
            set_location_button.isEnabled = isChecked
        }
        set_location_button.setOnClickListener { addMockLocation() }
    }


    override fun onPermissionsGranted(vararg permissions: String) {
        if (!permissions.contains(Manifest.permission.ACCESS_FINE_LOCATION)) return

        toggle_button.isChecked = true

        val locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(2000)
        updatedLocationSubscription = fusedLocationApi
                .requestLocationUpdates(locationRequest)
                .map(LocationToStringFunc)
                .map(object : Func1<String, String> {
                    internal var count = 0

                    override fun call(s: String): String {
                        return s + " " + count++
                    }
                })
                .subscribe(DisplayTextOnViewAction(updated_location_view))
    }

    private fun addMockLocation() {
        try {
            mockLocationSubject.onNext(createMockLocation())
        } catch (e: Throwable) {
            Toast.makeText(this@MockLocationsActivity, "Error parsing input.", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setMockMode(toggle: Boolean) {
        if (toggle) {
            mockLocationSubscription = Observable.zip(fusedLocationApi.mockLocation(mockLocationObservable),
                    mockLocationObservable, object : Func2<Status, Location, String> {
                internal var count = 0

                override fun call(result: Status, location: Location): String {
                    return LocationToStringFunc.call(location) + " " + count++
                }
            })
                    .subscribe(DisplayTextOnViewAction(mock_location_view), ErrorHandler())
        } else {
            mockLocationSubscription?.unsubscribe()
        }
    }

    private fun createMockLocation(): Location {
        val longitudeString = longitude_input.text.toString()
        val latitudeString = latitude_input.text.toString()

        if (!longitudeString.isEmpty() && !latitudeString.isEmpty()) {
            val longitude = Location.convert(longitudeString)
            val latitude = Location.convert(latitudeString)

            val mockLocation = Location("flp")
            mockLocation.latitude = latitude
            mockLocation.longitude = longitude
            mockLocation.accuracy = 1.0f
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mockLocation.elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
            }
            mockLocation.time = Date().time
            return mockLocation
        } else {
            throw IllegalArgumentException()
        }
    }

    override fun onStop() {
        super.onStop()
        mockLocationSubscription?.unsubscribe()
        updatedLocationSubscription?.unsubscribe()
    }

    private inner class ErrorHandler : Action1<Throwable> {
        override fun call(throwable: Throwable) {
            if (throwable is SecurityException) {
                Toast.makeText(this@MockLocationsActivity, "You need to enable mock locations in Developer Options.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MockLocationsActivity, "Error occurred.", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Error occurred", throwable)
            }
        }
    }

    companion object {
        private val TAG = "MockLocationsActivity"
    }

}

