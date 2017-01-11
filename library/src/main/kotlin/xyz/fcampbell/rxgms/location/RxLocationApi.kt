package xyz.fcampbell.rxgms.location

import android.app.PendingIntent
import android.content.Context
import android.location.Address
import android.location.Location
import android.support.annotation.RequiresPermission
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLngBounds
import rx.AsyncEmitter
import rx.Observable
import rx.schedulers.Schedulers
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi
import xyz.fcampbell.rxgms.common.util.pendingResultToObservable
import xyz.fcampbell.rxgms.location.action.geocode.Geocode
import xyz.fcampbell.rxgms.location.action.geocode.ReverseGeocode
import xyz.fcampbell.rxgms.location.action.location.LocationUpdates
import xyz.fcampbell.rxgms.location.action.location.MockLocation
import xyz.fcampbell.rxgms.location.action.location.RemoveLocationIntentUpdates
import java.util.*

/**
 * Reactive way to access Google Play Location APIs
 */
@Suppress("unused")
class RxLocationApi internal constructor(
        private val context: Context
) : RxGmsApi<Api.ApiOptions.NoOptions>(
        context,
        ApiDescriptor(arrayOf(ApiDescriptor.OptionsHolder(LocationServices.API)))
) {
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
    fun getLastLocation(): Observable<Location> {
        return rxApiClient.map {
            LocationServices.FusedLocationApi.getLastLocation(it)
        }
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
        return rxApiClient.flatMap {
            Observable.fromEmitter(
                    MockLocation(it, sourceLocationObservable),
                    AsyncEmitter.BackpressureMode.LATEST)
        }
    }

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
    fun requestLocationUpdates(locationRequest: LocationRequest): Observable<Location> {
        return rxApiClient.flatMap {
            Observable.fromEmitter(
                    LocationUpdates(it, locationRequest),
                    AsyncEmitter.BackpressureMode.LATEST)
        }
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
     * @param pendingIntent          PendingIntent that will be called with location updates
     * *
     * @return observable that adds the request and PendingIntent
     */
    @RequiresPermission(anyOf = arrayOf("android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"))
    fun requestLocationUpdates(locationRequest: LocationRequest, pendingIntent: PendingIntent): Observable<Status> {
        return rxApiClient.pendingResultToObservable {
            LocationServices.FusedLocationApi.requestLocationUpdates(it, locationRequest, pendingIntent)
        }
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
        return rxApiClient.flatMap {
            Observable.fromEmitter(
                    RemoveLocationIntentUpdates(it, intent),
                    AsyncEmitter.BackpressureMode.LATEST)
        }
    }

    /**
     * Creates observable that translates latitude and longitude to list of possible addresses using
     * included Geocoder class. In case geocoder fails with IOException("Service not Available") fallback
     * decoder is used using google web service. You should subscribe for this observable on I/O thread.
     * The stream finishes after address list is available.

     * @param lat        latitude
     * *
     * @param lng        longitude
     * *
     * @param maxResults maximal number of results you are interested in
     * *
     * @return observable that serves list of address based on location
     */
    fun reverseGeocode(lat: Double, lng: Double, maxResults: Int): Observable<List<Address>> {
        return reverseGeocode(Locale.getDefault(), lat, lng, maxResults)
    }

    /**
     * Creates observable that translates latitude and longitude to list of possible addresses using
     * included Geocoder class. In case geocoder fails with IOException("Service not Available") fallback
     * decoder is used using google web service. You should subscribe for this observable on I/O thread.
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
    fun reverseGeocode(locale: Locale, lat: Double, lng: Double, maxResults: Int): Observable<List<Address>> {
        return Observable.fromEmitter(
                ReverseGeocode(context, locale, lat, lng, maxResults),
                AsyncEmitter.BackpressureMode.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.trampoline())
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
    @JvmOverloads fun geocode(locationName: String, maxResults: Int, bounds: LatLngBounds? = null): Observable<List<Address>> {
        return Observable.create(Geocode(context, locationName, maxResults, bounds))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.trampoline())
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
        return rxApiClient.pendingResultToObservable {
            LocationServices.GeofencingApi.addGeofences(it, request, geofenceTransitionPendingIntent)
        }
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
        return rxApiClient.pendingResultToObservable { LocationServices.GeofencingApi.removeGeofences(it, pendingIntent) }
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
        return rxApiClient.pendingResultToObservable { LocationServices.GeofencingApi.removeGeofences(it, requestIds) }
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
        return rxApiClient.pendingResultToObservable { LocationServices.SettingsApi.checkLocationSettings(it, locationRequest) }
    }
}