package xyz.fcampbell.rxgms.sample.utils

import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.DetectedActivity
import io.reactivex.functions.Function

object ToMostProbableActivity : Function<ActivityRecognitionResult, DetectedActivity> {
    override fun apply(activityRecognitionResult: ActivityRecognitionResult): DetectedActivity {
        return activityRecognitionResult.mostProbableActivity
    }
}
