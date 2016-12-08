package xyz.fcampbell.rxgms

class GoogleApiConnectionSuspendedException internal constructor(
        val errorCause: Int
) : RuntimeException()
