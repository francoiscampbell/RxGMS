package xyz.fcampbell.rxgms.location.onsubscribe.location

import android.content.Context
import android.location.Location
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import rx.Observer
import xyz.fcampbell.rxgms.location.onsubscribe.BaseLocationOnSubscribe

internal class LocationUpdatesOnSubscribe(
        ctx: Context,
        private val locationRequest: LocationRequest
) : BaseLocationOnSubscribe<Location>(ctx) {
    private var listener: LocationListener? = null

    override fun onGoogleApiClientReady(apiClient: GoogleApiClient, observer: Observer<in Location>) {
        listener = LocationListener { location -> observer.onNext(location) }
        LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, listener)
    }

    override fun onUnsubscribe(apiClient: GoogleApiClient) {
        if (apiClient.isConnected) {
            LocationServices.FusedLocationApi.removeLocationUpdates(apiClient, listener)
        }
    }
}
