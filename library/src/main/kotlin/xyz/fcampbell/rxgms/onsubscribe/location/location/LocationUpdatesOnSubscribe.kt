package xyz.fcampbell.rxgms.onsubscribe.location.location

import android.content.Context
import android.location.Location
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import rx.Observable
import rx.Observer
import xyz.fcampbell.rxgms.onsubscribe.location.BaseLocationOnSubscribe

class LocationUpdatesOnSubscribe private constructor(
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

    companion object {
        @JvmStatic
        fun createObservable(ctx: Context, locationRequest: LocationRequest): Observable<Location> {
            return Observable.create(LocationUpdatesOnSubscribe(ctx, locationRequest))
        }
    }

}
