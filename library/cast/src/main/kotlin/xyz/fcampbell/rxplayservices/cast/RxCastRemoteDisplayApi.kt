package xyz.fcampbell.rxplayservices.cast

import android.content.Context
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.RxPlayServicesApi

/**
 * Wraps [CastRemoteDisplay.CastRemoteDisplayApi]
 */
@Suppress("unused")
class RxCastRemoteDisplayApi(
        apiClientDescriptor: ApiClientDescriptor,
        castRemoteDisplayOptions: CastRemoteDisplay.CastRemoteDisplayOptions,
        vararg scopes: Scope
) : RxPlayServicesApi<CastRemoteDisplayApi, CastRemoteDisplay.CastRemoteDisplayOptions>(
        apiClientDescriptor,
        ApiDescriptor(CastRemoteDisplay.API, CastRemoteDisplay.CastRemoteDisplayApi, castRemoteDisplayOptions, *scopes)
) {
    constructor(
            context: Context,
            castRemoteDisplayOptions: CastRemoteDisplay.CastRemoteDisplayOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), castRemoteDisplayOptions, *scopes)

    fun startRemoteDisplay(applicationId: String): Observable<CastRemoteDisplay.CastRemoteDisplaySessionResult> {
        return fromPendingResult { startRemoteDisplay(it, applicationId) }
    }

    fun stopRemoteDisplay(): Observable<CastRemoteDisplay.CastRemoteDisplaySessionResult> {
        return fromPendingResult { stopRemoteDisplay(it) }
    }
}