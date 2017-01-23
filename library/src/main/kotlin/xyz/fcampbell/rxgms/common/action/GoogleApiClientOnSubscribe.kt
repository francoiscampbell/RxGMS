package xyz.fcampbell.rxgms.common.action

import android.os.Bundle
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.exception.GoogleApiConnectionException
import xyz.fcampbell.rxgms.common.exception.GoogleApiConnectionSuspendedException
import xyz.fcampbell.rxgms.common.util.ResultActivity


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
            addApi(api as Api<Api.ApiOptions.NotRequiredOptions>)
        } else {
            addApi(api as Api<Api.ApiOptions.HasOptions>, options as Api.ApiOptions.HasOptions)
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
