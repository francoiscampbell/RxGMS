package xyz.fcampbell.rxgms.common.onsubscribe

import android.content.Context
import android.os.Bundle
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.GoogleApiClient
import rx.Observable
import rx.Observer
import rx.Subscriber
import rx.subscriptions.Subscriptions
import xyz.fcampbell.rxgms.common.exception.GoogleApiConnectionException
import xyz.fcampbell.rxgms.common.exception.GoogleApiConnectionSuspendedException


abstract internal class BaseOnSubscribe<T> @SafeVarargs protected constructor(
        private val ctx: Context,
        private vararg val services: Api<out Api.ApiOptions.NotRequiredOptions>
) : Observable.OnSubscribe<T> {

    override fun call(subscriber: Subscriber<in T>) {
        val apiClient = createApiClient(subscriber)
        try {
            apiClient.connect()
        } catch (ex: Throwable) {
            subscriber.onError(ex)
        }

        subscriber.add(Subscriptions.create {
            if (apiClient.isConnected || apiClient.isConnecting) {
                onUnsubscribe(apiClient)
                apiClient.disconnect()
            }
        })
    }


    protected fun createApiClient(subscriber: Subscriber<in T>): GoogleApiClient {
        val apiClientConnectionCallbacks = ApiClientConnectionCallbacks(subscriber)

        val apiClient = GoogleApiClient.Builder(
                ctx,
                apiClientConnectionCallbacks,
                apiClientConnectionCallbacks)
                .addApis(*services)
                .build()

        apiClientConnectionCallbacks.apiClient = apiClient

        return apiClient
    }

    protected open fun onUnsubscribe(apiClient: GoogleApiClient) {
    }

    protected abstract fun onGoogleApiClientReady(apiClient: GoogleApiClient, observer: Observer<in T>)

    private fun GoogleApiClient.Builder.addApis(vararg apis: Api<out Api.ApiOptions.NotRequiredOptions>): GoogleApiClient.Builder {
        for (api in apis) {
            addApi(api)
        }
        return this
    }

    private inner class ApiClientConnectionCallbacks(
            private val observer: Observer<in T>
    ) : GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

        lateinit var apiClient: GoogleApiClient

        override fun onConnected(bundle: Bundle?) {
            try {
                onGoogleApiClientReady(apiClient, observer)
            } catch (ex: Throwable) {
                observer.onError(ex)
            }

        }

        override fun onConnectionSuspended(cause: Int) {
            observer.onError(GoogleApiConnectionSuspendedException(cause))
        }

        override fun onConnectionFailed(connectionResult: ConnectionResult) {
            observer.onError(GoogleApiConnectionException(connectionResult, "Error connecting to GoogleApiClient."))
        }
    }
}
