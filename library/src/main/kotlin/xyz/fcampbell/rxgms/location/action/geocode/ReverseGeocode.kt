package xyz.fcampbell.rxgms.location.action.geocode

import android.content.Context
import android.location.Address
import android.location.Geocoder
import rx.AsyncEmitter
import rx.Observable
import rx.schedulers.Schedulers
import xyz.fcampbell.rxgms.common.action.FromEmitter
import java.io.IOException
import java.util.*

internal class ReverseGeocode(
        private val ctx: Context,
        private val locale: Locale,
        private val latitude: Double,
        private val longitude: Double,
        private val maxResults: Int
) : FromEmitter<List<Address>>() {

    override fun call(emitter: AsyncEmitter<List<Address>>) {
        super.call(emitter)

        val geocoder = Geocoder(ctx, locale)
        try {
            emitter.onNext(geocoder.getFromLocation(latitude, longitude, maxResults))
            emitter.onCompleted()
        } catch (e: IOException) {
            // If it's a service not available error try a different approach using google web service
            if (e.message.equals("Service not Available", ignoreCase = true)) {
                Observable
                        .fromEmitter(
                                FallbackReverseGeocodeFromEmitter(locale, latitude, longitude, maxResults),
                                AsyncEmitter.BackpressureMode.BUFFER)
                        .subscribeOn(Schedulers.io())
                        .subscribe(emitter)
            } else {
                emitter.onError(e)
            }
        }

    }
}
