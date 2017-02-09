package xyz.fcampbell.rxplayservices.auth.exception

/**
 * Thrown when there is an error when trying to sign-in to a Google Account
 *
 * @constructor
 * @param message A message to attach to this exception
 */
class SignInException(message: String) : Exception(message)