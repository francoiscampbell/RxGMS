package xyz.fcampbell.rxplayservices.nearby

import android.content.Context
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.RxPlayServicesApi

/**
 * Wraps [Nearby.Connections]
 */
@Suppress("unused")
class RxConnections(
        apiClientDescriptor: ApiClientDescriptor
) : RxPlayServicesApi<Connections, Api.ApiOptions.NoOptions>(
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