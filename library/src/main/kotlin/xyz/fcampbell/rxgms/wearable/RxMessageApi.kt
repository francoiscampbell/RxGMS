package xyz.fcampbell.rxgms.games

import android.content.Context
import android.net.Uri
import com.google.android.gms.common.api.Status
import com.google.android.gms.wearable.MessageApi
import com.google.android.gms.wearable.Wearable
import io.reactivex.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi

/**
 * Created by francois on 2017-01-13.
 */
@Suppress("unused")
class RxMessageApi(
        apiClientDescriptor: ApiClientDescriptor,
        wearableOptions: Wearable.WearableOptions
) : RxGmsApi<MessageApi, Wearable.WearableOptions>(
        apiClientDescriptor,
        ApiDescriptor(Wearable.API, Wearable.MessageApi, wearableOptions)
) {
    constructor(
            context: Context,
            wearableOptions: Wearable.WearableOptions
    ) : this(ApiClientDescriptor(context), wearableOptions)

    fun sendMessage(nodeId: String, path: String, data: ByteArray): Observable<MessageApi.SendMessageResult> {
        return fromPendingResult { sendMessage(it, nodeId, path, data) }
    }

    fun addListener(listener: MessageApi.MessageListener): Observable<Status> {
        return fromPendingResult { addListener(it, listener) }
    }

    fun addListener(listener: MessageApi.MessageListener, uri: Uri, filterType: Int): Observable<Status> {
        return fromPendingResult { addListener(it, listener, uri, filterType) }
    }

    fun removeListener(listener: MessageApi.MessageListener): Observable<Status> {
        return fromPendingResult { removeListener(it, listener) }
    }
}