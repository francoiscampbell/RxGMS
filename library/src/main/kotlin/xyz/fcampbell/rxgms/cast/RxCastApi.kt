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
import xyz.fcampbell.rxgms.common.util.fromPendingResult
import xyz.fcampbell.rxgms.common.util.toCompletable
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
        return apiClientPair.toCompletable { castApi.requestStatus(it.first) }
    }

    fun sendMessage(namespace: String, message: String): Observable<Status> {
        return apiClientPair.fromPendingResult { castApi.sendMessage(it.first, namespace, message) }
    }

    fun launchApplication(applicationId: String): Observable<Cast.ApplicationConnectionResult> {
        return apiClientPair.fromPendingResult { castApi.launchApplication(it.first, applicationId) }
    }

    fun launchApplication(applicationId: String, launchOptions: LaunchOptions): Observable<Cast.ApplicationConnectionResult> {
        return apiClientPair.fromPendingResult { castApi.launchApplication(it.first, applicationId, launchOptions) }
    }

    fun joinApplication(applicationId: String, sessionId: String): Observable<Cast.ApplicationConnectionResult> {
        return apiClientPair.fromPendingResult { castApi.joinApplication(it.first, applicationId, sessionId) }
    }

    fun joinApplication(applicationId: String): Observable<Cast.ApplicationConnectionResult> {
        return apiClientPair.fromPendingResult { castApi.joinApplication(it.first, applicationId) }
    }

    fun joinApplication(): Observable<Cast.ApplicationConnectionResult> {
        return apiClientPair.fromPendingResult { castApi.joinApplication(it.first) }
    }

    fun leaveApplication(): Observable<Status> {
        return apiClientPair.fromPendingResult { castApi.stopApplication(it.first) }
    }

    fun stopApplication(): Observable<Status> {
        return apiClientPair.fromPendingResult { castApi.stopApplication(it.first) }
    }

    fun stopApplication(sessionId: String): Observable<Status> {
        return apiClientPair.fromPendingResult { castApi.stopApplication(it.first, sessionId) }
    }

    @Throws(IOException::class, IllegalArgumentException::class, IllegalStateException::class)
    fun setVolume(volume: Double): Completable {
        return apiClientPair.toCompletable { castApi.setVolume(it.first, volume) }
    }

    @Throws(IllegalStateException::class)
    fun getVolume(): Observable<Double> {
        return apiClientPair.map { castApi.getVolume(it.first) }
    }

    @Throws(IOException::class, IllegalStateException::class)
    fun setMute(mute: Boolean): Completable {
        return apiClientPair.toCompletable { castApi.setMute(it.first, mute) }
    }

    @Throws(IllegalStateException::class)
    fun isMute(): Observable<Boolean> {
        return apiClientPair.map { castApi.isMute(it.first) }
    }

    @Throws(IllegalStateException::class)
    fun getActiveInputState(): Observable<Int> {
        return apiClientPair.map { castApi.getActiveInputState(it.first) }
    }

    @Throws(IllegalStateException::class)
    fun getStandbyState(): Observable<Int> {
        return apiClientPair.map { castApi.getStandbyState(it.first) }
    }

    @Throws(IllegalStateException::class)
    fun getApplicationMetadata(): Observable<ApplicationMetadata> {
        return apiClientPair.map { castApi.getApplicationMetadata(it.first) }
    }

    @Throws(IllegalStateException::class)
    fun getApplicationStatus(): Observable<String> {
        return apiClientPair.map { castApi.getApplicationStatus(it.first) }
    }

    @Throws(IOException::class, IllegalStateException::class)
    fun setMessageReceivedCallbacks(namespace: String, callbacks: Cast.MessageReceivedCallback): Completable {
        return apiClientPair.toCompletable { castApi.setMessageReceivedCallbacks(it.first, namespace, callbacks) }
    }

    @Throws(IOException::class, IllegalArgumentException::class)
    fun removeMessageReceivedCallbacks(namespace: String): Completable {
        return apiClientPair.toCompletable { castApi.removeMessageReceivedCallbacks(it.first, namespace) }
    }
}