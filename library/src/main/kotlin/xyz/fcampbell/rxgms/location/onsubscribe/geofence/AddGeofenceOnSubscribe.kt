package xyz.fcampbell.rxgms.location.onsubscribe.geofence

import android.app.PendingIntent
import android.content.Context
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import rx.Observer
import xyz.fcampbell.rxgms.common.exception.StatusException
import xyz.fcampbell.rxgms.location.onsubscribe.BaseLocationOnSubscribe

internal class AddGeofenceOnSubscribe(
        ctx: Context,
        private val request: GeofencingRequest,
        private val geofenceTransitionPendingIntent: PendingIntent
) : BaseLocationOnSubscribe<Status>(ctx) {

    override fun onGoogleApiClientReady(apiClient: GoogleApiClient, observer: Observer<in Status>) {
        LocationServices.GeofencingApi.addGeofences(apiClient, request, geofenceTransitionPendingIntent)
                .setResultCallback { status ->
                    if (status.isSuccess) {
                        observer.onNext(status)
                        observer.onCompleted()
                    } else {
                        observer.onError(StatusException(status))
                    }
                }
    }
}
