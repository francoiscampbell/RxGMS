package xyz.fcampbell.rxgms.cast

import android.content.Context
import com.google.android.gms.cast.CastRemoteDisplay
import com.google.android.gms.common.api.Scope
import rx.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi
import xyz.fcampbell.rxgms.common.util.pendingResultToObservable

@Suppress("unused")
class RxCastRemoteDisplayApi(
        apiClientDescriptor: ApiClientDescriptor,
        castRemoteDisplayOptions: CastRemoteDisplay.CastRemoteDisplayOptions,
        vararg scopes: Scope
) : RxGmsApi<CastRemoteDisplay.CastRemoteDisplayOptions>(
        apiClientDescriptor,
        ApiDescriptor(CastRemoteDisplay.API, castRemoteDisplayOptions, *scopes)
) {
    constructor(
            context: Context,
            castRemoteDisplayOptions: CastRemoteDisplay.CastRemoteDisplayOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), castRemoteDisplayOptions, *scopes)

    fun startRemoteDisplay(applicationId: String): Observable<CastRemoteDisplay.CastRemoteDisplaySessionResult> {
        return apiClient.pendingResultToObservable { CastRemoteDisplay.CastRemoteDisplayApi.startRemoteDisplay(it.first, applicationId) }
    }

    fun stopRemoteDisplay(): Observable<CastRemoteDisplay.CastRemoteDisplaySessionResult> {
        return apiClient.pendingResultToObservable { CastRemoteDisplay.CastRemoteDisplayApi.stopRemoteDisplay(it.first) }
    }
}