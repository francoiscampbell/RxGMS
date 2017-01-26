package xyz.fcampbell.rxgms.games

import android.content.Context
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.Status
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.AppMetadata
import com.google.android.gms.nearby.connection.Connections
import io.reactivex.Completable
import io.reactivex.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi

/**
 * Created by francois on 2017-01-13.
 */
@Suppress("unused")
class RxMessages(
        apiClientDescriptor: ApiClientDescriptor
) : RxGmsApi<Connections, Api.ApiOptions.NoOptions>(
        apiClientDescriptor,
        ApiDescriptor(Nearby.CONNECTIONS_API, Nearby.Connections)
) {
    constructor(
            context: Context
    ) : this(ApiClientDescriptor(context))

    fun startAdvertising(name: String, appMetadata: AppMetadata, durationMillis: Long, connectionRequestListener: Connections.ConnectionRequestListener): Observable<Connections.StartAdvertisingResult> {
        return fromPendingResult { startAdvertising(it, name, appMetadata, durationMillis, connectionRequestListener) }
    }

    fun stopAdvertising(): Completable {
        return toCompletable { stopAdvertising(it) }
    }

    fun startDiscovery(serviceId: String, durationMillis: Long, listener: Connections.EndpointDiscoveryListener): Observable<Status> {
        return fromPendingResult { startDiscovery(it, serviceId, durationMillis, listener) }
    }

    fun stopDiscovery(serviceId: String): Completable {
        return toCompletable { stopDiscovery(it, serviceId) }
    }

    fun sendConnectionRequest(name: String, remoteEndpointId: String, payload: ByteArray, connectionResponseCallback: Connections.ConnectionResponseCallback, messageListener: Connections.MessageListener): Observable<Status> {
        return fromPendingResult { sendConnectionRequest(it, name, remoteEndpointId, payload, connectionResponseCallback, messageListener) }
    }

    fun acceptConnectionRequest(remoteEndpointId: String, payload: ByteArray, messageListener: Connections.MessageListener): Observable<Status> {
        return fromPendingResult { acceptConnectionRequest(it, remoteEndpointId, payload, messageListener) }
    }

    fun rejectConnectionRequest(remoteEndpointId: String): Observable<Status> {
        return fromPendingResult { rejectConnectionRequest(it, remoteEndpointId) }
    }

    fun sendReliableMessage(remoteEndpointId: String, payload: ByteArray): Completable {
        return toCompletable { sendReliableMessage(it, remoteEndpointId, payload) }
    }

    fun sendReliableMessage(remoteEndpointIds: List<String>, payload: ByteArray): Completable {
        return toCompletable { sendReliableMessage(it, remoteEndpointIds, payload) }
    }

    fun sendUnreliableMessage(remoteEndpointId: String, payload: ByteArray): Completable {
        return toCompletable { sendUnreliableMessage(it, remoteEndpointId, payload) }
    }

    fun sendUnreliableMessage(remoteEndpointIds: List<String>, payload: ByteArray): Completable {
        return toCompletable { sendUnreliableMessage(it, remoteEndpointIds, payload) }
    }

    fun disconnectFromEndpoint(remoteEndpointId: String): Completable {
        return toCompletable { disconnectFromEndpoint(it, remoteEndpointId) }
    }

    fun stopAllEndpoints(): Completable {
        return toCompletable { stopAllEndpoints(it) }
    }
}