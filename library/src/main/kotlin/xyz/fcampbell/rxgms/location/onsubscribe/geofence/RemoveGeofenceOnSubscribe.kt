package xyz.fcampbell.rxgms.location.onsubscribe.geofence

import android.content.Context
import com.google.android.gms.common.api.GoogleApiClient
import rx.Observer
import xyz.fcampbell.rxgms.location.onsubscribe.BaseLocationOnSubscribe

internal abstract class RemoveGeofenceOnSubscribe<T>(
        ctx: Context
) : BaseLocationOnSubscribe<T>(ctx) {

    override fun onGoogleApiClientReady(apiClient: GoogleApiClient, observer: Observer<in T>) {
        removeGeofences(apiClient, observer)
    }

    protected abstract fun removeGeofences(locationClient: GoogleApiClient, observer: Observer<in T>)
}
