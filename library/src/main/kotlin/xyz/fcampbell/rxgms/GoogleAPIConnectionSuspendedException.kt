package xyz.fcampbell.rxgms

class GoogleAPIConnectionSuspendedException internal constructor(
        val errorCause: Int
) : RuntimeException()
