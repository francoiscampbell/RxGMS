package xyz.fcampbell.rxplayservices.base.exception

import com.google.android.gms.common.ConnectionResult

@Suppress("unused")
class GoogleApiConnectionException internal constructor(
        val connectionResult: ConnectionResult,
        detailMessage: String
) : Exception(detailMessage)
