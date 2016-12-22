package xyz.fcampbell.rxgms.common.exception

import com.google.android.gms.common.ConnectionResult

class GoogleApiConnectionException : Exception {
    val connectionResult: ConnectionResult

    internal constructor(connectionResult: ConnectionResult) : super() {
        this.connectionResult = connectionResult
    }

    internal constructor(connectionResult: ConnectionResult, detailMessage: String) : super(detailMessage) {
        this.connectionResult = connectionResult
    }

    internal constructor(connectionResult: ConnectionResult, cause: Throwable) : super(cause) {
        this.connectionResult = connectionResult
    }

    internal constructor(
            connectionResult: ConnectionResult,
            detailMessage: String,
            cause: Throwable
    ) : super(detailMessage, cause) {
        this.connectionResult = connectionResult
    }
}
