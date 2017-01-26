package xyz.fcampbell.rxgms.games

import android.content.Context
import com.google.android.gms.wearable.NodeApi
import com.google.android.gms.wearable.Wearable
import io.reactivex.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi

/**
 * Created by francois on 2017-01-13.
 */
@Suppress("unused")
class RxNodeApi(
        apiClientDescriptor: ApiClientDescriptor,
        wearableOptions: Wearable.WearableOptions
) : RxGmsApi<NodeApi, Wearable.WearableOptions>(
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