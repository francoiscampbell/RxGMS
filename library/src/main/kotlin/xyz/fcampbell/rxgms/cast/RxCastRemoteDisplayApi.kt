package xyz.fcampbell.rxgms.cast

import android.content.Context
import com.google.android.gms.cast.CastRemoteDisplay
import com.google.android.gms.cast.CastRemoteDisplayApi
import com.google.android.gms.common.api.Scope
import io.reactivex.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi

@Suppress("unused")
class RxCastRemoteDisplayApi(
        apiClientDescriptor: ApiClientDescriptor,
        castRemoteDisplayOptions: CastRemoteDisplay.CastRemoteDisplayOptions,
        vararg scopes: Scope
) : RxGmsApi<CastRemoteDisplayApi, CastRemoteDisplay.CastRemoteDisplayOptions>(
        apiClientDescriptor,
        ApiDescriptor(
                CastRemoteDisplay.API,
                CastRemoteDisplay.CastRemoteDisplayApi,
                castRemoteDisplayOptions,
                *scopes)
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