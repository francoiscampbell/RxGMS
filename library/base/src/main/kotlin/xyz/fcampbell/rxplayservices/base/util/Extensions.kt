package xyz.fcampbell.rxplayservices.base.util

/**
 * Convenience extension function to convert a [PendingResult] to an [Observable].
 *
 * @param T The type parameter of the [PendingResult] and the resulting [Observable].
 * @receiver The [PendingResult] to transform.
 * @return An [Observable] that emits the [Result] of this [PendingResult] when it is ready.
 */
fun <T : Result> PendingResult<T>.toObservable(): Observable<T> {
    return Observable.create(PendingResultOnSubscribe { this })
}