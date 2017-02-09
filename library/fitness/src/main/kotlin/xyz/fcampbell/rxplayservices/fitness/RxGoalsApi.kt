package xyz.fcampbell.rxplayservices.fitness

import android.content.Context
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.Scope
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.GoalsApi
import com.google.android.gms.fitness.request.GoalsReadRequest
import com.google.android.gms.fitness.result.GoalsResult
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.RxPlayServicesApi

/**
 * Wraps [Fitness.GoalsApi]
 */
@Suppress("unused")
class RxGoalsApi(
        apiClientDescriptor: ApiClientDescriptor,
        options: Api.ApiOptions.NoOptions,
        vararg scopes: Scope
) : RxPlayServicesApi<GoalsApi, Api.ApiOptions.NoOptions>(
        apiClientDescriptor,
        ApiDescriptor(Fitness.GOALS_API, Fitness.GoalsApi, options, *scopes)
) {
    constructor(
            context: Context,
            options: Api.ApiOptions.NoOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), options, *scopes)

    fun readCurrentGoals(request: GoalsReadRequest): Observable<GoalsResult> {
        return fromPendingResult { readCurrentGoals(it, request) }
    }
}