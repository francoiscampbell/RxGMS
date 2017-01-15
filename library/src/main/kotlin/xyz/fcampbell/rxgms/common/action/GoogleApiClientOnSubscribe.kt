package xyz.fcampbell.rxgms.common.action

import android.os.Bundle
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.GoogleApiClient
import rx.Observable
import rx.Subscriber
import rx.subscriptions.Subscriptions
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.exception.GoogleApiConnectionException
import xyz.fcampbell.rxgms.common.exception.GoogleApiConnectionSuspendedException
import xyz.fcampbell.rxgms.common.util.ResultActivity


internal class GoogleApiClientOnSubscribe<O : Api.ApiOptions>(
        private val apiClientDescriptor: ApiClientDescriptor,
        private vararg val apiDescriptors: ApiDescriptor<O>
) : Observable.OnSubscribe<Pair<GoogleApiClient, Bundle?>> {
    override fun call(subscriber: Subscriber<in Pair<GoogleApiClient, Bundle?>>) {
        val apiClient = createApiClient(subscriber)
        try {
            apiClient.connect()
        } catch (ex: Throwable) {
            subscriber.onError(ex)
        }

        subscriber.add(Subscriptions.create {
            if (apiClient.isConnected || apiClient.isConnecting) {
                apiClient.disconnect()
            }
        })
    }

    private fun createApiClient(subscriber: Subscriber<in Pair<GoogleApiClient, Bundle?>>): GoogleApiClient {
        val apiClientConnectionCallbacks = ApiClientConnectionCallbacks(subscriber)

        val apiClient = GoogleApiClient.Builder(
                apiClientDescriptor.context,
                apiClientConnectionCallbacks,
                apiClientConnectionCallbacks)
                .addApis(*apiDescriptors)
                .addScopes(*apiDescriptors)
                .addFromDescriptor(apiClientDescriptor)
                .build()

        apiClientConnectionCallbacks.apiClient = apiClient

        return apiClient
    }

    @Suppress("UNCHECKED_CAST")
    private fun GoogleApiClient.Builder.addApis(vararg descriptors: ApiDescriptor<O>): GoogleApiClient.Builder {
        descriptors.forEach {
            if (it.options == null) {
                addApi(it.api as Api<Api.ApiOptions.NotRequiredOptions>)
            } else {
                addApi(it.api as Api<Api.ApiOptions.HasOptions>, it.options as Api.ApiOptions.HasOptions)
            }
        }
        return this
    }

    private fun GoogleApiClient.Builder.addScopes(vararg descriptors: ApiDescriptor<O>): GoogleApiClient.Builder {
        descriptors.forEach { it.scopes.forEach { addScope(it) } }
        return this
    }

    private fun GoogleApiClient.Builder.addFromDescriptor(apiClientDescriptor: ApiClientDescriptor): GoogleApiClient.Builder {
        val handler = apiClientDescriptor.handler
        if (handler != null) setHandler(handler)

        val viewForPopups = apiClientDescriptor.viewForPopups
        if (viewForPopups != null) setViewForPopups(viewForPopups)

        val accountName = apiClientDescriptor.accountName
        if (accountName.isNotEmpty()) setAccountName(accountName)

        val gravityForPopups = apiClientDescriptor.gravityForPopups
        if (gravityForPopups != Int.MIN_VALUE) setGravityForPopups(gravityForPopups)

        return this
    }

    private inner class ApiClientConnectionCallbacks(
            private val subscriber: Subscriber<in Pair<GoogleApiClient, Bundle?>>
    ) : GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

        lateinit var apiClient: GoogleApiClient

        override fun onConnected(bundle: Bundle?) {
            try {
                subscriber.onNext(apiClient to bundle)
                //don't call onCompleted, we don't want the client to disconnect unless we explicitly unsubscribe
            } catch (ex: Throwable) {
                subscriber.onError(ex)
            }
        }

        override fun onConnectionSuspended(cause: Int) {
            subscriber.onError(GoogleApiConnectionSuspendedException(cause))
        }

        override fun onConnectionFailed(connectionResult: ConnectionResult) {
            if (connectionResult.hasResolution()) {
                ResultActivity.getResult(apiClientDescriptor.context, connectionResult.resolution!!.intentSender)
                        .subscribe({}, {}, { apiClient.connect() })
            } else {
                subscriber.onError(GoogleApiConnectionException(connectionResult, "Error connecting to GoogleApiClient"))
            }
        }
    }
}
