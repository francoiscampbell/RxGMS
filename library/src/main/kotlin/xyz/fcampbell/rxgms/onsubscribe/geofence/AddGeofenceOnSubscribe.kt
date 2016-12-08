package xyz.fcampbell.rxgms.onsubscribe.geofence

import android.app.PendingIntent
import android.content.Context
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import rx.Observable
import rx.Observer
import xyz.fcampbell.rxgms.onsubscribe.BaseLocationOnSubscribe
import xyz.fcampbell.rxgms.StatusException

class AddGeofenceOnSubscribe private constructor(
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

    companion object {
        @JvmStatic
        fun createObservable(ctx: Context, request: GeofencingRequest, geofenceTransitionPendingIntent: PendingIntent): Observable<Status> {
            return Observable.create(AddGeofenceOnSubscribe(ctx, request, geofenceTransitionPendingIntent))
        }
    }

}
