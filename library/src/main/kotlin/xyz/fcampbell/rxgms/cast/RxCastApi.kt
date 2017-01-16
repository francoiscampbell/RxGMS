package xyz.fcampbell.rxgms.cast

import android.content.Context
import com.google.android.gms.cast.ApplicationMetadata
import com.google.android.gms.cast.Cast
import com.google.android.gms.cast.LaunchOptions
import com.google.android.gms.common.api.Scope
import com.google.android.gms.common.api.Status
import rx.Completable
import rx.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi
import java.io.IOException

@Suppress("unused")
class RxCastApi(
        apiClientDescriptor: ApiClientDescriptor,
        castOptions: Cast.CastOptions,
        vararg scopes: Scope
) : RxGmsApi<Cast.CastOptions>(
        apiClientDescriptor,
        ApiDescriptor(Cast.API, castOptions, *scopes)
) {
    constructor(
            context: Context,
            castOptions: Cast.CastOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), castOptions, *scopes)

    //        private val mediaRouter = MediaRouter.getInstance(apiClientDescriptor.context) TODO determine if this is necessary
    private val castApi: Cast.CastApi = Cast.CastApi //can't inline or else the compiler gets confused between Cast.CastApi (the class) and Cast.CastApi (the static field)

    @Throws(IOException::class, IllegalStateException::class)
    fun requestStatus(): Completable {
        return toCompletable { castApi.requestStatus(it) }
    }

    fun sendMessage(namespace: String, message: String): Observable<Status> {
        return fromPendingResult { castApi.sendMessage(it, namespace, message) }
    }

    fun launchApplication(applicationId: String): Observable<Cast.ApplicationConnectionResult> {
        return fromPendingResult { castApi.launchApplication(it, applicationId) }
    }

    fun launchApplication(applicationId: String, launchOptions: LaunchOptions): Observable<Cast.ApplicationConnectionResult> {
        return fromPendingResult { castApi.launchApplication(it, applicationId, launchOptions) }
    }

    fun joinApplication(applicationId: String, sessionId: String): Observable<Cast.ApplicationConnectionResult> {
        return fromPendingResult { castApi.joinApplication(it, applicationId, sessionId) }
    }

    fun joinApplication(applicationId: String): Observable<Cast.ApplicationConnectionResult> {
        return fromPendingResult { castApi.joinApplication(it, applicationId) }
    }

    fun joinApplication(): Observable<Cast.ApplicationConnectionResult> {
        return fromPendingResult { castApi.joinApplication(it) }
    }

    fun leaveApplication(): Observable<Status> {
        return fromPendingResult { castApi.stopApplication(it) }
    }

    fun stopApplication(): Observable<Status> {
        return fromPendingResult { castApi.stopApplication(it) }
    }

    fun stopApplication(sessionId: String): Observable<Status> {
        return fromPendingResult { castApi.stopApplication(it, sessionId) }
    }

    @Throws(IOException::class, IllegalArgumentException::class, IllegalStateException::class)
    fun setVolume(volume: Double): Completable {
        return toCompletable { castApi.setVolume(it, volume) }
    }

    @Throws(IllegalStateException::class)
    fun getVolume(): Observable<Double> {
        return map { castApi.getVolume(it) }
    }

    @Throws(IOException::class, IllegalStateException::class)
    fun setMute(mute: Boolean): Completable {
        return toCompletable { castApi.setMute(it, mute) }
    }

    @Throws(IllegalStateException::class)
    fun isMute(): Observable<Boolean> {
        return map { castApi.isMute(it) }
    }

    @Throws(IllegalStateException::class)
    fun getActiveInputState(): Observable<Int> {
        return map { castApi.getActiveInputState(it) }
    }

    @Throws(IllegalStateException::class)
    fun getStandbyState(): Observable<Int> {
        return map { castApi.getStandbyState(it) }
    }

    @Throws(IllegalStateException::class)
    fun getApplicationMetadata(): Observable<ApplicationMetadata> {
        return map { castApi.getApplicationMetadata(it) }
    }

    @Throws(IllegalStateException::class)
    fun getApplicationStatus(): Observable<String> {
        return map { castApi.getApplicationStatus(it) }
    }

    @Throws(IOException::class, IllegalStateException::class)
    fun setMessageReceivedCallbacks(namespace: String, callbacks: Cast.MessageReceivedCallback): Completable {
        return toCompletable { castApi.setMessageReceivedCallbacks(it, namespace, callbacks) }
    }

    @Throws(IOException::class, IllegalArgumentException::class)
    fun removeMessageReceivedCallbacks(namespace: String): Completable {
        return toCompletable { castApi.removeMessageReceivedCallbacks(it, namespace) }
    }
}