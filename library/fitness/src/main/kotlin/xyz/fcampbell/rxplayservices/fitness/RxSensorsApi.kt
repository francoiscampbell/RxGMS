package xyz.fcampbell.rxplayservices.fitness

import android.app.PendingIntent
import android.content.Context
import android.support.annotation.RequiresPermission
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.Scope
import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.SensorsApi
import com.google.android.gms.fitness.request.DataSourcesRequest
import com.google.android.gms.fitness.request.OnDataPointListener
import com.google.android.gms.fitness.request.SensorRequest
import com.google.android.gms.fitness.result.DataSourcesResult
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.RxPlayServicesApi

/**
 * Wraps [Fitness.SensorsApi]
 */
@Suppress("unused")
class RxSensorsApi(
        apiClientDescriptor: ApiClientDescriptor,
        options: Api.ApiOptions.NoOptions,
        vararg scopes: Scope
) : RxPlayServicesApi<SensorsApi, Api.ApiOptions.NoOptions>(
        apiClientDescriptor,
        ApiDescriptor(Fitness.SENSORS_API, Fitness.SensorsApi, options, *scopes)
) {
    constructor(
            context: Context,
            options: Api.ApiOptions.NoOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), options, *scopes)

    @RequiresPermission(
            anyOf = arrayOf("android.permission.ACCESS_FINE_LOCATION", "android.permission.BODY_SENSORS"),
            conditional = true)
    fun findDataSources(request: DataSourcesRequest): Observable<DataSourcesResult> {
        return fromPendingResult { findDataSources(it, request) }
    }

    @RequiresPermission(
            anyOf = arrayOf("android.permission.ACCESS_FINE_LOCATION", "android.permission.BODY_SENSORS"),
            conditional = true)
    fun add(request: SensorRequest, listener: OnDataPointListener): Observable<Status> {
        return fromPendingResult { add(it, request, listener) }
    }

    @RequiresPermission(
            anyOf = arrayOf("android.permission.ACCESS_FINE_LOCATION", "android.permission.BODY_SENSORS"),
            conditional = true)
    fun add(request: SensorRequest, pendingIntent: PendingIntent): Observable<Status> {
        return fromPendingResult { add(it, request, pendingIntent) }
    }

    fun remove(listener: OnDataPointListener): Observable<Status> {
        return fromPendingResult { remove(it, listener) }
    }

    fun remove(pendingIntent: PendingIntent): Observable<Status> {
        return fromPendingResult { remove(it, pendingIntent) }
    }
}