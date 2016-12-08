package xyz.fcampbell.rxgms

import com.google.android.gms.common.data.AbstractDataBuffer
import rx.Observable
import rx.subscriptions.Subscriptions

/**
 * Util class that creates observable from buffer.
 */
object DataBufferObservable {

    /**
     * Creates observable from buffer. On unsubscribe buffer is automatically released.

     * @param buffer source buffer
     * *
     * @param <T>    item type
     * *
     * @return observable that emits all items from buffer and on unsubscription releases it
    </T> */
    @JvmStatic
    fun <T> from(buffer: AbstractDataBuffer<T>): Observable<T> {
        return Observable.create { subscriber ->
            Observable.from(buffer).subscribe(subscriber)
            subscriber.add(Subscriptions.create { buffer.release() })
        }
    }
}//no instance
