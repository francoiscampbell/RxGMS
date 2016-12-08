package xyz.fcampbell.rxgms

import com.google.android.gms.common.ConnectionResult

class GoogleApiConnectionException internal constructor(
        detailMessage: String,
        val connectionResult: ConnectionResult
) : RuntimeException(detailMessage)
