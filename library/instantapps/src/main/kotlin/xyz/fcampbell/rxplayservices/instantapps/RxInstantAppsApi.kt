package xyz.fcampbell.rxplayservices.instantapps

import android.content.Context
import android.content.Intent
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.Scope
import com.google.android.gms.instantapps.InstantApps
import com.google.android.gms.instantapps.InstantAppsApi
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.RxPlayServicesApi

/**
 * Wraps [InstantApps.InstantAppsApi]
 *
 * TODO update param names when documentation becomes available
 */
@Suppress("unused")
class RxInstantAppsApi(
        apiClientDescriptor: ApiClientDescriptor,
        options: Api.ApiOptions.NoOptions,
        vararg scopes: Scope
) : RxPlayServicesApi<InstantAppsApi, Api.ApiOptions.NoOptions>(
        apiClientDescriptor,
        ApiDescriptor(InstantApps.API, InstantApps.InstantAppsApi, options, *scopes)
) {
    constructor(
            context: Context,
            options: Api.ApiOptions.NoOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), options, *scopes)

    fun initializeIntentClient(context: Context): Observable<Boolean> {
        return Observable.just(original.initializeIntentClient(context))
    }

    fun getInstantAppIntent(context: Context, var2: String, var3: Intent): Observable<Intent> {
        return Observable.just(original.getInstantAppIntent(context, var2, var3))
    }

    fun getInstantAppLaunchData(var2: String): Observable<InstantAppsApi.LaunchDataResult> {
        return fromPendingResult { getInstantAppLaunchData(it, var2) }
    }
}