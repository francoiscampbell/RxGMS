package xyz.fcampbell.rxgms.location

import android.app.PendingIntent
import android.content.Context
import android.location.Address
import android.location.Location
import android.support.annotation.RequiresPermission
import com.google.android.gms.common.api.*
import com.google.android.gms.location.*
import com.google.android.gms.location.places.*
import com.google.android.gms.maps.model.LatLngBounds
import rx.Observable
import xyz.fcampbell.rxgms.common.onsubscribe.GoogleApiClientOnSubscribe
import xyz.fcampbell.rxgms.common.onsubscribe.PendingResultOnSubscribe
import xyz.fcampbell.rxgms.location.onsubscribe.geocode.GeocodeOnSubscribe
import xyz.fcampbell.rxgms.location.onsubscribe.geocode.ReverseGeocodeOnSubscribe
import xyz.fcampbell.rxgms.location.onsubscribe.geofence.AddGeofenceOnSubscribe
import xyz.fcampbell.rxgms.location.onsubscribe.geofence.RemoveGeofenceOnSubscribe
import xyz.fcampbell.rxgms.location.onsubscribe.location.*
import java.util.*

/**
 * Reactive way to access Google Play Location APIs
 */
class RxLocationApi internal constructor(private val ctx: Context) {
    /**
     * Creates observable that obtains last known location and than completes.
     * Delivered location is never null - when it is unavailable Observable completes without emitting
     * any value.
     *
     *
     * Observable can report [GoogleApiConnectionException]
     * when there are trouble connecting with Google Play Services and other exceptions that
     * can be thrown on [com.google.android.gms.location.FusedLocationProviderApi.getLastLocation].
     * Everything is delivered by [rx.Observer.onError].

     * @return observable that serves last known location
     */
    @RequiresPermission(anyOf = arrayOf("android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"))
    fun getLastKnownLocation() = LastKnownLocationOnSubscribe.createObservable(ctx)

    /**
     * Creates observable that allows to observe infinite stream of location updates.
     * To stop the stream you have to unsubscribe from observable - location updates are
     * then disconnected.
     *
     *
     * Observable can report [GoogleApiConnectionException]
     * when there are trouble connecting with Google Play Services and other exceptions that
     * can be thrown on [com.google.android.gms.location.FusedLocationProviderApi.requestLocationUpdates].
     * Everything is delivered by [rx.Observer.onError].

     * @param locationRequest request object with info about what kind of location you need
     * *
     * @return observable that serves infinite stream of location updates
     */
    @RequiresPermission(anyOf = arrayOf("android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"))
    fun getUpdatedLocation(locationRequest: LocationRequest): Observable<Location> {
        return LocationUpdatesOnSubscribe.createObservable(ctx, locationRequest)
    }

    /**
     * Returns an observable which activates mock location mode when subscribed to, using the
     * supplied observable as a source of mock locations. Mock locations will replace normal
     * location information for all users of the FusedLocationProvider API on the device while this
     * observable is subscribed to.
     *
     *
     * To use this method, mock locations must be enabled in developer options and your application
     * must hold the android.permission.ACCESS_MOCK_LOCATION permission, or a [java.lang.SecurityException]
     * will be thrown.
     *
     *
     * All statuses that are not successful will be reported as [StatusException].
     *
     *
     * Every exception is delivered by [rx.Observer.onError].

     * @param sourceLocationObservable observable that emits [android.location.Location] instances suitable to use as mock locations
     * *
     * @return observable that emits [com.google.android.gms.common.api.Status]
     */
    @RequiresPermission(allOf = arrayOf("android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_MOCK_LOCATION"))
    fun mockLocation(sourceLocationObservable: Observable<Location>): Observable<Status> {
        return MockLocationOnSubscribe.createObservable(ctx, sourceLocationObservable)
    }

    /**
     * Creates an observable that adds a [android.app.PendingIntent] as a location listener.
     *
     *
     * This invokes [com.google.android.gms.location.FusedLocationProviderApi.requestLocationUpdates].
     *
     *
     * When location updates are no longer required, a call to [.removeLocationUpdates]
     * should be made.
     *
     *
     * In case of unsuccessful status [StatusException] is delivered.

     * @param locationRequest request object with info about what kind of location you need
     * *
     * @param intent          PendingIntent that will be called with location updates
     * *
     * @return observable that adds the request and PendingIntent
     */
    @RequiresPermission(anyOf = arrayOf("android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"))
    fun requestLocationUpdates(locationRequest: LocationRequest, intent: PendingIntent): Observable<Status> {
        return AddLocationIntentUpdatesOnSubscribe.createObservable(ctx, locationRequest, intent)
    }

    /**
     * Observable that can be used to remove [android.app.PendingIntent] location updates.
     *
     *
     * In case of unsuccessful status [StatusException] is delivered.

     * @param intent PendingIntent to remove location updates for
     * *
     * @return observable that removes the PendingIntent
     */
    fun removeLocationUpdates(intent: PendingIntent): Observable<Status> {
        return RemoveLocationIntentUpdatesOnSubscribe.createObservable(ctx, intent)
    }

