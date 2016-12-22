package xyz.fcampbell.rxgms.onsubscribe.location.geocode

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLngBounds
import rx.Observable
import rx.Subscriber
import java.io.IOException

class GeocodeOnSubscribe private constructor(
        private val ctx: Context,
        private val locationName: String,
        private val maxResults: Int,
        private val bounds: LatLngBounds?
) : Observable.OnSubscribe<List<Address>> {

    override fun call(subscriber: Subscriber<in List<Address>>) {
        val geocoder = Geocoder(ctx)
        val result: List<Address>

        try {
            if (bounds != null) {
                result = geocoder.getFromLocationName(locationName, maxResults, bounds.southwest.latitude, bounds.southwest.longitude, bounds.northeast.latitude, bounds.northeast.longitude)
            } else {
                result = geocoder.getFromLocationName(locationName, maxResults)
            }

            if (!subscriber.isUnsubscribed) {
                subscriber.onNext(result)
                subscriber.onCompleted()
            }
        } catch (e: IOException) {
            if (!subscriber.isUnsubscribed) {
                subscriber.onError(e)
            }
        }

    }

    companion object {
        @JvmStatic
        fun createObservable(ctx: Context, locationName: String, maxResults: Int, bounds: LatLngBounds?): Observable<List<Address>> {
            return Observable.create(GeocodeOnSubscribe(ctx, locationName, maxResults, bounds))
        }
    }
}
