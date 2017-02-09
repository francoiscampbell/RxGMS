package xyz.fcampbell.rxplayservices.awareness

import android.content.Context
import android.support.annotation.RequiresPermission
import com.google.android.gms.awareness.Awareness
import com.google.android.gms.awareness.AwarenessOptions
import com.google.android.gms.awareness.SnapshotApi
import com.google.android.gms.awareness.snapshot.*
import com.google.android.gms.awareness.state.BeaconState
import com.google.android.gms.common.api.Scope
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.RxPlayServicesApi

/**
 * Wraps [Awareness.SnapshotApi]
 */
@Suppress("unused")
class RxSnapshotApi(
        apiClientDescriptor: ApiClientDescriptor,
        options: AwarenessOptions,
        vararg scopes: Scope
) : RxPlayServicesApi<SnapshotApi, AwarenessOptions>(
        apiClientDescriptor,
        ApiDescriptor(Awareness.API, Awareness.SnapshotApi, options, *scopes)
) {
    constructor(
            context: Context,
            options: AwarenessOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), options, *scopes)

    @RequiresPermission("android.permission.ACCESS_FINE_LOCATION")
    fun getBeaconState(beaconTypes: Collection<BeaconState.TypeFilter>): Observable<BeaconStateResult> {
        return fromPendingResult { getBeaconState(it, beaconTypes) }
    }

    @RequiresPermission("android.permission.ACCESS_FINE_LOCATION")
    fun getBeaconState(vararg beaconTypes: BeaconState.TypeFilter): Observable<BeaconStateResult> {
        return fromPendingResult { getBeaconState(it, *beaconTypes) }
    }

    @RequiresPermission("com.google.android.gms.permission.ACTIVITY_RECOGNITION")
    fun getDetectedActivity(): Observable<DetectedActivityResult> {
        return fromPendingResult { getDetectedActivity(it) }
    }

    fun getHeadphoneState(): Observable<HeadphoneStateResult> {
        return fromPendingResult { getHeadphoneState(it) }
    }

    @RequiresPermission("android.permission.ACCESS_FINE_LOCATION")
    fun getLocation(): Observable<LocationResult> {
        return fromPendingResult { getLocation(it) }
    }

    @RequiresPermission("android.permission.ACCESS_FINE_LOCATION")
    fun getPlaces(): Observable<PlacesResult> {
        return fromPendingResult { getPlaces(it) }
    }

    @RequiresPermission("android.permission.ACCESS_FINE_LOCATION")
    fun getWeather(): Observable<WeatherResult> {
        return fromPendingResult { getWeather(it) }
    }
}