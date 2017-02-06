package xyz.fcampbell.rxgms.cast

import android.content.Context
import com.google.android.gms.cast.ApplicationMetadata
import com.google.android.gms.cast.Cast
import com.google.android.gms.cast.LaunchOptions
import com.google.android.gms.common.api.Scope
import com.google.android.gms.common.api.Status
import io.reactivex.Completable
import io.reactivex.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi
import java.io.IOException

/**
 * Wraps [Cast.CastApi]
 */
@Suppress("unused")
class RxCastApi(
        apiClientDescriptor: ApiClientDescriptor,
        castOptions: Cast.CastOptions,
        vararg scopes: Scope
) : RxGmsApi<Cast.CastApi, Cast.CastOptions>(
        apiClientDescriptor,
        ApiDescriptor(Cast.API, Cast.CastApi, castOptions, *scopes)
) {
    constructor(
            context: Context,
            castOptions: Cast.CastOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), castOptions, *scopes)

    //        private val mediaRouter = MediaRouter.getInstance(apiClientDescriptor.context) TODO determine if this is necessary

    @Throws(IOException::class, IllegalStateException::class)
    fun requestStatus(): Completable {
        return toCompletable { requestStatus(it) }
    }

    fun sendMessage(namespace: String, message: String): Observable<Status> {
        return fromPendingResult { sendMessage(it, namespace, message) }
    }

    fun launchApplication(applicationId: String): Observable<Cast.ApplicationConnectionResult> {
        return fromPendingResult { launchApplication(it, applicationId) }
    }

    fun launchApplication(applicationId: String, launchOptions: LaunchOptions): Observable<Cast.ApplicationConnectionResult> {
        return fromPendingResult { launchApplication(it, applicationId, launchOptions) }
    }

    fun joinApplication(applicationId: String, sessionId: String): Observable<Cast.ApplicationConnectionResult> {
        return fromPendingResult { joinApplication(it, applicationId, sessionId) }
    }

    fun joinApplication(applicationId: String): Observable<Cast.ApplicationConnectionResult> {
        return fromPendingResult { joinApplication(it, applicationId) }
    }

    fun joinApplication(): Observable<Cast.ApplicationConnectionResult> {
        return fromPendingResult { joinApplication(it) }
    }

    fun leaveApplication(): Observable<Status> {
        return fromPendingResult { stopApplication(it) }
    }

    fun stopApplication(): Observable<Status> {
        return fromPendingResult { stopApplication(it) }
    }

    fun stopApplication(sessionId: String): Observable<Status> {
        return fromPendingResult { stopApplication(it, sessionId) }
    }

    @Throws(IOException::class, IllegalArgumentException::class, IllegalStateException::class)
    fun setVolume(volume: Double): Completable {
        return toCompletable { setVolume(it, volume) }
    }

    @Throws(IllegalStateException::class)
    fun getVolume(): Observable<Double> {
        return map { getVolume(it) }
    }

    @Throws(IOException::class, IllegalStateException::class)
    fun setMute(mute: Boolean): Completable {
        return toCompletable { setMute(it, mute) }
    }

    @Throws(IllegalStateException::class)
    fun isMute(): Observable<Boolean> {
        return map { isMute(it) }
    }

    @Throws(IllegalStateException::class)
    fun getActiveInputState(): Observable<Int> {
        return map { getActiveInputState(it) }
    }

    @Throws(IllegalStateException::class)
    fun getStandbyState(): Observable<Int> {
        return map { getStandbyState(it) }
    }

    @Throws(IllegalStateException::class)
    fun getApplicationMetadata(): Observable<ApplicationMetadata> {
        return map { getApplicationMetadata(it) }
    }

    @Throws(IllegalStateException::class)
    fun getApplicationStatus(): Observable<String> {
        return map { getApplicationStatus(it) }
    }

    @Throws(IOException::class, IllegalStateException::class)
    fun setMessageReceivedCallbacks(namespace: String, callbacks: Cast.MessageReceivedCallback): Completable {
        return toCompletable { setMessageReceivedCallbacks(it, namespace, callbacks) }
    }

    @Throws(IOException::class, IllegalArgumentException::class)
    fun removeMessageReceivedCallbacks(namespace: String): Completable {
        return toCompletable { removeMessageReceivedCallbacks(it, namespace) }
    }
}