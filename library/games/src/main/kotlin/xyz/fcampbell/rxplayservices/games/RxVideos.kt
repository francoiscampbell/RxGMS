package xyz.fcampbell.rxplayservices.games

import android.content.Context
import android.content.Intent
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.RxPlayServicesApi

/**
 * Wraps [Games.Videos]
 */
@Suppress("unused")
class RxVideos(
        apiClientDescriptor: ApiClientDescriptor,
        gamesOptions: Games.GamesOptions,
        vararg scopes: Scope
) : RxPlayServicesApi<Videos, Games.GamesOptions>(
        apiClientDescriptor,
        ApiDescriptor(Games.API, Games.Videos, gamesOptions, *scopes)
) {
    constructor(
            context: Context,
            gamesOptions: Games.GamesOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), gamesOptions, *scopes)

    fun getCaptureCapabilities(): Observable<Videos.CaptureCapabilitiesResult> {
        return fromPendingResult { getCaptureCapabilities(it) }
    }

    fun getCaptureOverlayIntent(): Observable<Intent> {
        return map { getCaptureOverlayIntent(it) }
    }

    fun getCaptureState(): Observable<Videos.CaptureStateResult> {
        return fromPendingResult { getCaptureState(it) }
    }

    fun isCaptureAvailable(captureMode: Int): Observable<Videos.CaptureAvailableResult> {
        return fromPendingResult { isCaptureAvailable(it, captureMode) }
    }

    fun isCaptureSupported(): Observable<Boolean> {
        return map { isCaptureSupported(it) }
    }

    fun registerCaptureOverlayStateChangedListener(listener: Videos.CaptureOverlayStateListener): Completable {
        return toCompletable { registerCaptureOverlayStateChangedListener(it, listener) }
    }

    fun unregisterCaptureOverlayStateChangedListener(): Completable {
        return toCompletable { unregisterCaptureOverlayStateChangedListener(it) }
    }
}