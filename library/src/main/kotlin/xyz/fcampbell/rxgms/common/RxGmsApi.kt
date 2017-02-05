package xyz.fcampbell.rxgms.common

import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.GoogleApiClient
import io.reactivex.Observable
import io.reactivex.disposables.Disposables
import xyz.fcampbell.rxgms.common.action.GoogleApiClientOnSubscribe

/**
 * Created by francois on 2016-12-29.
 */
abstract class RxGmsApi<out A, O : Api.ApiOptions>(
        apiClientDescriptor: ApiClientDescriptor,
        api: ApiDescriptor<A, O>
) : RxWrappedApi<A> {
    companion object {
        private const val TAG = "RxGmsApi"
    }

    override val original = api.apiInterface

    private val googleApiClientOnSubscribe = GoogleApiClientOnSubscribe(apiClientDescriptor, api)

    private var currentDisposable = Disposables.disposed()
    private var currentApiClientPair: Observable<Pair<GoogleApiClient, Bundle?>>? = null

    private val apiClientPair: Observable<Pair<GoogleApiClient, Bundle?>>
        get() {
            var localRxApiClient = currentApiClientPair
            if (localRxApiClient != null && !currentDisposable.isDisposed) return localRxApiClient

            localRxApiClient = Observable.create(googleApiClientOnSubscribe)
                    .doOnSubscribe {
                        Log.d(TAG, "Sub to main apiClient")
                    }
                    .doOnDispose {
                        Log.d(TAG, "Dispose main apiClient")
                    }
                    .replay(1)
                    .autoConnect(1) { disposable ->
                        currentDisposable = disposable //to unsub from main client and disconnect from GMS
                    }
                    .firstElement() //to force a terminal event to subscribers after one GoogleApiClient emission
                    .toObservable()
                    .doOnSubscribe {
                        Log.d(TAG, "Sub to replayed apiClient")
                    }
                    .doOnDispose {
                        Log.d(TAG, "Dispose replayed apiClient")
                    }
            currentApiClientPair = localRxApiClient
            return localRxApiClient
        }

    override val apiClient: Observable<GoogleApiClient>
        get() = apiClientPair.map { it.first }

    val bundle: Observable<Bundle?>
        get() = apiClientPair.map { it.second }

    fun disconnect() {
        currentDisposable.dispose()
        currentApiClientPair = null
    }
}
