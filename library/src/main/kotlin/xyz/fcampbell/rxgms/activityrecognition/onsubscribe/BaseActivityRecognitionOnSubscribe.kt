package xyz.fcampbell.rxgms.activityrecognition.onsubscribe

import android.content.Context

import com.google.android.gms.location.ActivityRecognition

import xyz.fcampbell.rxgms.common.onsubscribe.BaseOnSubscribe

abstract class BaseActivityRecognitionOnSubscribe<T> protected constructor(
        ctx: Context
) : BaseOnSubscribe<T>(ctx, ActivityRecognition.API)
