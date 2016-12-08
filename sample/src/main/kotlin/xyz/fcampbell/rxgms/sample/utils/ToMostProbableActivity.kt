package xyz.fcampbell.rxgms.sample.utils

import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.DetectedActivity

import rx.functions.Func1

class ToMostProbableActivity : Func1<ActivityRecognitionResult, DetectedActivity> {
    override fun call(activityRecognitionResult: ActivityRecognitionResult): DetectedActivity {
        return activityRecognitionResult.mostProbableActivity
    }
}
