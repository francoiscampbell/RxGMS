package xyz.fcampbell.rxgms.sample.utils

import android.location.Location

import rx.functions.Func1

class LocationToStringFunc : Func1<Location, String> {
    override fun call(location: Location?): String {
        if (location != null)
            return location.latitude.toString() + " " + location.longitude + " (" + location.accuracy + ")"
        return "no location available"
    }
}
