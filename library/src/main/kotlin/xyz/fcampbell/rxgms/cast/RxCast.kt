package xyz.fcampbell.rxgms.cast

import android.support.v7.media.MediaRouter
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

/**
 * Created by francois on 2017-01-13.
 */
@Suppress("unused")
class RxCast private constructor() {
    class CastApi(
            apiClientDescriptor: ApiClientDescriptor,
            castOptions: Cast.CastOptions,
            vararg scopes: Scope
    ) : RxGmsApi<Cast.CastOptions>(
            apiClientDescriptor,
            ApiDescriptor(Cast.API, castOptions, *scopes)
    ) {
        private val mediaRouter = MediaRouter.getInstance(apiClientDescriptor.context)
        private val castApi: Cast.CastApi = Cast.CastApi //can't inline or else the compiler gets confused between Cast.CastApi (the class) and Cast.CastApi (the static field)

        @Throws(IOException::class, IllegalStateException::class)
        fun requestStatus(): Completable {
            return apiClient.map { castApi.requestStatus(it) }.toCompletable()
        }

        fun sendMessage(namespace: String, message: String): Observable<Status> {
            return apiClient.pendingResultToObservable { castApi.sendMessage(it, namespace, message) }
        }

        fun launchApplication(applicationId: String): Observable<Cast.ApplicationConnectionResult> {
            return apiClient.pendingResultToObservable { castApi.launchApplication(it, applicationId) }
        }

        fun launchApplication(applicationId: String, launchOptions: LaunchOptions): Observable<Cast.ApplicationConnectionResult> {
            return apiClient.pendingResultToObservable { castApi.launchApplication(it, applicationId, launchOptions) }
        }

        fun joinApplication(applicationId: String, sessionId: String): Observable<Cast.ApplicationConnectionResult> {
            return apiClient.pendingResultToObservable { castApi.joinApplication(it, applicationId, sessionId) }
        }

        fun joinApplication(applicationId: String): Observable<Cast.ApplicationConnectionResult> {
            return apiClient.pendingResultToObservable { castApi.joinApplication(it, applicationId) }
        }

        fun joinApplication(): Observable<Cast.ApplicationConnectionResult> {
            return apiClient.pendingResultToObservable { castApi.joinApplication(it) }
        }

        fun leaveApplication(): Observable<Status> {
            return apiClient.pendingResultToObservable { castApi.stopApplication(it) }
        }

        fun stopApplication(): Observable<Status> {
            return apiClient.pendingResultToObservable { castApi.stopApplication(it) }
        }

        fun stopApplication(sessionId: String): Observable<Status> {
            return apiClient.pendingResultToObservable { castApi.stopApplication(it, sessionId) }
        }

        @Throws(IOException::class, IllegalArgumentException::class, IllegalStateException::class)
        fun setVolume(volume: Double): Completable {
            return apiClient.map { castApi.setVolume(it, volume) }.toCompletable()
        }

        @Throws(IllegalStateException::class)
        fun getVolume(): Observable<Double> {
            return apiClient.map { castApi.getVolume(it) }
        }

        @Throws(IOException::class, IllegalStateException::class)
        fun setMute(mute: Boolean): Completable {
            return apiClient.map { castApi.setMute(it, mute) }.toCompletable()
        }

        @Throws(IllegalStateException::class)
        fun isMute(): Observable<Boolean> {
            return apiClient.map { castApi.isMute(it) }
        }

        @Throws(IllegalStateException::class)
        fun getActiveInputState(): Observable<Int> {
            return apiClient.map { castApi.getActiveInputState(it) }
        }

        @Throws(IllegalStateException::class)
        fun getStandbyState(): Observable<Int> {
            return apiClient.map { castApi.getStandbyState(it) }
        }

        @Throws(IllegalStateException::class)
        fun getApplicationMetadata(): Observable<ApplicationMetadata> {
            return apiClient.map { castApi.getApplicationMetadata(it) }
        }

        @Throws(IllegalStateException::class)
        fun getApplicationStatus(): Observable<String> {
            return apiClient.map { castApi.getApplicationStatus(it) }
        }

        @Throws(IOException::class, IllegalStateException::class)
        fun setMessageReceivedCallbacks(namespace: String, callbacks: Cast.MessageReceivedCallback): Completable {
            return apiClient.map { castApi.setMessageReceivedCallbacks(it, namespace, callbacks) }.toCompletable()
        }

        @Throws(IOException::class, IllegalArgumentException::class)
        fun removeMessageReceivedCallbacks(namespace: String): Completable {
            return apiClient.map { castApi.removeMessageReceivedCallbacks(it, namespace) }.toCompletable()
        }
    }
}