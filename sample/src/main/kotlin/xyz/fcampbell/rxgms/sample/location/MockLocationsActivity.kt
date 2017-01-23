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
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.processors.PublishProcessor
import kotlinx.android.synthetic.main.activity_mocklocations.*
import xyz.fcampbell.rxgms.location.RxFusedLocationApi
import xyz.fcampbell.rxgms.sample.PermittedActivity
import xyz.fcampbell.rxgms.sample.R
import xyz.fcampbell.rxgms.sample.utils.DisplayTextOnViewAction
import xyz.fcampbell.rxgms.sample.utils.LocationToStringFunc
import java.util.*

class MockLocationsActivity : PermittedActivity() {
    override val permissionsToRequest = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

    private val fusedLocationApi = RxFusedLocationApi(this)

    private lateinit var mockLocationObservable: Observable<Location>
    private var mockLocationDisposable: Disposable? = null
    private var updatedLocationDispoable: Disposable? = null

    private lateinit var mockLocationProcessor: PublishProcessor<Location>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mocklocations)

        mockLocationProcessor = PublishProcessor.create<Location>()
        mockLocationObservable = mockLocationProcessor.toObservable()

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
        updatedLocationDispoable = fusedLocationApi
                .requestLocationUpdates(locationRequest)
                .map(LocationToStringFunc)
                .map(object : Function<String, String> {
                    internal var count = 0

                    override fun apply(s: String): String {
                        return s + " " + count++
                    }
                })
                .subscribe(DisplayTextOnViewAction(updated_location_view))
    }

    private fun addMockLocation() {
        try {
            mockLocationProcessor.onNext(createMockLocation())
        } catch (e: Throwable) {
            Toast.makeText(this@MockLocationsActivity, "Error parsing input.", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setMockMode(toggle: Boolean) {
        if (toggle) {
            mockLocationDisposable = Observable.zip(fusedLocationApi.mockLocation(mockLocationObservable),
                    mockLocationObservable, object : BiFunction<Status, Location, String> {
                internal var count = 0

                override fun apply(result: Status, location: Location): String {
                    return LocationToStringFunc.apply(location) + " " + count++
                }
            })
                    .subscribe(DisplayTextOnViewAction(mock_location_view), ErrorHandler())
        } else {
            mockLocationDisposable?.dispose()
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
        mockLocationDisposable?.dispose()
        updatedLocationDispoable?.dispose()
    }

    private inner class ErrorHandler : Consumer<Throwable> {
        override fun accept(throwable: Throwable) {
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

