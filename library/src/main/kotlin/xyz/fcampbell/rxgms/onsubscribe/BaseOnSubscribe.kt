package xyz.fcampbell.rxgms.onsubscribe

import android.content.Context
import android.os.Bundle
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.GoogleApiClient
import rx.Observable
import rx.Observer
import rx.Subscriber
import rx.subscriptions.Subscriptions
import xyz.fcampbell.rxgms.GoogleApiConnectionException
import xyz.fcampbell.rxgms.GoogleApiConnectionSuspendedException
import java.util.*


abstract class BaseOnSubscribe<T> @SafeVarargs protected constructor(
        private val ctx: Context,
        vararg services: Api<out Api.ApiOptions.NotRequiredOptions>
) : Observable.OnSubscribe<T> {
    private val services: List<Api<out Api.ApiOptions.NotRequiredOptions>>

    init {
        this.services = Arrays.asList(*services)
    }

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

        val apiClientBuilder = GoogleApiClient.Builder(
                ctx,
                apiClientConnectionCallbacks,
                apiClientConnectionCallbacks)

        for (service in services) {
            apiClientBuilder.addApi(service)
        }

        val apiClient = apiClientBuilder.build()

        apiClientConnectionCallbacks.setClient(apiClient)

        return apiClient
    }

    protected open fun onUnsubscribe(apiClient: GoogleApiClient) {
    }

    protected abstract fun onGoogleApiClientReady(apiClient: GoogleApiClient, observer: Observer<in T>)

    private inner class ApiClientConnectionCallbacks(
            private val observer: Observer<in T>
    ) : GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

        private lateinit var apiClient: GoogleApiClient

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
            observer.onError(GoogleApiConnectionException("Error connecting to GoogleApiClient.", connectionResult))
        }

        fun setClient(client: GoogleApiClient) {
            this.apiClient = client
        }
    }

}
