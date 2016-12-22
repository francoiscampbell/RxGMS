package xyz.fcampbell.rxgms.onsubscribe.activityrecognition

import android.content.Context

import com.google.android.gms.location.ActivityRecognition

import xyz.fcampbell.rxgms.onsubscribe.BaseOnSubscribe

abstract class BaseActivityRecognitionOnSubscribe<T> protected constructor(
        ctx: Context
) : BaseOnSubscribe<T>(ctx, ActivityRecognition.API)
