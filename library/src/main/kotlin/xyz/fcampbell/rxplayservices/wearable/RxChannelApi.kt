package xyz.fcampbell.rxplayservices.games

import android.content.Context
import com.google.android.gms.common.api.Status
import com.google.android.gms.wearable.ChannelApi
import com.google.android.gms.wearable.Wearable
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.RxPlayServicesApi
import xyz.fcampbell.rxplayservices.drive.RxChannel

/**
 * Wraps [Wearable.ChannelApi]
 */
@Suppress("unused")
class RxChannelApi(
        apiClientDescriptor: ApiClientDescriptor,
        wearableOptions: Wearable.WearableOptions
) : RxPlayServicesApi<ChannelApi, Wearable.WearableOptions>(
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