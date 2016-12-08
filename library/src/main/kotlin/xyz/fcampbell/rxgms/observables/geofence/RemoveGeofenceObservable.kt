package xyz.fcampbell.rxgms.observables.geofence

import android.app.PendingIntent
import android.content.Context
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import rx.Observable
import rx.Observer
import xyz.fcampbell.rxgms.observables.BaseLocationObservable

abstract class RemoveGeofenceObservable<T> protected constructor(ctx: Context) : BaseLocationObservable<T>(ctx) {

    override fun onGoogleApiClientReady(apiClient: GoogleApiClient, observer: Observer<in T>) {
        removeGeofences(apiClient, observer)
    }

    protected abstract fun removeGeofences(locationClient: GoogleApiClient, observer: Observer<in T>)

    companion object {
        @JvmStatic
        fun createObservable(
                ctx: Context, pendingIntent: PendingIntent): Observable<Status> {
            return Observable.create(RemoveGeofenceByPendingIntentObservable(ctx, pendingIntent))
        }

        @JvmStatic
        fun createObservable(
                ctx: Context, requestIds: List<String>): Observable<Status> {
            return Observable.create(RemoveGeofenceRequestIdsObservable(ctx, requestIds))
        }
    }

}
