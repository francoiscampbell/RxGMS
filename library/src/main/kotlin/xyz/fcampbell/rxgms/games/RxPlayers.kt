package xyz.fcampbell.rxgms.games

import android.content.Context
import android.content.Intent
import com.google.android.gms.common.api.Scope
import com.google.android.gms.games.Games
import com.google.android.gms.games.Player
import com.google.android.gms.games.Players
import io.reactivex.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi

/**
 * Wraps [Games.Players]
 */
@Suppress("unused")
class RxPlayers(
        apiClientDescriptor: ApiClientDescriptor,
        gamesOptions: Games.GamesOptions,
        vararg scopes: Scope
) : RxGmsApi<Players, Games.GamesOptions>(
        apiClientDescriptor,
        ApiDescriptor(Games.API, Games.Players, gamesOptions, *scopes)
) {
    constructor(
            context: Context,
            gamesOptions: Games.GamesOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), gamesOptions, *scopes)

    fun getCurrentPlayer(): Observable<Player> {
        return map { getCurrentPlayer(it) }
    }

    fun loadPlayer(playerId: String): Observable<Players.LoadPlayersResult> {
        return fromPendingResult { loadPlayer(it, playerId) }
    }

    fun loadPlayer(playerId: String, forceReload: Boolean): Observable<Players.LoadPlayersResult> {
        return fromPendingResult { loadPlayer(it, playerId, forceReload) }
    }

    fun loadInvitablePlayers(pageSize: Int, forceReload: Boolean): Observable<Players.LoadPlayersResult> {
        return fromPendingResult { loadInvitablePlayers(it, pageSize, forceReload) }
    }

    fun loadMoreInvitablePlayers(pageSize: Int): Observable<Players.LoadPlayersResult> {
        return fromPendingResult { loadMoreInvitablePlayers(it, pageSize) }
    }

    fun loadRecentlyPlayedWithPlayers(pageSize: Int, forceReload: Boolean): Observable<Players.LoadPlayersResult> {
        return fromPendingResult { loadRecentlyPlayedWithPlayers(it, pageSize, forceReload) }
    }

    fun loadMoreRecentlyPlayedWithPlayers(pageSize: Int): Observable<Players.LoadPlayersResult> {
        return fromPendingResult { loadMoreRecentlyPlayedWithPlayers(it, pageSize) }
    }

    fun loadConnectedPlayers(forceReload: Boolean): Observable<Players.LoadPlayersResult> {
        return fromPendingResult { loadConnectedPlayers(it, forceReload) }
    }

    fun getCompareProfileIntent(player: Player): Observable<Intent> {
        return map { getCompareProfileIntent(it, player) }
    }

    fun getPlayerSearchIntent(): Observable<Intent> {
        return map { getPlayerSearchIntent(it) }
    }
}