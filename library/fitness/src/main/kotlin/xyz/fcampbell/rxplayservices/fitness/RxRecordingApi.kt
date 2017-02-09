package xyz.fcampbell.rxplayservices.fitness

import android.content.Context
import android.support.annotation.RequiresPermission
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.Scope
import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.RecordingApi
import com.google.android.gms.fitness.data.DataSource
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Subscription
import com.google.android.gms.fitness.result.ListSubscriptionsResult
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.RxPlayServicesApi

/**
 * Wraps [Fitness.RecordingApi]
 */
@Suppress("unused")
class RxRecordingApi(
        apiClientDescriptor: ApiClientDescriptor,
        options: Api.ApiOptions.NoOptions,
        vararg scopes: Scope
) : RxPlayServicesApi<RecordingApi, Api.ApiOptions.NoOptions>(
        apiClientDescriptor,
        ApiDescriptor(Fitness.RECORDING_API, Fitness.RecordingApi, options, *scopes)
) {
    constructor(
            context: Context,
            options: Api.ApiOptions.NoOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), options, *scopes)

    @RequiresPermission(
            anyOf = arrayOf("android.permission.ACCESS_FINE_LOCATION", "android.permission.BODY_SENSORS"),
            conditional = true)
    fun subscribe(dataType: DataType): Observable<Status> {
        return fromPendingResult { subscribe(it, dataType) }
    }

    @RequiresPermission(
            anyOf = arrayOf("android.permission.ACCESS_FINE_LOCATION", "android.permission.BODY_SENSORS"),
            conditional = true)
    fun subscribe(dataSource: DataSource): Observable<Status> {
        return fromPendingResult { subscribe(it, dataSource) }
    }

    fun unsubscribe(dataType: DataType): Observable<Status> {
        return fromPendingResult { unsubscribe(it, dataType) }
    }

    fun unsubscribe(dataSource: DataSource): Observable<Status> {
        return fromPendingResult { unsubscribe(it, dataSource) }
    }

    fun unsubscribe(subscription: Subscription): Observable<Status> {
        return fromPendingResult { unsubscribe(it, subscription) }
    }

    fun listSubscriptions(): Observable<ListSubscriptionsResult> {
        return fromPendingResult { listSubscriptions(it) }
    }

    fun listSubscriptions(dataType: DataType): Observable<ListSubscriptionsResult> {
        return fromPendingResult { listSubscriptions(it, dataType) }
    }
}