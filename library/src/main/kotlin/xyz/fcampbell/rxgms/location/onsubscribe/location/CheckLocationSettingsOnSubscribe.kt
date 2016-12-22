package xyz.fcampbell.rxgms.location.onsubscribe.location

import android.content.Context
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResult
import rx.Observable
import rx.Observer
import xyz.fcampbell.rxgms.common.onsubscribe.PendingResultOnSubscribe
import xyz.fcampbell.rxgms.location.onsubscribe.BaseLocationOnSubscribe

/**
 * Created by francois on 2016-12-22.
 */
internal class CheckLocationSettingsOnSubscribe private constructor(
        ctx: Context,
        private val locationRequest: LocationSettingsRequest
) : BaseLocationOnSubscribe<LocationSettingsResult>(ctx) {
    override fun onGoogleApiClientReady(apiClient: GoogleApiClient, observer: Observer<in LocationSettingsResult>) {
        val pendingResult = LocationServices.SettingsApi.checkLocationSettings(apiClient, locationRequest)
        Observable.create(PendingResultOnSubscribe(pendingResult)).subscribe(observer)
    }

    companion object {
        fun createObservable(ctx: Context, locationRequest: LocationSettingsRequest): Observable<LocationSettingsResult> {
            return Observable.create(CheckLocationSettingsOnSubscribe(ctx, locationRequest))
        }
    }
}