package xyz.fcampbell.rxplayservices.base.exception

@Suppress("unused")
class GoogleApiConnectionSuspendedException internal constructor(val errorCause: Int) : RuntimeException()
