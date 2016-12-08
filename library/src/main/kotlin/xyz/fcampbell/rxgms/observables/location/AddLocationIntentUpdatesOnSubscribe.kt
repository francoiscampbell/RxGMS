package xyz.fcampbell.rxgms.observables.location

import android.app.PendingIntent
import android.content.Context
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import rx.Observable
import rx.Observer
import xyz.fcampbell.rxgms.observables.BaseLocationOnSubscribe
import xyz.fcampbell.rxgms.observables.StatusException

class AddLocationIntentUpdatesOnSubscribe private constructor(
        ctx: Context,
        private val locationRequest: LocationRequest,
        private val intent: PendingIntent
) : BaseLocationOnSubscribe<Status>(ctx) {

    override fun onGoogleApiClientReady(apiClient: GoogleApiClient, observer: Observer<in Status>) {
        LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, intent)
                .setResultCallback { status ->
                    if (!status.isSuccess) {
                        observer.onError(StatusException(status))
                    } else {
                        observer.onNext(status)
                        observer.onCompleted()
                    }
                }

    }

    companion object {
        @JvmStatic
        fun createObservable(ctx: Context, locationRequest: LocationRequest, intent: PendingIntent): Observable<Status> {
            return Observable.create(AddLocationIntentUpdatesOnSubscribe(ctx, locationRequest, intent))
        }
    }
}
