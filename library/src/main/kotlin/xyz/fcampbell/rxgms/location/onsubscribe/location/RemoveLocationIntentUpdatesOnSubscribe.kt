package xyz.fcampbell.rxgms.location.onsubscribe.location

import android.app.PendingIntent
import android.content.Context
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationServices
import rx.Observer
import xyz.fcampbell.rxgms.common.exception.StatusException
import xyz.fcampbell.rxgms.location.onsubscribe.BaseLocationOnSubscribe

internal class RemoveLocationIntentUpdatesOnSubscribe(
        ctx: Context,
        private val intent: PendingIntent
) : BaseLocationOnSubscribe<Status>(ctx) {

    override fun onGoogleApiClientReady(apiClient: GoogleApiClient, observer: Observer<in Status>) {
        LocationServices.FusedLocationApi.removeLocationUpdates(apiClient, intent)
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
