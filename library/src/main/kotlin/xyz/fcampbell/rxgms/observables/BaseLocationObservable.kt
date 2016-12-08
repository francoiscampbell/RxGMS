package xyz.fcampbell.rxgms.observables

import android.content.Context

import com.google.android.gms.location.LocationServices

abstract class BaseLocationObservable<T> protected constructor(ctx: Context) : BaseObservable<T>(ctx, LocationServices.API)
