package xyz.fcampbell.rxplayservices.games

import android.content.Context
import com.google.android.gms.common.api.Scope
import com.google.android.gms.games.Games
import com.google.android.gms.games.stats.Stats
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.RxPlayServicesApi

/**
 * Wraps [Games.Stats]
 */
@Suppress("unused")
class RxStats(
        apiClientDescriptor: ApiClientDescriptor,
        gamesOptions: Games.GamesOptions,
        vararg scopes: Scope
) : RxPlayServicesApi<Stats, Games.GamesOptions>(
        apiClientDescriptor,
        ApiDescriptor(Games.API, Games.Stats, gamesOptions, *scopes)
) {
    constructor(
            context: Context,
            gamesOptions: Games.GamesOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), gamesOptions, *scopes)

    fun loadPlayerStats(forceReload: Boolean): Observable<Stats.LoadPlayerStatsResult> {
        return fromPendingResult { loadPlayerStats(it, forceReload) }
    }
}