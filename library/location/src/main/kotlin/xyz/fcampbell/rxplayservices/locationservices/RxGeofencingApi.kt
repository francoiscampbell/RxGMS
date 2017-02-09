package xyz.fcampbell.rxplayservices.locationservices

import android.app.PendingIntent
import android.content.Context
import android.support.annotation.RequiresPermission
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.GeofencingApi
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.RxPlayServicesApi

/**
 * Wraps [LocationServices.GeofencingApi]
 */
@Suppress("unused")
class RxGeofencingApi(
        apiClientDescriptor: ApiClientDescriptor
) : RxPlayServicesApi<GeofencingApi, Api.ApiOptions.NoOptions>(
        apiClientDescriptor,
        ApiDescriptor(LocationServices.API, LocationServices.GeofencingApi)
) {
    constructor(
            context: Context
    ) : this(ApiClientDescriptor(context))

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
     * Other exceptions will be reported that can be thrown on [GeofencingApi.addGeofences]

     * @param geofenceTransitionPendingIntent pending intent to register on geofence transition
     * *
     * @param request                         list of request to add
     * *
     * @return observable that adds request
     */
    @RequiresPermission("android.permission.ACCESS_FINE_LOCATION")
    fun addGeofences(geofenceTransitionPendingIntent: PendingIntent, request: GeofencingRequest): Observable<Status> {
        return fromPendingResult { addGeofences(it, request, geofenceTransitionPendingIntent) }
    }

    /**
     * Observable that can be used to remove geofences from LocationClient.
     *
     *
     * In case of unsuccessful status [StatusException] is delivered.
     *
     *
     * Other exceptions will be reported that can be thrown on [GeofencingApi.removeGeofences].
     *
     *
     * Every exception is delivered by [Observer.onError].

     * @param pendingIntent key of registered geofences
     * *
     * @return observable that removed geofences
     */
    fun removeGeofences(pendingIntent: PendingIntent): Observable<Status> {
        return fromPendingResult { removeGeofences(it, pendingIntent) }
    }

    /**
     * Observable that can be used to remove geofences from LocationClient.
     *
     *
     * In case of unsuccessful status [StatusException] is delivered.
     *
     *
     * Other exceptions will be reported that can be thrown on [GeofencingApi.removeGeofences].
     *
     *
     * Every exception is delivered by [Observer.onError].

     * @param requestIds geofences to remove
     * *
     * @return observable that removed geofences
     */
    fun removeGeofences(requestIds: List<String>): Observable<Status> {
        return fromPendingResult { removeGeofences(it, requestIds) }
    }
}