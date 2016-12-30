package xyz.fcampbell.rxgms.location

import android.content.Context
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.PlaceFilter
import com.google.android.gms.location.places.PlacePhotoMetadata
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLngBounds
import rx.Single
import xyz.fcampbell.rxgms.RxGmsApi
import xyz.fcampbell.rxgms.location.action.location.*

/**
 * Reactive way to access Google Play Location APIs
 */
class RxPlacesApi internal constructor(
        private val context: Context
) : RxGmsApi(context, Places.GEO_DATA_API, Places.PLACE_DETECTION_API) {
    /**
     * Returns observable that fetches current place from Places API. To flatmap and auto release
     * buffer to [com.google.android.gms.location.places.PlaceLikelihood] observable use
     * [DataBufferObservable].

     * @param placeFilter filter
     * *
     * @return observable that emits current places buffer and completes
     */
    fun getCurrentPlace(placeFilter: PlaceFilter?) = rxApiClient.flatMap {
        Single.create(GetCurrentPlace(it, placeFilter)).toObservable()
    }

    /**
     * Returns observable that fetches a place from the Places API using the place ID.

     * @param placeId id for place
     * *
     * @return observable that emits places buffer and completes
     */
    fun getPlaceById(placeId: String) = rxApiClient.flatMap {
        Single.create(GetPlaceById(it, placeId)).toObservable()
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
    fun getPlaceAutocompletePredictions(query: String, bounds: LatLngBounds, filter: AutocompleteFilter?) = rxApiClient.flatMap {
        Single.create(GetAutocompletePredictions(it, query, bounds, filter)).toObservable()
    }

    /**
     * Returns observable that fetches photo metadata from the Places API using the place ID.

     * @param placeId id for place
     * *
     * @return observable that emits metadata buffer and completes
     */
    fun getPlacePhotos(placeId: String) = rxApiClient.flatMap {
        Single.create(GetPlacePhotos(it, placeId)).toObservable()
    }

    /**
     * Returns observable that fetches a placePhotoMetadata from the Places API using the place placePhotoMetadata metadata.
     * Use after fetching the place placePhotoMetadata metadata with [RxGms.getPhotoMetadataById]

     * @param placePhotoMetadata the place photo meta data
     * *
     * @return observable that emits the photo result and completes
     */
    fun getPlacePhoto(placePhotoMetadata: PlacePhotoMetadata) = rxApiClient.flatMap {
        Single.create(GetPlacePhoto(it, placePhotoMetadata)).toObservable()
    }
}