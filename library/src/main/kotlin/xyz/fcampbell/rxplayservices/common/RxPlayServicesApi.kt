package xyz.fcampbell.rxplayservices.common

import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.GoogleApiClient
import io.reactivex.Observable
import io.reactivex.disposables.Disposables
import xyz.fcampbell.rxplayservices.common.action.GoogleApiClientOnSubscribe

/**
 * Inherit from this class to wrap a particular Google Play services API.
 *
 * @param A The type of the API to wrap  (If there is no API but you still want to inherit from this class, use [Unit] or [Void]).
 * @param O The type of that API's options.
 *
 * @constructor
 * @param apiClientDescriptor Describes the desired parameters of the [GoogleApiClient] that will back this API.
 * @param api Describes the Google Play services API to which to connect.
 */
abstract class RxPlayServicesApi<out A, O : Api.ApiOptions>(
        apiClientDescriptor: ApiClientDescriptor,
        api: ApiDescriptor<A, O>
) : RxWrappedApi<A> {
    companion object {
        private const val TAG = "RxPlayServicesApi"
    }

    private val googleApiClientOnSubscribe = GoogleApiClientOnSubscribe(apiClientDescriptor, api)

    private var apiClientDisposable = Disposables.disposed()
    private var apiClientPair = newClientObservable()

    override val original = api.apiInterface

    /**
     * An [Observable] that emits a single [GoogleApiClient] upon its connection to Google Play services
     */
    override val apiClient: Observable<GoogleApiClient>
        get() = apiClientPair.map { it.first }

    /**
     * The [Bundle] that was passed to [onConnected(Bundle)] when the [GoogleApiClient] was connected.
     */
    val bundle: Observable<Bundle?>
        get() = apiClientPair.map { it.second }

    /**
     * Disconnects the underlying [GoogleApiClient]. Further use of this API will result in re-connection.
     */
    fun disconnect() {
        apiClientDisposable.dispose() //disconnect the GoogleApiClient
        apiClientPair = newClientObservable() //create a new Observable to start fresh
    }

    private fun newClientObservable(): Observable<Pair<GoogleApiClient, Bundle?>> {
        return Observable.create(googleApiClientOnSubscribe)
                .doOnSubscribe {
                    Log.d(TAG, "Sub to main apiClient")
                }
                .doOnDispose {
                    Log.d(TAG, "Dispose main apiClient")
                }
                .replay(1)
                .autoConnect(1) { disposable ->
                    apiClientDisposable = disposable //to unsub from main client and disconnect from GMS
                }
                .take(1) //to force a terminal event to subscribers after one GoogleApiClient emission
                .doOnSubscribe { disposable ->
                    Log.d(TAG, "Sub to replayed apiClient")
                }
                .doOnDispose {
                    Log.d(TAG, "Dispose replayed apiClient")
                }
    }
}
