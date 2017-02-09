package xyz.fcampbell.rxplayservices.fitness

import android.content.Context
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.Scope
import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.ConfigApi
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.request.DataTypeCreateRequest
import com.google.android.gms.fitness.result.DataTypeResult
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.RxPlayServicesApi

/**
 * Wraps [Fitness.ConfigApi]
 */
@Suppress("unused")
class RxConfigApi(
        apiClientDescriptor: ApiClientDescriptor,
        options: Api.ApiOptions.NoOptions,
        vararg scopes: Scope
) : RxPlayServicesApi<ConfigApi, Api.ApiOptions.NoOptions>(
        apiClientDescriptor,
        ApiDescriptor(Fitness.CONFIG_API, Fitness.ConfigApi, options, *scopes)
) {
    constructor(
            context: Context,
            options: Api.ApiOptions.NoOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), options, *scopes)

    fun createCustomDataType(request: DataTypeCreateRequest): Observable<DataTypeResult> {
        return fromPendingResult { createCustomDataType(it, request) }
    }

    fun readDataType(dataTypeName: String): Observable<DataTypeResult> {
        return fromPendingResult { readDataType(it, dataTypeName) }
    }

    fun disableFit(): Observable<Status> {
        return fromPendingResult { disableFit(it) }
    }
}