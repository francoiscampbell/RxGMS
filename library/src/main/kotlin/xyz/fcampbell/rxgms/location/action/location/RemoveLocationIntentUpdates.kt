package xyz.fcampbell.rxgms.location.action.location

import android.app.PendingIntent
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationServices
import rx.AsyncEmitter
import xyz.fcampbell.rxgms.common.action.FromEmitter
import xyz.fcampbell.rxgms.common.exception.StatusException

internal class RemoveLocationIntentUpdates(
        private val apiClient: GoogleApiClient,
        private val intent: PendingIntent
) : FromEmitter<Status>() {

    override fun call(emitter: AsyncEmitter<Status>) {
        super.call(emitter)

        LocationServices.FusedLocationApi.removeLocationUpdates(apiClient, intent)
                .setResultCallback { status ->
                    if (status.isSuccess) {
                        emitter.onNext(status)
                        emitter.onCompleted()
                    } else {
                        emitter.onError(StatusException(status))
                    }
                }
    }
}
