package xyz.fcampbell.rxgms.location.onsubscribe.location

import android.content.Context
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.PlaceFilter
import com.google.android.gms.location.places.PlaceLikelihoodBuffer
import com.google.android.gms.location.places.Places
import rx.Observable
import rx.Observer
import xyz.fcampbell.rxgms.common.onsubscribe.PendingResultOnSubscribe
import xyz.fcampbell.rxgms.location.onsubscribe.BaseLocationOnSubscribe

/**
 * Created by francois on 2016-12-22.
 */
internal class GetCurrentPlaceOnSubscribe(
        ctx: Context,
        private val placeFilter: PlaceFilter?
) : BaseLocationOnSubscribe<PlaceLikelihoodBuffer>(ctx) {
    override fun onGoogleApiClientReady(apiClient: GoogleApiClient, observer: Observer<in PlaceLikelihoodBuffer>) {
        val pendingResult = Places.PlaceDetectionApi.getCurrentPlace(apiClient, placeFilter)
        Observable.create(PendingResultOnSubscribe(pendingResult)).subscribe(observer)
    }
}