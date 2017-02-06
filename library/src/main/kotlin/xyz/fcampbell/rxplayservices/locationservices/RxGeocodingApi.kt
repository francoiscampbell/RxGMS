package xyz.fcampbell.rxplayservices.locationservices

import android.content.Context
import android.location.Address
import com.google.android.gms.common.api.Api
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLngBounds
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import xyz.fcampbell.rxplayservices.common.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.common.ApiDescriptor
import xyz.fcampbell.rxplayservices.common.RxGmsApi
import xyz.fcampbell.rxplayservices.locationservices.action.geocode.Geocode
import xyz.fcampbell.rxplayservices.locationservices.action.geocode.ReverseGeocode
import java.util.*

/**
 * Wraps [Geocoder]
 */
@Suppress("unused")
class RxGeocodingApi(
        private val apiClientDescriptor: ApiClientDescriptor
) : RxGmsApi<Unit, Api.ApiOptions.NoOptions>(
        apiClientDescriptor,
        ApiDescriptor(LocationServices.API, Unit)
) {
    constructor(
            context: Context
    ) : this(ApiClientDescriptor(context))

    /**
     * Creates observable that translates latitude and longitude to list of possible addresses using
     * included Geocoder class. In case geocoder fails with IOException("Service not Available") fallback
     * decoder is used using google web service. You should subscribe for this observable on I/O thread.
     * The stream finishes after address list is available.

     * @param lat        latitude
     * *
     * @param lng        longitude
     * *
     * @param maxResults maximal number of results you are interested in
     * *
     * @return observable that serves list of address based on location
     */
    fun reverseGeocode(lat: Double, lng: Double, maxResults: Int): Observable<List<Address>> {
        return reverseGeocode(Locale.getDefault(), lat, lng, maxResults)
    }

    /**
     * Creates observable that translates latitude and longitude to list of possible addresses using
     * included Geocoder class. In case geocoder fails with IOException("Service not Available") fallback
     * decoder is used using google web service. You should subscribe for this observable on I/O thread.
     * The stream finishes after address list is available.

     * @param locale     locale for address language
     * *
     * @param lat        latitude
     * *
     * @param lng        longitude
     * *
     * @param maxResults maximal number of results you are interested in
     * *
     * @return observable that serves list of address based on location
     */
    fun reverseGeocode(locale: Locale, lat: Double, lng: Double, maxResults: Int): Observable<List<Address>> {
        return create { ReverseGeocode(apiClientDescriptor.context, locale, lat, lng, maxResults) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.trampoline())
    }

    /**
     * Creates observable that translates a street address or other description into a list of
     * possible addresses using included Geocoder class. You should subscribe for this
     * observable on I/O thread.
     * The stream finishes after address list is available.
     *
     * You may specify a bounding box for the search results.

     * @param locationName a user-supplied description of a location
     * *
     * @param maxResults   max number of results you are interested in
     * *
     * @param bounds       restricts the results to geographical bounds. May be null
     * *
     * @return observable that serves list of address based on location name
     */
    @JvmOverloads fun geocode(locationName: String, maxResults: Int, bounds: LatLngBounds? = null): Observable<List<Address>> {
        return create { Geocode(apiClientDescriptor.context, locationName, maxResults, bounds) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.trampoline())
    }
}