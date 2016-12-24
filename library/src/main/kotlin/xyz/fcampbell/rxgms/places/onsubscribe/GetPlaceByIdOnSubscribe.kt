package xyz.fcampbell.rxgms.location.onsubscribe.location

import android.content.Context
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.PlaceBuffer
import com.google.android.gms.location.places.Places
import rx.Observable
import rx.Observer
import xyz.fcampbell.rxgms.common.onsubscribe.PendingResultOnSubscribe
import xyz.fcampbell.rxgms.location.onsubscribe.BaseLocationOnSubscribe

/**
 * Created by francois on 2016-12-22.
 */
internal class GetPlaceByIdOnSubscribe(
        ctx: Context,
        private val placeId: String
) : BaseLocationOnSubscribe<PlaceBuffer>(ctx) {
    override fun onGoogleApiClientReady(apiClient: GoogleApiClient, observer: Observer<in PlaceBuffer>) {
        val pendingResult = Places.GeoDataApi.getPlaceById(apiClient, placeId)
        Observable.create(PendingResultOnSubscribe(pendingResult)).subscribe(observer)
    }
}