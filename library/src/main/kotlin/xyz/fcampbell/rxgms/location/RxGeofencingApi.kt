package xyz.fcampbell.rxgms.location

import android.app.PendingIntent
import android.content.Context
import android.support.annotation.RequiresPermission
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import rx.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi
import xyz.fcampbell.rxgms.common.util.pendingResultToObservable

@Suppress("unused")
class RxGeofencingApi(
        apiClientDescriptor: ApiClientDescriptor
) : RxGmsApi<Api.ApiOptions.NoOptions>(
        apiClientDescriptor,
        ApiDescriptor(LocationServices.API)
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
     * Other exceptions will be reported that can be thrown on [com.google.android.gms.location.GeofencingApi.addGeofences]

     * @param geofenceTransitionPendingIntent pending intent to register on geofence transition
     * *
     * @param request                         list of request to add
     * *
     * @return observable that adds request
     */
    @RequiresPermission("android.permission.ACCESS_FINE_LOCATION")
    fun addGeofences(geofenceTransitionPendingIntent: PendingIntent, request: GeofencingRequest): Observable<Status> {
        return apiClient.pendingResultToObservable {
            LocationServices.GeofencingApi.addGeofences(it.first, request, geofenceTransitionPendingIntent)
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
        return apiClient.pendingResultToObservable { LocationServices.GeofencingApi.removeGeofences(it.first, pendingIntent) }
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
        return apiClient.pendingResultToObservable { LocationServices.GeofencingApi.removeGeofences(it.first, requestIds) }
    }
}