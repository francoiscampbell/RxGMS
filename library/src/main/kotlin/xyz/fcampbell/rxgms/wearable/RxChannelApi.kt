package xyz.fcampbell.rxgms.games

import android.content.Context
import com.google.android.gms.common.api.Status
import com.google.android.gms.wearable.ChannelApi
import com.google.android.gms.wearable.Wearable
import io.reactivex.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi
import xyz.fcampbell.rxgms.drive.RxChannel

/**
 * Created by francois on 2017-01-13.
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