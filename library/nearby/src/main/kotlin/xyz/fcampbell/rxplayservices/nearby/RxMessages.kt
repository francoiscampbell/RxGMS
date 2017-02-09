package xyz.fcampbell.rxplayservices.nearby

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.google.android.gms.common.api.Status
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.*
import io.reactivex.Completable
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.RxPlayServicesApi

/**
 * Wraps [Nearby.Messages]
 */
@Suppress("unused")
class RxMessages(
        apiClientDescriptor: ApiClientDescriptor,
        messagesOptions: MessagesOptions
) : RxPlayServicesApi<Messages, MessagesOptions>(
        apiClientDescriptor,
        ApiDescriptor(Nearby.MESSAGES_API, Nearby.Messages, messagesOptions)
) {
    constructor(
            context: Context,
            messagesOptions: MessagesOptions
    ) : this(ApiClientDescriptor(context), messagesOptions)

    fun publish(message: Message): Observable<Status> {
        return fromPendingResult { publish(it, message) }
    }

    fun publish(message: Message, options: PublishOptions): Observable<Status> {
        return fromPendingResult { publish(it, message, options) }
    }

    fun unpublish(message: Message): Observable<Status> {
        return fromPendingResult { unpublish(it, message) }
    }

    fun subscribe(listener: MessageListener): Observable<Status> {
        return fromPendingResult { subscribe(it, listener) }
    }

    fun subscribe(listener: MessageListener, options: SubscribeOptions): Observable<Status> {
        return fromPendingResult { subscribe(it, listener, options) }
    }

    fun subscribe(pendingIntent: PendingIntent, options: SubscribeOptions): Observable<Status> {
        return fromPendingResult { subscribe(it, pendingIntent, options) }
    }

    fun subscribe(pendingIntent: PendingIntent): Observable<Status> {
        return fromPendingResult { subscribe(it, pendingIntent) }
    }

    fun unsubscribe(listener: MessageListener): Observable<Status> {
        return fromPendingResult { unsubscribe(it, listener) }
    }

    fun unsubscribe(pendingIntent: PendingIntent): Observable<Status> {
        return fromPendingResult { unsubscribe(it, pendingIntent) }
    }

    fun registerStatusCallback(statusCallback: StatusCallback): Observable<Status> {
        return fromPendingResult { registerStatusCallback(it, statusCallback) }
    }

    fun unregisterStatusCallback(statusCallback: StatusCallback): Observable<Status> {
        return fromPendingResult { unregisterStatusCallback(it, statusCallback) }
    }

    fun handleIntent(intent: Intent, messageListener: MessageListener): Completable {
        return Completable.fromAction { handleIntent(intent, messageListener) }
    }
}