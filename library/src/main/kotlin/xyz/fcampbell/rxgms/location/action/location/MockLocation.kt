package xyz.fcampbell.rxgms.location.action.location

import android.location.Location
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationServices
import rx.AsyncEmitter
import rx.Observable
import rx.Observer
import rx.Subscription
import xyz.fcampbell.rxgms.common.action.FromEmitter
import xyz.fcampbell.rxgms.common.exception.StatusException

internal class MockLocation(
        private val apiClient: GoogleApiClient,
        private val locationObservable: Observable<Location>
) : FromEmitter<Status>() {
    private var mockLocationSubscription: Subscription? = null

    override fun call(emitter: AsyncEmitter<Status>) {
        super.call(emitter)

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

    private fun startLocationMocking(apiClient: GoogleApiClient, observer: Observer<in Status>) {
        mockLocationSubscription = locationObservable.subscribe({ location ->
            LocationServices.FusedLocationApi.setMockLocation(apiClient, location)
                    .setResultCallback { status ->
                        if (status.isSuccess) {
                            observer.onNext(status)
                        } else {
                            observer.onError(StatusException(status))
                        }
                    }
        }, { throwable -> observer.onError(throwable) }) { observer.onCompleted() }
    }

    override fun onUnsubscribe() {
        if (apiClient.isConnected) {
            try {
                LocationServices.FusedLocationApi.setMockMode(apiClient, false)
            } catch (e: SecurityException) {
                // if this happens then we couldn't have switched mock mode on in the first place,
                // and the observer's onError will already have been called
            }

        }
        if (mockLocationSubscription != null && !mockLocationSubscription!!.isUnsubscribed) {
            mockLocationSubscription!!.unsubscribe()
        }
    }
}
