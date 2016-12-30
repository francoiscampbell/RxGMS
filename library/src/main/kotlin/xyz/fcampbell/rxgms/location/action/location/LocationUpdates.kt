package xyz.fcampbell.rxgms.location.action.location

import android.location.Location
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import rx.AsyncEmitter
import xyz.fcampbell.rxgms.common.action.FromEmitter

internal class LocationUpdates(
        private val apiClient: GoogleApiClient,
        private val locationRequest: LocationRequest
) : FromEmitter<Location>() {
    private var listener: LocationListener? = null

    override fun call(emitter: AsyncEmitter<Location>) {
        super.call(emitter)

        listener = LocationListener { location -> emitter.onNext(location) }
        LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, listener)
    }

    override fun onUnsubscribe() {
        if (apiClient.isConnected) {
            LocationServices.FusedLocationApi.removeLocationUpdates(apiClient, listener)
        }
    }
}
