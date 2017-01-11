package xyz.fcampbell.rxgms.location

import android.content.Context
import com.google.android.gms.location.places.*
import com.google.android.gms.maps.model.LatLngBounds
import rx.Observable
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi
import xyz.fcampbell.rxgms.common.util.pendingResultToObservable

/**
 * Reactive way to access Google Play Location APIs
 */
@Suppress("unused")
class RxPlacesApi internal constructor(
        context: Context
) : RxGmsApi<PlacesOptions>(
        context,
        ApiDescriptor(arrayOf(ApiDescriptor.OptionsHolder(Places.GEO_DATA_API), ApiDescriptor.OptionsHolder(Places.PLACE_DETECTION_API)))
) {
    /**
     * Returns observable that fetches current place from Places API. To flatmap and auto release
     * buffer to [com.google.android.gms.location.places.PlaceLikelihood] observable use
     * [DataBufferObservable].

     * @param placeFilter filter
     * *
     * @return observable that emits current places buffer and completes
     */
    fun getCurrentPlace(placeFilter: PlaceFilter?): Observable<PlaceLikelihoodBuffer> {
        return apiClient.pendingResultToObservable { Places.PlaceDetectionApi.getCurrentPlace(it, placeFilter) }
    }

    /**
     * Returns observable that fetches a place from the Places API using the place ID.

     * @param placeId id for place
     * *
     * @return observable that emits places buffer and completes
     */
    fun getPlaceById(vararg placeIds: String): Observable<PlaceBuffer> {
        return apiClient.pendingResultToObservable { Places.GeoDataApi.getPlaceById(it, *placeIds) }
    }

    /**
     * Returns observable that fetches autocomplete predictions from Places API. To flatmap and autorelease
     * [com.google.android.gms.location.places.AutocompletePredictionBuffer] you can use
     * [DataBufferObservable].

     * @param query  search query
     * *
     * @param bounds bounds where to fetch suggestions from
     * *
     * @param filter filter
     * *
     * @return observable with suggestions buffer and completes
     */
    fun getPlaceAutocompletePredictions(query: String, bounds: LatLngBounds, filter: AutocompleteFilter?): Observable<AutocompletePredictionBuffer> {
        return apiClient.pendingResultToObservable { Places.GeoDataApi.getAutocompletePredictions(it, query, bounds, filter) }
    }

    /**
     * Returns observable that fetches photo metadata from the Places API using the place ID.

     * @param placeId id for place
     * *
     * @return observable that emits metadata buffer and completes
     */
    fun getPlacePhotos(placeId: String): Observable<PlacePhotoMetadataResult> {
        return apiClient.pendingResultToObservable { Places.GeoDataApi.getPlacePhotos(it, placeId) }
    }

    /**
     * Returns observable that fetches a placePhotoMetadata from the Places API using the place placePhotoMetadata metadata.
     * Use after fetching the place placePhotoMetadata metadata with [RxGms.getPhotoMetadataById]

     * @param placePhotoMetadata the place photo meta data
     * *
     * @return observable that emits the photo result and completes
     */
    fun getPlacePhoto(placePhotoMetadata: PlacePhotoMetadata): Observable<PlacePhotoResult> {
        return apiClient.pendingResultToObservable { placePhotoMetadata.getPhoto(it) }
    }
}