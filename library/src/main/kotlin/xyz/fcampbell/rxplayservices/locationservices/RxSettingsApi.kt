package xyz.fcampbell.rxplayservices.locationservices

import android.content.Context
import com.google.android.gms.common.api.Api
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResult
import com.google.android.gms.location.SettingsApi
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.common.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.common.ApiDescriptor
import xyz.fcampbell.rxplayservices.common.RxGmsApi

/**
 * Wraps [LocationServices.SettingsApi]
 */
@Suppress("unused")
class RxSettingsApi(
        apiClientDescriptor: ApiClientDescriptor
) : RxGmsApi<SettingsApi, Api.ApiOptions.NoOptions>(
        apiClientDescriptor,
        ApiDescriptor(LocationServices.API, LocationServices.SettingsApi)
) {
    constructor(
            context: Context
    ) : this(ApiClientDescriptor(context))

    /**
     * Observable that can be used to check settings state for given location request.

     * @param locationRequest location request
     * *
     * @return observable that emits check result of location settings
     * *
     * @see com.google.android.gms.location.SettingsApi
     */
    fun checkLocationSettings(locationRequest: LocationSettingsRequest): Observable<LocationSettingsResult> {
        return fromPendingResult { checkLocationSettings(it, locationRequest) }
    }
}