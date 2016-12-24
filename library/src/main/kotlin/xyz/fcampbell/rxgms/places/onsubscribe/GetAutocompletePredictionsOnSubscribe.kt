package xyz.fcampbell.rxgms.location.onsubscribe.location

import android.content.Context
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.AutocompletePredictionBuffer
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLngBounds
import rx.Observable
import rx.Observer
import xyz.fcampbell.rxgms.common.onsubscribe.PendingResultOnSubscribe
import xyz.fcampbell.rxgms.location.onsubscribe.BaseLocationOnSubscribe

/**
 * Created by francois on 2016-12-22.
 */
internal class GetAutocompletePredictionsOnSubscribe(
        ctx: Context,
        private val query: String,
        private val bounds: LatLngBounds,
        private val filter: AutocompleteFilter?
) : BaseLocationOnSubscribe<AutocompletePredictionBuffer>(ctx) {
    override fun onGoogleApiClientReady(apiClient: GoogleApiClient, observer: Observer<in AutocompletePredictionBuffer>) {
        val pendingResult = Places.GeoDataApi.getAutocompletePredictions(apiClient, query, bounds, filter)
        Observable.create(PendingResultOnSubscribe(pendingResult)).subscribe(observer)
    }
}