package xyz.fcampbell.rxgms.location.onsubscribe.geofence

import android.content.Context
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationServices
import rx.Observer
import xyz.fcampbell.rxgms.common.exception.StatusException

internal class RemoveGeofenceRequestIdsOnSubscribe(
        ctx: Context,
        private val geofenceRequestIds: List<String>
) : RemoveGeofenceOnSubscribe<Status>(ctx) {

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
