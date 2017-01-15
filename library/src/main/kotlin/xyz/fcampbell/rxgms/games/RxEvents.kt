package xyz.fcampbell.rxgms.games

import android.content.Context
import com.google.android.gms.common.api.Scope
import com.google.android.gms.games.Games
import com.google.android.gms.games.event.Events
import rx.Completable
import rx.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi
import xyz.fcampbell.rxgms.common.util.pendingResultToObservable
import xyz.fcampbell.rxgms.common.util.toCompletable

/**
 * Created by francois on 2017-01-13.
 */
@Suppress("unused")
class RxEvents(
        apiClientDescriptor: ApiClientDescriptor,
        gamesOptions: Games.GamesOptions,
        vararg scopes: Scope
) : RxGmsApi<Games.GamesOptions>(
        apiClientDescriptor,
        ApiDescriptor(Games.API, gamesOptions, *scopes)
) {
    constructor(
            context: Context,
            gamesOptions: Games.GamesOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), gamesOptions, *scopes)

    fun load(forceReload: Boolean): Observable<Events.LoadEventsResult> {
        return apiClient.pendingResultToObservable { Games.Events.load(it.first, forceReload) }
    }

    fun loadByIds(forceReload: Boolean, vararg eventIds: String): Observable<Events.LoadEventsResult> {
        return apiClient.pendingResultToObservable { Games.Events.loadByIds(it.first, forceReload, *eventIds) }
    }

    fun increment(eventId: String, incrementAmount: Int): Completable {
        return apiClient.toCompletable { Games.Events.increment(it.first, eventId, incrementAmount) }
    }
}