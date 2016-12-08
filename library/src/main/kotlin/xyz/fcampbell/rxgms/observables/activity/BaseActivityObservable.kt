package xyz.fcampbell.rxgms.observables.activity

import android.content.Context

import com.google.android.gms.location.ActivityRecognition

import xyz.fcampbell.rxgms.observables.BaseObservable

abstract class BaseActivityObservable<T> protected constructor(
        ctx: Context
) : BaseObservable<T>(ctx, ActivityRecognition.API)
