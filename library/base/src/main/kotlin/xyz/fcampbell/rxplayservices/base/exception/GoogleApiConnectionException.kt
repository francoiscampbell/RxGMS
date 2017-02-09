package xyz.fcampbell.rxplayservices.base.exception

@Suppress("unused")
class GoogleApiConnectionException internal constructor(
        val connectionResult: ConnectionResult,
        detailMessage: String
) : Exception(detailMessage)
