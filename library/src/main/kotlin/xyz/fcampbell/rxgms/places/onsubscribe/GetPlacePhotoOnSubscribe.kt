package xyz.fcampbell.rxgms.location.onsubscribe.location

import android.content.Context
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.PlacePhotoMetadata
import com.google.android.gms.location.places.PlacePhotoResult
import rx.Observable
import rx.Observer
import xyz.fcampbell.rxgms.common.onsubscribe.PendingResultOnSubscribe
import xyz.fcampbell.rxgms.location.onsubscribe.BaseLocationOnSubscribe

/**
 * Created by francois on 2016-12-22.
 */
internal class GetPlacePhotoOnSubscribe(
        ctx: Context,
        private val placePhotoMetadata: PlacePhotoMetadata
) : BaseLocationOnSubscribe<PlacePhotoResult>(ctx) {
    override fun onGoogleApiClientReady(apiClient: GoogleApiClient, observer: Observer<in PlacePhotoResult>) {
        val pendingResult = placePhotoMetadata.getPhoto(apiClient)
        Observable.create(PendingResultOnSubscribe(pendingResult)).subscribe(observer)
    }
}