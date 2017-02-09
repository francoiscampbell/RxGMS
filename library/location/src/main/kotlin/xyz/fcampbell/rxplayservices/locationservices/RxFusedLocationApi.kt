package xyz.fcampbell.rxplayservices.locationservices

import android.app.PendingIntent
import android.content.Context
import android.location.Location
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.RxPlayServicesApi

/**
 * Wraps [LocationServices.FusedLocationApi]
 */
@Suppress("unused")
class RxFusedLocationApi(
        apiClientDescriptor: ApiClientDescriptor
) : RxPlayServicesApi<FusedLocationProviderApi, Api.ApiOptions.NoOptions>(
        apiClientDescriptor,
        ApiDescriptor(LocationServices.API, LocationServices.FusedLocationApi)
) {
    constructor(
            context: Context
    ) : this(ApiClientDescriptor(context))

    /**
     * Creates observable that obtains last known location and than completes.
     * Delivered location is never null - when it is unavailable Observable completes without emitting
     * any value.
     *
     *
     * Observable can report [GoogleApiConnectionException]
     * when there are trouble connecting with Google Play Services and other exceptions that
     * can be thrown on [FusedLocationProviderApi.getLastLocation].
     * Everything is delivered by [rx.Observer.onError].

     * @return observable that serves last known location
     */
    @RequiresPermission(anyOf = arrayOf("android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"))
    fun getLastLocation(): Observable<Location> {
        return map { getLastLocation(it) }
    }

    /**
     * Returns an observable which activates mock location mode when subscribed to, using the
     * supplied observable as a source of mock locations. Mock locations will replace normal
     * location information for all users of the FusedLocationProvider API on the device while this
     * observable is subscribed to.
     *
     *
     * To use this method, mock locations must be enabled in developer options and your application
     * must hold the android.permission.ACCESS_MOCK_LOCATION permission, or a [SecurityException]
     * will be thrown.
     *
     *
     * All statuses that are not successful will be reported as [StatusException].
     *
     *
     * Every exception is delivered by [Observer.onError].

     * @param sourceLocationObservable observable that emits [Location] instances suitable to use as mock locations
     * *
     * @return observable that emits [Status]
     */
    @RequiresPermission(allOf = arrayOf("android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_MOCK_LOCATION"))
    fun mockLocation(sourceLocationObservable: Observable<Location>): Observable<Status> {
        return create { MockLocation(it, sourceLocationObservable) }
    }

    /**
     * Creates observable that allows to observe infinite stream of location updates.
     * To stop the stream you have to unsubscribe from observable - location updates are
     * then disconnected.
     *
     *
     * Observable can report [GoogleApiConnectionException]
     * when there are trouble connecting with Google Play Services and other exceptions that
     * can be thrown on [FusedLocationProviderApi.requestLocationUpdates].
     * Everything is delivered by [rx.Observer.onError].

     * @param locationRequest request object with info about what kind of location you need
     * *
     * @return observable that serves infinite stream of location updates
     */
    @RequiresPermission(anyOf = arrayOf("android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"))
    fun requestLocationUpdates(locationRequest: LocationRequest): Observable<Location> {
        return create { LocationUpdates(it, locationRequest) }
    }

    /**
     * Creates an observable that adds a [PendingIntent] as a location listener.
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
        return fromPendingResult { requestLocationUpdates(it, locationRequest, pendingIntent) }
    }

    /**
     * Observable that can be used to remove [PendingIntent] location updates.
     *
     *
     * In case of unsuccessful status [StatusException] is delivered.

     * @param intent PendingIntent to remove location updates for
     * *
     * @return observable that removes the PendingIntent
     */
    fun removeLocationUpdates(intent: PendingIntent): Observable<Status> {
        return fromPendingResult { LocationServices.FusedLocationApi.removeLocationUpdates(it, intent) }
    }
}