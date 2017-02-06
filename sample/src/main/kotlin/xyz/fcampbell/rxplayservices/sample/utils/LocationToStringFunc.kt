package xyz.fcampbell.rxplayservices.sample.utils

import android.location.Location
import io.reactivex.functions.Function

object LocationToStringFunc : Function<Location, String> {
    override fun apply(location: Location?): String {
        return if (location != null) "${location.latitude}-${location.longitude} (${location.accuracy})" else "no location available"
    }
}
