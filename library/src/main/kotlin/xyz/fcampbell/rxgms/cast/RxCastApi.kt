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
import xyz.fcampbell.rxgms.common.util.pendingResultToObservable
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
        return apiClient.map { castApi.requestStatus(it.first) }.toCompletable()
    }

    fun sendMessage(namespace: String, message: String): Observable<Status> {
        return apiClient.pendingResultToObservable { castApi.sendMessage(it.first, namespace, message) }
    }

    fun launchApplication(applicationId: String): Observable<Cast.ApplicationConnectionResult> {
        return apiClient.pendingResultToObservable { castApi.launchApplication(it.first, applicationId) }
    }

    fun launchApplication(applicationId: String, launchOptions: LaunchOptions): Observable<Cast.ApplicationConnectionResult> {
        return apiClient.pendingResultToObservable { castApi.launchApplication(it.first, applicationId, launchOptions) }
    }

    fun joinApplication(applicationId: String, sessionId: String): Observable<Cast.ApplicationConnectionResult> {
        return apiClient.pendingResultToObservable { castApi.joinApplication(it.first, applicationId, sessionId) }
    }

    fun joinApplication(applicationId: String): Observable<Cast.ApplicationConnectionResult> {
        return apiClient.pendingResultToObservable { castApi.joinApplication(it.first, applicationId) }
    }

    fun joinApplication(): Observable<Cast.ApplicationConnectionResult> {
        return apiClient.pendingResultToObservable { castApi.joinApplication(it.first) }
    }

    fun leaveApplication(): Observable<Status> {
        return apiClient.pendingResultToObservable { castApi.stopApplication(it.first) }
    }

    fun stopApplication(): Observable<Status> {
        return apiClient.pendingResultToObservable { castApi.stopApplication(it.first) }
    }

    fun stopApplication(sessionId: String): Observable<Status> {
        return apiClient.pendingResultToObservable { castApi.stopApplication(it.first, sessionId) }
    }

    @Throws(IOException::class, IllegalArgumentException::class, IllegalStateException::class)
    fun setVolume(volume: Double): Completable {
        return apiClient.map { castApi.setVolume(it.first, volume) }.toCompletable()
    }

    @Throws(IllegalStateException::class)
    fun getVolume(): Observable<Double> {
        return apiClient.map { castApi.getVolume(it.first) }
    }

    @Throws(IOException::class, IllegalStateException::class)
    fun setMute(mute: Boolean): Completable {
        return apiClient.map { castApi.setMute(it.first, mute) }.toCompletable()
    }

    @Throws(IllegalStateException::class)
    fun isMute(): Observable<Boolean> {
        return apiClient.map { castApi.isMute(it.first) }
    }

    @Throws(IllegalStateException::class)
    fun getActiveInputState(): Observable<Int> {
        return apiClient.map { castApi.getActiveInputState(it.first) }
    }

    @Throws(IllegalStateException::class)
    fun getStandbyState(): Observable<Int> {
        return apiClient.map { castApi.getStandbyState(it.first) }
    }

    @Throws(IllegalStateException::class)
    fun getApplicationMetadata(): Observable<ApplicationMetadata> {
        return apiClient.map { castApi.getApplicationMetadata(it.first) }
    }

    @Throws(IllegalStateException::class)
    fun getApplicationStatus(): Observable<String> {
        return apiClient.map { castApi.getApplicationStatus(it.first) }
    }

    @Throws(IOException::class, IllegalStateException::class)
    fun setMessageReceivedCallbacks(namespace: String, callbacks: Cast.MessageReceivedCallback): Completable {
        return apiClient.map { castApi.setMessageReceivedCallbacks(it.first, namespace, callbacks) }.toCompletable()
    }

    @Throws(IOException::class, IllegalArgumentException::class)
    fun removeMessageReceivedCallbacks(namespace: String): Completable {
        return apiClient.map { castApi.removeMessageReceivedCallbacks(it.first, namespace) }.toCompletable()
    }
}