package xyz.fcampbell.rxgms.drive

import android.net.Uri
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.wearable.Channel
import com.google.android.gms.wearable.ChannelApi
import io.reactivex.Observable
import xyz.fcampbell.rxgms.common.util.toObservable

/**
 * Created by francois on 2017-01-10.
 */
@Suppress("unused")
class RxChannel(
        private val googleApiClient: GoogleApiClient,
        val channel: Channel
) {
    fun close(): Observable<Status> {
        return channel.close(googleApiClient).toObservable()
    }

    fun close(errorCode: Int): Observable<Status> {
        return channel.close(googleApiClient, errorCode).toObservable()
    }

    fun getInputStream(): Observable<Channel.GetInputStreamResult> {
        return channel.getInputStream(googleApiClient).toObservable()
    }

    fun getOutputStream(): Observable<Channel.GetOutputStreamResult> {
        return channel.getOutputStream(googleApiClient).toObservable()
    }

    fun receiveFile(uri: Uri, append: Boolean): Observable<Status> {
        return channel.receiveFile(googleApiClient, uri, append).toObservable()
    }

    fun sendFile(uri: Uri): Observable<Status> {
        return channel.sendFile(googleApiClient, uri).toObservable()
    }

    fun sendFile(uri: Uri, startOffset: Long, length: Long): Observable<Status> {
        return channel.sendFile(googleApiClient, uri, startOffset, length).toObservable()
    }

    fun addListener(listener: ChannelApi.ChannelListener): Observable<Status> {
        return channel.addListener(googleApiClient, listener).toObservable()
    }

    fun removeListener(listener: ChannelApi.ChannelListener): Observable<Status> {
        return channel.removeListener(googleApiClient, listener).toObservable()
    }
}