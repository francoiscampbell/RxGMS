package xyz.fcampbell.rxplayservices.locationservices.action.geocode

import android.content.Context
import android.graphics.RectF
import android.location.Address
import android.location.Geocoder
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import java.io.IOException

internal class Geocode(
        private val context: Context,
        private val locationName: String,
        private val maxResults: Int,
        private val bounds: RectF?
) : ObservableOnSubscribe<List<Address>> {

    override fun subscribe(emitter: ObservableEmitter<List<Address>>) {
        val geocoder = Geocoder(context)
        val result: List<Address>

        try {
            if (bounds != null) {
                result = geocoder.getFromLocationName(
                        locationName,
                        maxResults,
                        bounds.bottom.toDouble(),
                        bounds.left.toDouble(),
                        bounds.top.toDouble(),
                        bounds.right.toDouble())
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
