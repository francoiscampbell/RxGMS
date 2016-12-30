package xyz.fcampbell.rxgms.common.action

import android.content.Context
import android.os.Bundle
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.GoogleApiClient
import rx.Single
import rx.SingleSubscriber
import rx.subscriptions.Subscriptions
import xyz.fcampbell.rxgms.common.exception.GoogleApiConnectionException
import xyz.fcampbell.rxgms.common.exception.GoogleApiConnectionSuspendedException


internal open class GoogleApiClientOnSubscribe(
        private val context: Context,
        private vararg val services: Api<out Api.ApiOptions.NotRequiredOptions>
) : Single.OnSubscribe<GoogleApiClient> {

    override fun call(subscriber: SingleSubscriber<in GoogleApiClient>) {
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

    private fun createApiClient(subscriber: SingleSubscriber<in GoogleApiClient>): GoogleApiClient {
        val apiClientConnectionCallbacks = ApiClientConnectionCallbacks(subscriber)

        val apiClient = GoogleApiClient.Builder(
                context,
                apiClientConnectionCallbacks,
                apiClientConnectionCallbacks)
                .addApis(*services)
                .build()

        apiClientConnectionCallbacks.apiClient = apiClient

        return apiClient
    }

    private fun GoogleApiClient.Builder.addApis(vararg apis: Api<out Api.ApiOptions.NotRequiredOptions>): GoogleApiClient.Builder {
        for (api in apis) {
            addApi(api)
        }
        return this
    }

    private inner class ApiClientConnectionCallbacks(
            private val subscriber: SingleSubscriber<in GoogleApiClient>
    ) : GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

        lateinit var apiClient: GoogleApiClient

        override fun onConnected(bundle: Bundle?) {
            try {
                subscriber.onSuccess(apiClient)
            } catch (ex: Throwable) {
                subscriber.onError(ex)
            }

        }

        override fun onConnectionSuspended(cause: Int) {
            subscriber.onError(GoogleApiConnectionSuspendedException(cause))
        }

        override fun onConnectionFailed(connectionResult: ConnectionResult) {
            subscriber.onError(GoogleApiConnectionException(connectionResult, "Error connecting to GoogleApiClient."))
        }
    }
}
