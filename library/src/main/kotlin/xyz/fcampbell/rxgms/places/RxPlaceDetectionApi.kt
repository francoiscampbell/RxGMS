package xyz.fcampbell.rxgms.location

import android.content.Context
import com.google.android.gms.location.places.PlaceFilter
import com.google.android.gms.location.places.PlaceLikelihoodBuffer
import com.google.android.gms.location.places.Places
import com.google.android.gms.location.places.PlacesOptions
import rx.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi
import xyz.fcampbell.rxgms.common.util.fromPendingResult

@Suppress("unused")
class RxPlaceDetectionApi(
        apiClientDescriptor: ApiClientDescriptor
) : RxGmsApi<PlacesOptions>(
        apiClientDescriptor,
        ApiDescriptor(Places.PLACE_DETECTION_API)
) {
    constructor(
            context: Context
    ) : this(ApiClientDescriptor(context))

    /**
     * Returns observable that fetches current place from Places API. To flatmap and auto release
     * buffer to [com.google.android.gms.location.places.PlaceLikelihood] observable use
     * [DataBufferObservable].

     * @param placeFilter filter
     * *
     * @return observable that emits current places buffer and completes
     */
    fun getCurrentPlace(placeFilter: PlaceFilter?): Observable<PlaceLikelihoodBuffer> {
        return apiClientPair.fromPendingResult { Places.PlaceDetectionApi.getCurrentPlace(it.first, placeFilter) }
    }
}