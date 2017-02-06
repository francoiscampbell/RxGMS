package xyz.fcampbell.rxplayservices.common.exception

class GoogleApiConnectionSuspendedException : RuntimeException {
    val errorCause: Int

    internal constructor(errorCause: Int) : super() {
        this.errorCause = errorCause
    }

    internal constructor(errorCause: Int, detailMessage: String) : super(detailMessage) {
        this.errorCause = errorCause
    }

    internal constructor(errorCause: Int, cause: Throwable) : super(cause) {
        this.errorCause = errorCause
    }

    internal constructor(errorCause: Int, detailMessage: String, cause: Throwable) : super(detailMessage, cause) {
        this.errorCause = errorCause
    }
}
