package xyz.fcampbell.rxplayservices.common.exception

import com.google.android.gms.common.api.Status

class StatusException : Exception {
    val status: Status

    constructor(status: Status) : super() {
        this.status = status
    }

    constructor(status: Status, message: String) : super(message) {
        this.status = status
    }

    constructor(status: Status, ex: Throwable) : super(ex) {
        this.status = status
    }

    constructor(status: Status, message: String, ex: Throwable) : super(message, ex) {
        this.status = status
    }
}
