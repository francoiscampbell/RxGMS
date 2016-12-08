package xyz.fcampbell.rxgms.observables.geocode

import android.location.Address
import android.text.TextUtils
import org.json.JSONException
import org.json.JSONObject
import rx.Observable
import rx.Subscriber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

internal class FallbackReverseGeocodeOnSubscribe(
        private val locale: Locale,
        private val latitude: Double,
        private val longitude: Double,
        private val maxResults: Int
) : Observable.OnSubscribe<List<Address>> {

    override fun call(subscriber: Subscriber<in List<Address>>) {
        try {
            subscriber.onNext(alternativeReverseGeocodeQuery())
            subscriber.onCompleted()
        } catch (ex: Exception) {
            subscriber.onError(ex)
        }

    }

    /**
     * This function fetches a list of addresses for the set latitude, longitude and maxResults properties from the
     * Google Geocode API (http://maps.googleapis.com/maps/api/geocode).

     * @return List of addresses
     * *
     * @throws IOException   In case of network problems
     * *
     * @throws JSONException In case of problems while parsing the json response from google geocode API servers
     */
    @Throws(IOException::class, JSONException::class) //TODO use gson?
    private fun alternativeReverseGeocodeQuery(): List<Address> {
        val url = URL(String.format(Locale.ENGLISH,
                "http://maps.googleapis.com/maps/api/geocode/json?" + "latlng=%1\$f,%2\$f&sensor=true&language=%3\$s",
                latitude, longitude, locale.language
        ))
        val urlConnection = url.openConnection() as HttpURLConnection
        val stringBuilder = StringBuilder()
        val outResult = ArrayList<Address>()

        try {
            val reader = BufferedReader(InputStreamReader(urlConnection.inputStream, "UTF-8"))
            var line = reader.readLine()
            while (line != null) {
                stringBuilder.append(line)
                line = reader.readLine()
            }

            // Root json response object
            val jsonRootObject = JSONObject(stringBuilder.toString())

            // No results status
            if ("ZERO_RESULTS".equals(jsonRootObject.getString("status"), ignoreCase = true)) {
                return emptyList()
            }

            // Other non-OK responses status
            if (!"OK".equals(jsonRootObject.getString("status"), ignoreCase = true)) {
                throw RuntimeException("Wrong API response")
            }

            // Process results
            val results = jsonRootObject.getJSONArray("results")
            var i = 0
            while (i < results.length() && i < maxResults) {
                val address = Address(Locale.getDefault())
                var addressLineString = ""
                val sourceResult = results.getJSONObject(i)
                val addressComponents = sourceResult.getJSONArray("address_components")

                // Assemble address by various components
                for (ac in 0..addressComponents.length() - 1) {
                    val longNameVal = addressComponents.getJSONObject(ac).getString("long_name")
                    val shortNameVal = addressComponents.getJSONObject(ac).getString("short_name")
                    val acTypes = addressComponents.getJSONObject(ac).getJSONArray("types")
                    val acType = acTypes.getString(0)

                    if (!TextUtils.isEmpty(longNameVal)) {
                        if (acType.equals("street_number", ignoreCase = true)) {
                            if (TextUtils.isEmpty(addressLineString)) {
                                addressLineString = longNameVal
                            } else {
                                addressLineString += " " + longNameVal
                            }
                        } else if (acType.equals("route", ignoreCase = true)) {
                            if (TextUtils.isEmpty(addressLineString)) {
                                addressLineString = longNameVal
                            } else {
                                addressLineString = longNameVal + " " + addressLineString
                            }
                        } else if (acType.equals("sublocality", ignoreCase = true)) {
                            address.subLocality = longNameVal
                        } else if (acType.equals("locality", ignoreCase = true)) {
                            address.locality = longNameVal
                        } else if (acType.equals("administrative_area_level_2", ignoreCase = true)) {
                            address.subAdminArea = longNameVal
                        } else if (acType.equals("administrative_area_level_1", ignoreCase = true)) {
                            address.adminArea = longNameVal
                        } else if (acType.equals("country", ignoreCase = true)) {
                            address.countryName = longNameVal
                            address.countryCode = shortNameVal
                        } else if (acType.equals("postal_code", ignoreCase = true)) {
                            address.postalCode = longNameVal
                        }
                    }
                }

                // Try to get the already formatted address
                val formattedAddress = sourceResult.getString("formatted_address")
                if (!TextUtils.isEmpty(formattedAddress)) {
                    val formattedAddressLines = formattedAddress.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                    for (ia in formattedAddressLines.indices) {
                        address.setAddressLine(ia, formattedAddressLines[ia].trim { it <= ' ' })
                    }
                } else if (!TextUtils.isEmpty(addressLineString)) {
                    // If that fails use our manually assembled formatted address
                    address.setAddressLine(0, addressLineString)
                }

                // Finally add address to resulting set
                outResult.add(address)
                i++
            }

        } finally {
            urlConnection.disconnect()
        }

        return Collections.unmodifiableList(outResult)
    }
}
