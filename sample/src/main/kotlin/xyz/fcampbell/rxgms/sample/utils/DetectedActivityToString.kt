package xyz.fcampbell.rxgms.sample.utils

import com.google.android.gms.location.DetectedActivity

import rx.functions.Func1

class DetectedActivityToString : Func1<DetectedActivity, String> {
    override fun call(detectedActivity: DetectedActivity): String {
        return getNameFromType(detectedActivity.type) + " with confidence " + detectedActivity.confidence
    }

    private fun getNameFromType(activityType: Int): String {
        when (activityType) {
            DetectedActivity.RUNNING -> return "running"
            DetectedActivity.IN_VEHICLE -> return "in_vehicle"
            DetectedActivity.ON_BICYCLE -> return "on_bicycle"
            DetectedActivity.ON_FOOT -> return "on_foot"
            DetectedActivity.STILL -> return "still"
            DetectedActivity.UNKNOWN -> return "unknown"
            DetectedActivity.TILTING -> return "tilting"
        }
        return "unknown"
    }
}
