package xyz.fcampbell.rxplayservices.wearable

import android.content.Context
import com.google.android.gms.wearable.NodeApi
import com.google.android.gms.wearable.Wearable
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.RxPlayServicesApi

/**
 * Wraps [Wearable.NodeApi]
 */
@Suppress("unused")
class RxNodeApi(
        apiClientDescriptor: ApiClientDescriptor,
        wearableOptions: Wearable.WearableOptions
) : RxPlayServicesApi<NodeApi, Wearable.WearableOptions>(
        apiClientDescriptor,
        ApiDescriptor(Wearable.API, Wearable.NodeApi, wearableOptions)
) {
    constructor(
            context: Context,
            wearableOptions: Wearable.WearableOptions
    ) : this(ApiClientDescriptor(context), wearableOptions)

    fun getLocalNode(): Observable<NodeApi.GetLocalNodeResult> {
        return fromPendingResult { getLocalNode(it) }
    }

    fun getConnectedNodes(): Observable<NodeApi.GetConnectedNodesResult> {
        return fromPendingResult { getConnectedNodes(it) }
    }
}