package xyz.fcampbell.rxgms.locationservices.action.location

import android.location.Location
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationServices
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.Disposable
import xyz.fcampbell.rxgms.common.exception.StatusException

internal class MockLocation(
        private val apiClient: GoogleApiClient,
        private val locationObservable: Observable<Location>
) : ObservableOnSubscribe<Status> {
    private var mockLocationDisposable: Disposable? = null

    override fun subscribe(emitter: ObservableEmitter<Status>) {
        emitter.setCancellable {
            if (apiClient.isConnected) {
                try {
                    LocationServices.FusedLocationApi.setMockMode(apiClient, false)
                } catch (e: SecurityException) {
                    // if this happens then we couldn't have switched mock mode on in the first place,
                    // and the observer's onError will already have been called
                }

            }
            if (mockLocationDisposable != null && !mockLocationDisposable!!.isDisposed) {
                mockLocationDisposable!!.dispose()
            }
        }

        // this throws SecurityException if permissions are bad or mock locations are not enabled,
        // which is passed to observer's onError by BaseObservable
        LocationServices.FusedLocationApi.setMockMode(apiClient, true)
                .setResultCallback { status ->
                    if (status.isSuccess) {
                        startLocationMocking(apiClient, emitter)
                    } else {
                        emitter.onError(StatusException(status))
                    }
                }
    }

    private fun startLocationMocking(apiClient: GoogleApiClient, emitter: ObservableEmitter<in Status>) {
        mockLocationDisposable = locationObservable.subscribe({ location ->
            LocationServices.FusedLocationApi.setMockLocation(apiClient, location)
                    .setResultCallback { status ->
                        if (status.isSuccess) {
                            emitter.onNext(status)
                        } else {
                            emitter.onError(StatusException(status))
                        }
                    }
        }, {
            emitter.onError(it)
        }, {
            emitter.onComplete()
        })
    }
}