    /**
     * Creates observable that translates latitude and longitude to list of possible addresses using
     * included Geocoder class. In case geocoder fails with IOException("Service not Available") fallback
     * decoder is used using google web api. You should subscribe for this observable on I/O thread.
     * The stream finishes after address list is available.

     * @param lat        latitude
     * *
     * @param lng        longitude
     * *
     * @param maxResults maximal number of results you are interested in
     * *
     * @return observable that serves list of address based on location
     */
    fun getReverseGeocodeObservable(lat: Double, lng: Double, maxResults: Int): Observable<List<Address>> {
        return ReverseGeocodeOnSubscribe.createObservable(ctx, Locale.getDefault(), lat, lng, maxResults)
    }

    /**
     * Creates observable that translates latitude and longitude to list of possible addresses using
     * included Geocoder class. In case geocoder fails with IOException("Service not Available") fallback
     * decoder is used using google web api. You should subscribe for this observable on I/O thread.
     * The stream finishes after address list is available.

     * @param locale     locale for address language
     * *
     * @param lat        latitude
     * *
     * @param lng        longitude
     * *
     * @param maxResults maximal number of results you are interested in
     * *
     * @return observable that serves list of address based on location
     */
    fun getReverseGeocodeObservable(locale: Locale, lat: Double, lng: Double, maxResults: Int): Observable<List<Address>> {
        return ReverseGeocodeOnSubscribe.createObservable(ctx, locale, lat, lng, maxResults)
    }

    /**
     * Creates observable that translates a street address or other description into a list of
     * possible addresses using included Geocoder class. You should subscribe for this
     * observable on I/O thread.
     * The stream finishes after address list is available.
     *
     * You may specify a bounding box for the search results.

     * @param locationName a user-supplied description of a location
     * *
     * @param maxResults   max number of results you are interested in
     * *
     * @param bounds       restricts the results to geographical bounds. May be null
     * *
     * @return observable that serves list of address based on location name
     */
    @JvmOverloads fun getGeocodeObservable(locationName: String, maxResults: Int, bounds: LatLngBounds? = null): Observable<List<Address>> {
        return GeocodeOnSubscribe.createObservable(ctx, locationName, maxResults, bounds)
    }

    /**
     * Creates observable that adds request and completes when the action is done.
     *
     *
     * Observable can report [GoogleApiConnectionException]
     * when there are trouble connecting with Google Play Services.
     *
     *
     * In case of unsuccessful status [StatusException] is delivered.
     *
     *
     * Other exceptions will be reported that can be thrown on [com.google.android.gms.location.GeofencingApi.addGeofences]

     * @param geofenceTransitionPendingIntent pending intent to register on geofence transition
     * *
     * @param request                         list of request to add
     * *
     * @return observable that adds request
     */
    @RequiresPermission("android.permission.ACCESS_FINE_LOCATION")
    fun addGeofences(geofenceTransitionPendingIntent: PendingIntent, request: GeofencingRequest): Observable<Status> {
        return AddGeofenceOnSubscribe.createObservable(ctx, request, geofenceTransitionPendingIntent)
    }

    /**
     * Observable that can be used to remove geofences from LocationClient.
     *
     *
     * In case of unsuccessful status [StatusException] is delivered.
     *
     *
     * Other exceptions will be reported that can be thrown on [com.google.android.gms.location.GeofencingApi.removeGeofences].
     *
     *
     * Every exception is delivered by [rx.Observer.onError].

     * @param pendingIntent key of registered geofences
     * *
     * @return observable that removed geofences
     */
    fun removeGeofences(pendingIntent: PendingIntent): Observable<Status> {
        return RemoveGeofenceOnSubscribe.createObservable(ctx, pendingIntent)
    }

    /**
     * Observable that can be used to remove geofences from LocationClient.
     *
     *
     * In case of unsuccessful status [StatusException] is delivered.
     *
     *
     * Other exceptions will be reported that can be thrown on [com.google.android.gms.location.GeofencingApi.removeGeofences].
     *
     *
     * Every exception is delivered by [rx.Observer.onError].

     * @param requestIds geofences to remove
     * *
     * @return observable that removed geofences
     */
    fun removeGeofences(requestIds: List<String>): Observable<Status> {
        return RemoveGeofenceOnSubscribe.createObservable(ctx, requestIds)
    }

    /**
     * Observable that can be used to check settings state for given location request.

     * @param locationRequest location request
     * *
     * @return observable that emits check result of location settings
     * *
     * @see com.google.android.gms.location.SettingsApi
     */
    fun checkLocationSettings(locationRequest: LocationSettingsRequest): Observable<LocationSettingsResult> {
        return getGoogleApiClientObservable(LocationServices.API)
                .flatMap { googleApiClient ->
                    fromPendingResult(LocationServices.SettingsApi.checkLocationSettings(googleApiClient, locationRequest))
                }
    }

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
    fun getPhotoMetadataById(placeId: String): Observable<PlacePhotoMetadataResult> {
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
    fun getPhotoForMetadata(placePhotoMetadata: PlacePhotoMetadata): Observable<PlacePhotoResult> {
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