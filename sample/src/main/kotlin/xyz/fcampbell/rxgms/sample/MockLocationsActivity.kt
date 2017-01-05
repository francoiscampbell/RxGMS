package xyz.fcampbell.rxgms.sample

import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.*
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationRequest
import rx.Observable
import rx.Subscription
import rx.functions.Action1
import rx.functions.Func1
import rx.functions.Func2
import rx.subjects.PublishSubject
import xyz.fcampbell.rxgms.RxGms
import xyz.fcampbell.rxgms.sample.utils.DisplayTextOnViewAction
import xyz.fcampbell.rxgms.sample.utils.LocationToStringFunc
import xyz.fcampbell.rxgms.sample.utils.UnsubscribeIfPresent.unsubscribe
import java.util.*

class MockLocationsActivity : BaseActivity() {

    private lateinit var latitudeInput: EditText
    private lateinit var longitudeInput: EditText
    private lateinit var mockLocationView: TextView
    private lateinit var updatedLocationView: TextView
    private lateinit var mockModeToggleButton: ToggleButton
    private lateinit var setLocationButton: Button

    private lateinit var rxGms: RxGms
    private lateinit var mockLocationObservable: Observable<Location>
    private lateinit var mockLocationSubscription: Subscription
    private lateinit var updatedLocationSubscription: Subscription

    private lateinit var mockLocationSubject: PublishSubject<Location>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mocklocations)

        rxGms = RxGms(this)
        mockLocationSubject = PublishSubject.create<Location>()

        mockLocationObservable = mockLocationSubject.asObservable()

        initViews()
    }

    private fun initViews() {
        latitudeInput = findViewById(R.id.latitude_input) as EditText
        longitudeInput = findViewById(R.id.longitude_input) as EditText
        mockLocationView = findViewById(R.id.mock_location_view) as TextView
        updatedLocationView = findViewById(R.id.updated_location_view) as TextView
        mockModeToggleButton = findViewById(R.id.toggle_button) as ToggleButton
        setLocationButton = findViewById(R.id.set_location_button) as Button

        mockModeToggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            setMockMode(isChecked)
            setLocationButton.isEnabled = isChecked
        }
        setLocationButton.setOnClickListener { addMockLocation() }
    }

    override fun onLocationPermissionGranted() {
        mockModeToggleButton.isChecked = true

        val locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(2000)
        updatedLocationSubscription = rxGms.locationApi
                .requestLocationUpdates(locationRequest)
                .map(LocationToStringFunc)
                .map(object : Func1<String, String> {
                    internal var count = 0

                    override fun call(s: String): String {
                        return s + " " + count++
                    }
                })
                .subscribe(DisplayTextOnViewAction(updatedLocationView))
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
            mockLocationSubscription = Observable.zip(rxGms.locationApi.mockLocation(mockLocationObservable),
                    mockLocationObservable, object : Func2<Status, Location, String> {
                internal var count = 0

                override fun call(result: Status, location: Location): String {
                    return LocationToStringFunc.call(location) + " " + count++
                }
            })
                    .subscribe(DisplayTextOnViewAction(mockLocationView), ErrorHandler())
        } else {
            mockLocationSubscription.unsubscribe()
        }
    }

    private fun createMockLocation(): Location {
        val longitudeString = longitudeInput.text.toString()
        val latitudeString = latitudeInput.text.toString()

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
        unsubscribe(mockLocationSubscription)
        unsubscribe(updatedLocationSubscription)
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

