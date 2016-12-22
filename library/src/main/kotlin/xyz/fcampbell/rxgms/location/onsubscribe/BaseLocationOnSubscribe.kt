package xyz.fcampbell.rxgms.location.onsubscribe

import android.content.Context

import com.google.android.gms.location.LocationServices
import xyz.fcampbell.rxgms.common.onsubscribe.BaseOnSubscribe

abstract internal class BaseLocationOnSubscribe<T> protected constructor(
        ctx: Context
) : BaseOnSubscribe<T>(ctx, LocationServices.API)
