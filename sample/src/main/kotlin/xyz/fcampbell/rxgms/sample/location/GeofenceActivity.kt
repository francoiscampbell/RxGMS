package xyz.fcampbell.rxgms.sample.location

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.GeofencingRequest
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_geofence.*
import xyz.fcampbell.rxgms.locationservices.RxFusedLocationApi
import xyz.fcampbell.rxgms.locationservices.RxGeofencingApi
import xyz.fcampbell.rxgms.sample.PermittedActivity
import xyz.fcampbell.rxgms.sample.R
import xyz.fcampbell.rxgms.sample.utils.DisplayTextOnViewAction
import xyz.fcampbell.rxgms.sample.utils.LocationToStringFunc
import java.lang.Double
import java.lang.Float

class GeofenceActivity : PermittedActivity() {
    override val permissionsToRequest = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

    private val fusedLocationApi = RxFusedLocationApi(this)
    private val geofencingApi = RxGeofencingApi(this)

    private var lastKnownLocationDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geofence)
        initViews()
    }

    private fun initViews() {
        add_button.setOnClickListener { addGeofence() }
        clear_button.setOnClickListener { clearGeofence() }
    }


    override fun onPermissionsGranted(vararg permissions: String) {
        if (!permissions.contains(Manifest.permission.ACCESS_FINE_LOCATION)) return

        lastKnownLocationDisposable = fusedLocationApi
                .getLastLocation()
                .map(LocationToStringFunc)
                .subscribe(DisplayTextOnViewAction(last_known_location_view))
    }

    override fun onStop() {
        super.onStop()
        lastKnownLocationDisposable?.dispose()
    }

    private fun clearGeofence() {
        geofencingApi.removeGeofences(createNotificationBroadcastPendingIntent())
                .subscribe({
                    toast("Geofences removed")
                }, { throwable ->
                    toast("Error removing geofences")
                    Log.d(TAG, "Error removing geofences", throwable)
                })
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
        geofencingApi.removeGeofences(pendingIntent)
                .flatMap { geofencingApi.addGeofences(pendingIntent, geofencingRequest) }
                .subscribe({
                    addGeofenceResult ->
                    toast("Geofence added, success: " + addGeofenceResult.isSuccess)
                }, { throwable ->
                    toast("Error adding geofence.")
                    Log.d(TAG, "Error adding geofence.", throwable)
                })
    }

    private fun createGeofencingRequest(): GeofencingRequest? {
        try {
            val longitude = java.lang.Double.parseDouble(longitude_input.text.toString())
            val latitude = java.lang.Double.parseDouble(latitude_input.text.toString())
            val radius = java.lang.Float.parseFloat(radius_input.text.toString())
            val geofence = com.google.android.gms.location.Geofence.Builder()
                    .setRequestId("GEOFENCE")
                    .setCircularRegion(latitude, longitude, radius)
                    .setExpirationDuration(com.google.android.gms.location.Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_ENTER or com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_EXIT)
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
