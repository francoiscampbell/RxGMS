package xyz.fcampbell.rxgms.auth.exception

/**
 * Created by francois on 2017-01-11.
 */
class SignInException : Exception {
    constructor() : super()

    constructor(message: String) : super(message)

    constructor(ex: Throwable) : super(ex)

    constructor(message: String, ex: Throwable) : super(message, ex)
}