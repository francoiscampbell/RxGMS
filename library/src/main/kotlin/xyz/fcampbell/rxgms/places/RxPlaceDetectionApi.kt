package xyz.fcampbell.rxgms.places

import android.content.Context
import com.google.android.gms.location.places.*
import io.reactivex.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi

@Suppress("unused")
class RxPlaceDetectionApi(
        apiClientDescriptor: ApiClientDescriptor
) : RxGmsApi<PlaceDetectionApi, PlacesOptions>(
        apiClientDescriptor,
        ApiDescriptor(Places.PLACE_DETECTION_API, Places.PlaceDetectionApi)
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
        return fromPendingResult { getCurrentPlace(it, placeFilter) }
    }
}