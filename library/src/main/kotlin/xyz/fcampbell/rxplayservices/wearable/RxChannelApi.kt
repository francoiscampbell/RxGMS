package xyz.fcampbell.rxplayservices.games

import android.content.Context
import com.google.android.gms.common.api.Status
import com.google.android.gms.wearable.ChannelApi
import com.google.android.gms.wearable.Wearable
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.common.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.common.ApiDescriptor
import xyz.fcampbell.rxplayservices.common.RxGmsApi
import xyz.fcampbell.rxplayservices.drive.RxChannel

/**
 * Wraps [Wearable.ChannelApi]
 */
@Suppress("unused")
class RxChannelApi(
        apiClientDescriptor: ApiClientDescriptor,
        wearableOptions: Wearable.WearableOptions
) : RxGmsApi<ChannelApi, Wearable.WearableOptions>(
        apiClientDescriptor,
        ApiDescriptor(Wearable.API, Wearable.ChannelApi, wearableOptions)
) {
    constructor(
            context: Context,
            wearableOptions: Wearable.WearableOptions
    ) : this(ApiClientDescriptor(context), wearableOptions)

    fun openChannel(nodeId: String, path: String): Observable<RxChannel> {
        return fromPendingResult { openChannel(it, nodeId, path) }
                .map { RxChannel(apiClient, it.channel) }
    }

    fun addListener(listener: ChannelApi.ChannelListener): Observable<Status> {
        return fromPendingResult { addListener(it, listener) }
    }

    fun removeListener(listener: ChannelApi.ChannelListener): Observable<Status> {
        return fromPendingResult { removeListener(it, listener) }
    }
}