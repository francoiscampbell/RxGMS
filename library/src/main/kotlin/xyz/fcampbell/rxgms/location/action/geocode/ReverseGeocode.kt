package xyz.fcampbell.rxgms.location.action.geocode

import android.content.Context
import android.location.Address
import android.location.Geocoder
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.util.*

internal class ReverseGeocode(
        private val ctx: Context,
        private val locale: Locale,
        private val latitude: Double,
        private val longitude: Double,
        private val maxResults: Int
) : ObservableOnSubscribe<List<Address>> {

    override fun subscribe(emitter: ObservableEmitter<List<Address>>) {
        val geocoder = Geocoder(ctx, locale)
        try {
            emitter.onNext(geocoder.getFromLocation(latitude, longitude, maxResults))
            emitter.onComplete()
        } catch (e: IOException) {
            // If it's a service not available error try a different approach using google web service
            if (e.message.equals("Service not Available", ignoreCase = true)) {
                Observable.create(FallbackReverseGeocodeFromEmitter(locale, latitude, longitude, maxResults))
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            emitter.onNext(it)
                        }, {
                            emitter.onError(it)
                        }, {
                            emitter.onComplete()
                        })
            } else {
                emitter.onError(e)
            }
        }

    }
}
