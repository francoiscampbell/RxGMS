package xyz.fcampbell.rxplayservices.drive

import android.net.Uri
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.wearable.Channel
import com.google.android.gms.wearable.ChannelApi
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.common.RxWrappedApi

/**
 * Wraps [Channel]
 */
@Suppress("unused")
class RxChannel(
        override val apiClient: Observable<GoogleApiClient>,
        override val original: Channel
) : RxWrappedApi<Channel> {
    fun close(): Observable<Status> {
        return fromPendingResult { close(it) }
    }

    fun close(errorCode: Int): Observable<Status> {
        return fromPendingResult { close(it, errorCode) }
    }

    fun getInputStream(): Observable<Channel.GetInputStreamResult> {
        return fromPendingResult { getInputStream(it) }
    }

    fun getOutputStream(): Observable<Channel.GetOutputStreamResult> {
        return fromPendingResult { getOutputStream(it) }
    }

    fun receiveFile(uri: Uri, append: Boolean): Observable<Status> {
        return fromPendingResult { receiveFile(it, uri, append) }
    }

    fun sendFile(uri: Uri): Observable<Status> {
        return fromPendingResult { sendFile(it, uri) }
    }

    fun sendFile(uri: Uri, startOffset: Long, length: Long): Observable<Status> {
        return fromPendingResult { sendFile(it, uri, startOffset, length) }
    }

    fun addListener(listener: ChannelApi.ChannelListener): Observable<Status> {
        return fromPendingResult { addListener(it, listener) }
    }

    fun removeListener(listener: ChannelApi.ChannelListener): Observable<Status> {
        return fromPendingResult { removeListener(it, listener) }
    }
}