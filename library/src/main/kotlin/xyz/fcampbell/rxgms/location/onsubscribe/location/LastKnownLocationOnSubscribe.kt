package xyz.fcampbell.rxgms.location.onsubscribe.location

import android.content.Context
import android.location.Location
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import rx.Observer
import xyz.fcampbell.rxgms.location.onsubscribe.BaseLocationOnSubscribe

internal class LastKnownLocationOnSubscribe(
        ctx: Context
) : BaseLocationOnSubscribe<Location>(ctx) {

    override fun onGoogleApiClientReady(apiClient: GoogleApiClient, observer: Observer<in Location>) {
        val location = LocationServices.FusedLocationApi.getLastLocation(apiClient)
        if (location != null) {
            observer.onNext(location)
        }
        observer.onCompleted()
    }
}
