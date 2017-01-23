package xyz.fcampbell.rxgms.location.action.geocode

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLngBounds
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import java.io.IOException

internal class Geocode(
        private val context: Context,
        private val locationName: String,
        private val maxResults: Int,
        private val bounds: LatLngBounds?
) : ObservableOnSubscribe<List<Address>> {

    override fun subscribe(emitter: ObservableEmitter<List<Address>>) {
        val geocoder = Geocoder(context)
        val result: List<Address>

        try {
            if (bounds != null) {
                result = geocoder.getFromLocationName(locationName, maxResults, bounds.southwest.latitude, bounds.southwest.longitude, bounds.northeast.latitude, bounds.northeast.longitude)
            } else {
                result = geocoder.getFromLocationName(locationName, maxResults)
            }

            emitter.onNext(result)
            emitter.onComplete()
        } catch (e: IOException) {
            emitter.onError(e)
        }
    }
}
