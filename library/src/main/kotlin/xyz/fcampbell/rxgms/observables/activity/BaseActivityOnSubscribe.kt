package xyz.fcampbell.rxgms.observables.activity

import android.content.Context

import com.google.android.gms.location.ActivityRecognition

import xyz.fcampbell.rxgms.observables.BaseOnSubscribe

abstract class BaseActivityOnSubscribe<T> protected constructor(
        ctx: Context
) : BaseOnSubscribe<T>(ctx, ActivityRecognition.API)
