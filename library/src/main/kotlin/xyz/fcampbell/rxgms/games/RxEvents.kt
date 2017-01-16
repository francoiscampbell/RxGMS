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
        return fromPendingResult { Games.Events.load(it, forceReload) }
    }

    fun loadByIds(forceReload: Boolean, vararg eventIds: String): Observable<Events.LoadEventsResult> {
        return fromPendingResult { Games.Events.loadByIds(it, forceReload, *eventIds) }
    }

    fun increment(eventId: String, incrementAmount: Int): Completable {
        return toCompletable { Games.Events.increment(it, eventId, incrementAmount) }
    }
}