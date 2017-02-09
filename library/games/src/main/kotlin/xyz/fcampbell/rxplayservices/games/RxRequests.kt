package xyz.fcampbell.rxplayservices.games

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import com.google.android.gms.common.api.Scope
import com.google.android.gms.games.Games
import com.google.android.gms.games.request.GameRequest
import com.google.android.gms.games.request.OnRequestReceivedListener
import com.google.android.gms.games.request.Requests
import io.reactivex.Completable
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.RxPlayServicesApi
import java.util.*

/**
 * Wraps [Games.Requests]
 */
@Suppress("unused")
class RxRequests(
        apiClientDescriptor: ApiClientDescriptor,
        gamesOptions: Games.GamesOptions,
        vararg scopes: Scope
) : RxPlayServicesApi<Requests, Games.GamesOptions>(
        apiClientDescriptor,
        ApiDescriptor(Games.API, Games.Requests, gamesOptions, *scopes)
) {
    constructor(
            context: Context,
            gamesOptions: Games.GamesOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), gamesOptions, *scopes)

    fun getGameRequestsFromInboxResponse(response: Intent): Observable<ArrayList<GameRequest>> {
        return Observable.just(Games.Requests.getGameRequestsFromInboxResponse(response))
    }

    fun getGameRequestsFromBundle(extras: Bundle): Observable<ArrayList<GameRequest>> {
        return Observable.just(Games.Requests.getGameRequestsFromBundle(extras))
    }

    fun registerRequestListener(listener: OnRequestReceivedListener): Completable {
        return toCompletable { registerRequestListener(it, listener) }
    }

    fun unregisterRequestListener(): Completable {
        return toCompletable { unregisterRequestListener(it) }
    }

    fun getInboxIntent(): Observable<Intent> {
        return map { getInboxIntent(it) }
    }

    fun getSendIntent(type: Int, payload: ByteArray, requestLifetimeDays: Int, icon: Bitmap, description: String): Observable<Intent> {
        return map { getSendIntent(it, type, payload, requestLifetimeDays, icon, description) }
    }

    fun getMaxPayloadSize(): Observable<Int> {
        return map { getMaxPayloadSize(it) }
    }

    fun getMaxLifetimeDays(): Observable<Int> {
        return map { getMaxLifetimeDays(it) }
    }

    fun acceptRequest(requestId: String): Observable<Requests.UpdateRequestsResult> {
        return fromPendingResult { acceptRequest(it, requestId) }
    }

    fun acceptRequests(requestIds: List<String>): Observable<Requests.UpdateRequestsResult> {
        return fromPendingResult { acceptRequests(it, requestIds) }
    }

    fun dismissRequest(requestId: String): Observable<Requests.UpdateRequestsResult> {
        return fromPendingResult { dismissRequest(it, requestId) }
    }

    fun dismissRequests(requestIds: List<String>): Observable<Requests.UpdateRequestsResult> {
        return fromPendingResult { dismissRequests(it, requestIds) }
    }

    fun loadRequests(reverseDirection: Int, types: Int, sortOrder: Int): Observable<Requests.LoadRequestsResult> {
        return fromPendingResult { loadRequests(it, reverseDirection, types, sortOrder) }
    }
}