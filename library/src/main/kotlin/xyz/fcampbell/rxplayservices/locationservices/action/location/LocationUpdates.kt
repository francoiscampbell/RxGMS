package xyz.fcampbell.rxplayservices.locationservices.action.location

import android.location.Location
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe

internal class LocationUpdates(
        private val apiClient: GoogleApiClient,
        private val locationRequest: LocationRequest
) : ObservableOnSubscribe<Location> {

    override fun subscribe(emitter: ObservableEmitter<Location>) {
        val listener = LocationListener { location -> emitter.onNext(location) }

        emitter.setCancellable {
            if (apiClient.isConnected) {
                LocationServices.FusedLocationApi.removeLocationUpdates(apiClient, listener)
            }
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, listener)
    }
}
