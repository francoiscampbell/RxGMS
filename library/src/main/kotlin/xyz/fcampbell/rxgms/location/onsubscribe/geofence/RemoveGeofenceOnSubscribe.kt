package xyz.fcampbell.rxgms.location.onsubscribe.geofence

import android.app.PendingIntent
import android.content.Context
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import rx.Observable
import rx.Observer
import xyz.fcampbell.rxgms.location.onsubscribe.BaseLocationOnSubscribe

internal abstract class RemoveGeofenceOnSubscribe<T> protected constructor(
        ctx: Context
) : BaseLocationOnSubscribe<T>(ctx) {

    override fun onGoogleApiClientReady(apiClient: GoogleApiClient, observer: Observer<in T>) {
        removeGeofences(apiClient, observer)
    }

    protected abstract fun removeGeofences(locationClient: GoogleApiClient, observer: Observer<in T>)

    companion object {
        @JvmStatic
        fun createObservable(
                ctx: Context, pendingIntent: PendingIntent): Observable<Status> {
            return Observable.create(RemoveGeofenceByPendingIntentOnSubscribe(ctx, pendingIntent))
        }

        @JvmStatic
        fun createObservable(
                ctx: Context, requestIds: List<String>): Observable<Status> {
            return Observable.create(RemoveGeofenceRequestIdsOnSubscribe(ctx, requestIds))
        }
    }

}
