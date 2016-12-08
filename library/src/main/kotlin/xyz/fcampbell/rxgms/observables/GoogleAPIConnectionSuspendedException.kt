package xyz.fcampbell.rxgms.observables

class GoogleAPIConnectionSuspendedException internal constructor(val errorCause: Int) : RuntimeException()
