package xyz.fcampbell.rxplayservices.base.action

import android.os.Bundle
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.exception.GoogleApiConnectionException
import xyz.fcampbell.rxplayservices.base.exception.GoogleApiConnectionSuspendedException
import xyz.fcampbell.rxplayservices.base.util.ResultActivity

/**
 * Handles creating, connecting, and delivering the [GoogleApiClient] for a particulat Google Play services API
 *
 * @param A The type of the API to wrap  (If there is no API but you still want to extend this class, use [Unit] or [Void]).
 * @param O The type of that API's options.
 *
 * @constructor
 * @param apiClientDescriptor Describes the desired parameters of the [GoogleApiClient] that will back this API.
 * @param apiDescriptor Describes the Google Play services API to which to connect.
 */
internal class GoogleApiClientOnSubscribe<A, O : Api.ApiOptions>(
        private val apiClientDescriptor: ApiClientDescriptor,
        private val apiDescriptor: ApiDescriptor<A, O>
) : ObservableOnSubscribe<Pair<GoogleApiClient, Bundle?>> {
    override fun subscribe(emitter: ObservableEmitter<Pair<GoogleApiClient, Bundle?>>) {
        val apiClient = createApiClient(emitter)

        emitter.setCancellable {
            if (apiClient.isConnected || apiClient.isConnecting) {
                apiClient.disconnect()
            }
        }

        try {
            apiClient.connect()
        } catch (ex: Throwable) {
            emitter.onError(ex)
        }
    }

    private fun createApiClient(emitter: ObservableEmitter<in Pair<GoogleApiClient, Bundle?>>): GoogleApiClient {
        val apiClientConnectionCallbacks = ApiClientConnectionCallbacks(emitter)

        val apiClient = GoogleApiClient.Builder(
                apiClientDescriptor.context,
                apiClientConnectionCallbacks,
                apiClientConnectionCallbacks)
                .addApi(apiDescriptor.api, apiDescriptor.options)
                .addScopes(*apiDescriptor.scopes)
                .addFromDescriptor(apiClientDescriptor)
                .build()

        apiClientConnectionCallbacks.apiClient = apiClient

        return apiClient
    }

    @Suppress("UNCHECKED_CAST")
    private fun GoogleApiClient.Builder.addApi(api: Api<O>, options: O?): GoogleApiClient.Builder {
        if (options == null) {
            if (api in APIS_TO_ADD_ONLY_IF_AVAILABLE) {
                addApiIfAvailable(api as Api<Api.ApiOptions.NotRequiredOptions>)
            } else {
                addApi(api as Api<Api.ApiOptions.NotRequiredOptions>)
            }
        } else {
            if (api in APIS_TO_ADD_ONLY_IF_AVAILABLE) {
                addApiIfAvailable(api as Api<Api.ApiOptions.HasOptions>, options as Api.ApiOptions.HasOptions)
            } else {
                addApi(api as Api<Api.ApiOptions.HasOptions>, options as Api.ApiOptions.HasOptions)
            }
        }
        return this
    }

    private fun GoogleApiClient.Builder.addScopes(vararg scopes: Scope): GoogleApiClient.Builder {
        scopes.forEach { addScope(it) }
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

    companion object {
        private val APIS_TO_ADD_ONLY_IF_AVAILABLE = mutableSetOf<Api<out Api.ApiOptions>>()

        init {
            try { //add one per api that may not be there
                APIS_TO_ADD_ONLY_IF_AVAILABLE.add(com.google.android.gms.wearable.Wearable.API)
            } catch (e: NoClassDefFoundError) {
            }
        }
    }

    private inner class ApiClientConnectionCallbacks(
            private val emitter: ObservableEmitter<in Pair<GoogleApiClient, Bundle?>>
    ) : GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

        lateinit var apiClient: GoogleApiClient

        override fun onConnected(bundle: Bundle?) {
            try {
                emitter.onNext(apiClient to bundle)
                //don't call onCompleted, we don't want the client to disconnect unless we explicitly unsubscribe
            } catch (ex: Throwable) {
                emitter.onError(ex)
            }
        }

        override fun onConnectionSuspended(cause: Int) {
            emitter.onError(GoogleApiConnectionSuspendedException(cause))
        }

        override fun onConnectionFailed(connectionResult: ConnectionResult) {
            if (connectionResult.hasResolution()) {
                ResultActivity.getResult(apiClientDescriptor.context, connectionResult.resolution!!.intentSender)
                        .subscribe({}, {}, { apiClient.connect() })
            } else {
                emitter.onError(GoogleApiConnectionException(connectionResult, "Error connecting to GoogleApiClient"))
            }
        }
    }
}
