package xyz.fcampbell.rxplayservices.fitness

import android.app.PendingIntent
import android.content.Context
import android.support.annotation.RequiresPermission
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.Scope
import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.HistoryApi
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataDeleteRequest
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.fitness.request.DataUpdateListenerRegistrationRequest
import com.google.android.gms.fitness.request.DataUpdateRequest
import com.google.android.gms.fitness.result.DailyTotalResult
import com.google.android.gms.fitness.result.DataReadResult
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.RxPlayServicesApi

/**
 * Wraps [Fitness.HistoryApi]
 */
@Suppress("unused")
class RxHistoryApi(
        apiClientDescriptor: ApiClientDescriptor,
        options: Api.ApiOptions.NoOptions,
        vararg scopes: Scope
) : RxPlayServicesApi<HistoryApi, Api.ApiOptions.NoOptions>(
        apiClientDescriptor,
        ApiDescriptor(Fitness.HISTORY_API, Fitness.HistoryApi, options, *scopes)
) {
    constructor(
            context: Context,
            options: Api.ApiOptions.NoOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), options, *scopes)

    @RequiresPermission(
            anyOf = arrayOf("android.permission.ACCESS_FINE_LOCATION", "android.permission.BODY_SENSORS"),
            conditional = true)
    fun readData(request: DataReadRequest): Observable<DataReadResult> {
        return fromPendingResult { readData(it, request) }
    }

    fun readDailyTotal(dataType: DataType): Observable<DailyTotalResult> {
        return fromPendingResult { readDailyTotal(it, dataType) }
    }

    fun readDailyTotalFromLocalDevice(dataType: DataType): Observable<DailyTotalResult> {
        return fromPendingResult { readDailyTotalFromLocalDevice(it, dataType) }
    }

    fun insertData(dataSet: DataSet): Observable<Status> {
        return fromPendingResult { insertData(it, dataSet) }
    }

    fun deleteData(dataDeleteRequest: DataDeleteRequest): Observable<Status> {
        return fromPendingResult { deleteData(it, dataDeleteRequest) }
    }

    fun updateData(dataUpdateRequest: DataUpdateRequest): Observable<Status> {
        return fromPendingResult { updateData(it, dataUpdateRequest) }
    }

    @RequiresPermission(
            anyOf = arrayOf("android.permission.ACCESS_FINE_LOCATION", "android.permission.BODY_SENSORS")
            , conditional = true)
    fun registerDataUpdateListener(request: DataUpdateListenerRegistrationRequest): Observable<Status> {
        return fromPendingResult { registerDataUpdateListener(it, request) }
    }

    fun unregisterDataUpdateListener(pendingIntent: PendingIntent): Observable<Status> {
        return fromPendingResult { unregisterDataUpdateListener(it, pendingIntent) }
    }
}