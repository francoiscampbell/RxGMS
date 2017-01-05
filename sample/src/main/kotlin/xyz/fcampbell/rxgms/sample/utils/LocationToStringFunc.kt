package xyz.fcampbell.rxgms.sample.utils

import android.location.Location

import rx.functions.Func1

object LocationToStringFunc : Func1<Location, String> {
    override fun call(location: Location?): String {
        return if (location != null) "${location.latitude}-${location.longitude} (${location.accuracy})" else "no location available"
    }
}
