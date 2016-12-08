package xyz.fcampbell.rxgms.onsubscribe

class GoogleAPIConnectionSuspendedException internal constructor(
        val errorCause: Int
) : RuntimeException()
