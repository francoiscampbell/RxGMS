package xyz.fcampbell.rxgms.location

import android.app.PendingIntent
import android.content.Context
import android.location.Location
import android.support.annotation.RequiresPermission
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import rx.AsyncEmitter
import rx.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi
import xyz.fcampbell.rxgms.common.util.fromPendingResult
import xyz.fcampbell.rxgms.location.action.location.LocationUpdates
import xyz.fcampbell.rxgms.location.action.location.MockLocation
import xyz.fcampbell.rxgms.location.action.location.RemoveLocationIntentUpdates

@Suppress("unused")
class RxFusedLocationApi(
        apiClientDescriptor: ApiClientDescriptor
) : RxGmsApi<Api.ApiOptions.NoOptions>(
        apiClientDescriptor,
        ApiDescriptor(LocationServices.API)
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
     * can be thrown on [com.google.android.gms.location.FusedLocationProviderApi.getLastLocation].
     * Everything is delivered by [rx.Observer.onError].

     * @return observable that serves last known location
     */
    @RequiresPermission(anyOf = arrayOf("android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"))
    fun getLastLocation(): Observable<Location> {
        return apiClientPair.map { LocationServices.FusedLocationApi.getLastLocation(it.first) }
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
        return apiClientPair.flatMap {
            Observable.fromEmitter(
                    MockLocation(it.first, sourceLocationObservable),
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
        return apiClientPair.flatMap {
            Observable.fromEmitter(
                    LocationUpdates(it.first, locationRequest),
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
        return apiClientPair.fromPendingResult {
            LocationServices.FusedLocationApi.requestLocationUpdates(it.first, locationRequest, pendingIntent)
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
        return apiClientPair.flatMap {
            Observable.fromEmitter(
                    RemoveLocationIntentUpdates(it.first, intent),
                    AsyncEmitter.BackpressureMode.LATEST)
        }
    }
}