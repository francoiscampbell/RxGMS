package xyz.fcampbell.rxplayservices.games

import android.content.Context
import android.content.Intent
import com.google.android.gms.common.api.Scope
import com.google.android.gms.games.Games
import com.google.android.gms.games.video.Videos
import io.reactivex.Completable
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.common.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.common.ApiDescriptor
import xyz.fcampbell.rxplayservices.common.RxGmsApi

/**
 * Wraps [Games.Videos]
 */
@Suppress("unused")
class RxVideos(
        apiClientDescriptor: ApiClientDescriptor,
        gamesOptions: Games.GamesOptions,
        vararg scopes: Scope
) : RxGmsApi<Videos, Games.GamesOptions>(
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