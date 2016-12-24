package xyz.fcampbell.rxgms.location.onsubscribe.location

import android.content.Context
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.PlacePhotoMetadataResult
import com.google.android.gms.location.places.Places
import rx.Observable
import rx.Observer
import xyz.fcampbell.rxgms.common.onsubscribe.PendingResultOnSubscribe
import xyz.fcampbell.rxgms.location.onsubscribe.BaseLocationOnSubscribe

/**
 * Created by francois on 2016-12-22.
 */
internal class GetPlacePhotosOnSubscribe(
        ctx: Context,
        private val placeId: String
) : BaseLocationOnSubscribe<PlacePhotoMetadataResult>(ctx) {
    override fun onGoogleApiClientReady(apiClient: GoogleApiClient, observer: Observer<in PlacePhotoMetadataResult>) {
        val pendingResult = Places.GeoDataApi.getPlacePhotos(apiClient, placeId)
        Observable.create(PendingResultOnSubscribe(pendingResult)).subscribe(observer)
    }
}