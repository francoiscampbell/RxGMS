package xyz.fcampbell.rxgms.observables.geofence

import android.content.Context
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationServices
import rx.Observer
import xyz.fcampbell.rxgms.observables.StatusException

internal class RemoveGeofenceRequestIdsObservable(ctx: Context, private val geofenceRequestIds: List<String>) : RemoveGeofenceObservable<Status>(ctx) {

    override fun removeGeofences(locationClient: GoogleApiClient, observer: Observer<in Status>) {
        LocationServices.GeofencingApi.removeGeofences(locationClient, geofenceRequestIds)
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
