package xyz.fcampbell.rxplayservices.awareness

import android.content.Context
import com.google.android.gms.awareness.Awareness
import com.google.android.gms.awareness.AwarenessOptions
import com.google.android.gms.awareness.FenceApi
import com.google.android.gms.awareness.fence.FenceQueryRequest
import com.google.android.gms.awareness.fence.FenceQueryResult
import com.google.android.gms.awareness.fence.FenceUpdateRequest
import com.google.android.gms.common.api.Scope
import com.google.android.gms.common.api.Status
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.RxPlayServicesApi

/**
 * Wraps [Awareness.FenceApi]
 */
@Suppress("unused")
class RxFenceApi(
        apiClientDescriptor: ApiClientDescriptor,
        options: AwarenessOptions,
        vararg scopes: Scope
) : RxPlayServicesApi<FenceApi, AwarenessOptions>(
        apiClientDescriptor,
        ApiDescriptor(Awareness.API, Awareness.FenceApi, options, *scopes)
) {
    constructor(
            context: Context,
            options: AwarenessOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), options, *scopes)

    fun updateFences(fenceUpdateRequest: FenceUpdateRequest): Observable<Status> {
        return fromPendingResult { updateFences(it, fenceUpdateRequest) }
    }

    fun queryFences(fenceQueryRequest: FenceQueryRequest): Observable<FenceQueryResult> {
        return fromPendingResult { queryFences(it, fenceQueryRequest) }
    }
}