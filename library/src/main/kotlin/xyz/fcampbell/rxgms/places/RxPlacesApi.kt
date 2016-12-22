package xyz.fcampbell.rxgms.location

import android.content.Context
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Result
import com.google.android.gms.location.places.*
import com.google.android.gms.maps.model.LatLngBounds
import rx.Observable
import xyz.fcampbell.rxgms.common.onsubscribe.GoogleApiClientOnSubscribe
import xyz.fcampbell.rxgms.common.onsubscribe.PendingResultOnSubscribe

/**
 * Reactive way to access Google Play Location APIs
 */
class RxPlacesApi internal constructor(private val ctx: Context) {
    /**
     * Returns observable that fetches current place from Places API. To flatmap and auto release
     * buffer to [com.google.android.gms.location.places.PlaceLikelihood] observable use
     * [DataBufferObservable].

     * @param placeFilter filter
     * *
     * @return observable that emits current places buffer and completes
     */
    fun getCurrentPlace(placeFilter: PlaceFilter?): Observable<PlaceLikelihoodBuffer> {
        return getGoogleApiClientObservable(Places.PLACE_DETECTION_API, Places.GEO_DATA_API)
                .flatMap { api ->
                    fromPendingResult(Places.PlaceDetectionApi.getCurrentPlace(api, placeFilter))
                }
    }

    /**
     * Returns observable that fetches a place from the Places API using the place ID.

     * @param placeId id for place
     * *
     * @return observable that emits places buffer and completes
     */
    fun getPlaceById(placeId: String?): Observable<PlaceBuffer> {
        return getGoogleApiClientObservable(Places.PLACE_DETECTION_API, Places.GEO_DATA_API)
                .flatMap { api ->
                    fromPendingResult(Places.GeoDataApi.getPlaceById(api, placeId))
                }
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
        return getGoogleApiClientObservable(Places.PLACE_DETECTION_API, Places.GEO_DATA_API)
                .flatMap { api ->
                    fromPendingResult(Places.GeoDataApi.getAutocompletePredictions(api, query, bounds, filter))
                }
    }

    /**
     * Returns observable that fetches photo metadata from the Places API using the place ID.

     * @param placeId id for place
     * *
     * @return observable that emits metadata buffer and completes
     */
    fun getPlacePhotos(placeId: String): Observable<PlacePhotoMetadataResult> {
        return getGoogleApiClientObservable(Places.PLACE_DETECTION_API, Places.GEO_DATA_API)
                .flatMap { api ->
                    fromPendingResult(Places.GeoDataApi.getPlacePhotos(api, placeId))
                }
    }

    /**
     * Returns observable that fetches a placePhotoMetadata from the Places API using the place placePhotoMetadata metadata.
     * Use after fetching the place placePhotoMetadata metadata with [RxGms.getPhotoMetadataById]

     * @param placePhotoMetadata the place photo meta data
     * *
     * @return observable that emits the photo result and completes
     */
    fun getPhoto(placePhotoMetadata: PlacePhotoMetadata): Observable<PlacePhotoResult> {
        return getGoogleApiClientObservable(Places.PLACE_DETECTION_API, Places.GEO_DATA_API)
                .flatMap { api ->
                    fromPendingResult(placePhotoMetadata.getPhoto(api))
                }
    }

    /**
     * Observable that emits [com.google.android.gms.common.api.GoogleApiClient] object after connection.
     * In case of error [GoogleApiConnectionException] is emmited.
     * When connection to Google Play Services is suspended [GoogleApiConnectionSuspendedException]
     * is emitted as error.
     * Do not disconnect from apis client manually - just unsubscribe.

     * @param apis collection of apis to connect to
     * *
     * @return observable that emits apis client after successful connection
     */
    fun getGoogleApiClientObservable(vararg apis: Api<out Api.ApiOptions.NotRequiredOptions>): Observable<GoogleApiClient> {
        return GoogleApiClientOnSubscribe.create(ctx, *apis)
    }

    companion object {
        /**
         * Util method that wraps [com.google.android.gms.common.api.PendingResult] in Observable.

         * @param result pending result to wrap
         * *
         * @param <T>    parameter type of result
         * *
         * @return observable that emits pending result and completes
        </T> */
        @JvmStatic
        fun <T : Result> fromPendingResult(result: PendingResult<T>): Observable<T> {
            return Observable.create(PendingResultOnSubscribe(result))
        }
    }
}