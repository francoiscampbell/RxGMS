package xyz.fcampbell.rxgms.onsubscribe.location

import android.content.Context

import com.google.android.gms.location.LocationServices
import xyz.fcampbell.rxgms.onsubscribe.BaseOnSubscribe

abstract class BaseLocationOnSubscribe<T> protected constructor(
        ctx: Context
) : BaseOnSubscribe<T>(ctx, LocationServices.API)
