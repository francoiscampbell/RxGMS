package xyz.fcampbell.rxgms.observables

import com.google.android.gms.common.ConnectionResult

class GoogleAPIConnectionException internal constructor(detailMessage: String, val connectionResult: ConnectionResult) : RuntimeException(detailMessage)
