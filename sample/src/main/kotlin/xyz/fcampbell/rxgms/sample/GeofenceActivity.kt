package xyz.fcampbell.rxgms.sample

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import rx.Subscription
import xyz.fcampbell.rxgms.RxGms
import xyz.fcampbell.rxgms.sample.utils.DisplayTextOnViewAction
import xyz.fcampbell.rxgms.sample.utils.LocationToStringFunc
import xyz.fcampbell.rxgms.sample.utils.UnsubscribeIfPresent

class GeofenceActivity : BaseActivity() {

    private lateinit var rxGms: RxGms
    private lateinit var latitudeInput: EditText
    private lateinit var longitudeInput: EditText
    private lateinit var radiusInput: EditText
    private lateinit var lastKnownLocationView: TextView
    private lateinit var lastKnownLocationSubscription: Subscription

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rxGms = RxGms(this)
        setContentView(R.layout.activity_geofence)
        initViews()
    }

    private fun initViews() {
        lastKnownLocationView = findViewById(R.id.last_known_location_view) as TextView
        latitudeInput = findViewById(R.id.latitude_input) as EditText
        longitudeInput = findViewById(R.id.longitude_input) as EditText
        radiusInput = findViewById(R.id.radius_input) as EditText
        findViewById(R.id.add_button).setOnClickListener { addGeofence() }
        findViewById(R.id.clear_button).setOnClickListener { clearGeofence() }
    }

    override fun onLocationPermissionGranted() {
        lastKnownLocationSubscription = rxGms.locationApi
                .getLastLocation()
                .map(LocationToStringFunc)
                .subscribe(DisplayTextOnViewAction(lastKnownLocationView))
    }

    override fun onStop() {
        super.onStop()
        UnsubscribeIfPresent.unsubscribe(lastKnownLocationSubscription)
    }

    private fun clearGeofence() {
        rxGms.locationApi.removeGeofences(createNotificationBroadcastPendingIntent()).subscribe({ toast("Geofences removed") }) { throwable ->
            toast("Error removing geofences")
            Log.d(TAG, "Error removing geofences", throwable)
        }
    }

    private fun toast(text: String) {
        Toast.makeText(this@GeofenceActivity, text, Toast.LENGTH_SHORT).show()
    }

    private fun createNotificationBroadcastPendingIntent(): PendingIntent {
        return PendingIntent.getBroadcast(this, 0, Intent(this, GeofenceBroadcastReceiver::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun addGeofence() {
        val geofencingRequest = createGeofencingRequest() ?: return

        val pendingIntent = createNotificationBroadcastPendingIntent()
        rxGms.locationApi
                .removeGeofences(pendingIntent)
                .flatMap { rxGms.locationApi.addGeofences(pendingIntent, geofencingRequest) }
                .subscribe({ addGeofenceResult -> toast("Geofence added, success: " + addGeofenceResult.isSuccess) }) { throwable ->
                    toast("Error adding geofence.")
                    Log.d(TAG, "Error adding geofence.", throwable)
                }
    }

    private fun createGeofencingRequest(): GeofencingRequest? {
        try {
            val longitude = java.lang.Double.parseDouble(longitudeInput.text.toString())
            val latitude = java.lang.Double.parseDouble(latitudeInput.text.toString())
            val radius = java.lang.Float.parseFloat(radiusInput.text.toString())
            val geofence = Geofence.Builder()
                    .setRequestId("GEOFENCE")
                    .setCircularRegion(latitude, longitude, radius)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build()
            return GeofencingRequest.Builder().addGeofence(geofence).build()
        } catch (ex: NumberFormatException) {
            toast("Error parsing input.")
            return null
        }

    }

    companion object {
        private const val TAG = "GeofenceActivity"
    }
}
