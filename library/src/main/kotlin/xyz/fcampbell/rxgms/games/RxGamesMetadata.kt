package xyz.fcampbell.rxgms.games

import android.content.Context
import com.google.android.gms.common.api.Scope
import com.google.android.gms.games.Game
import com.google.android.gms.games.Games
import com.google.android.gms.games.GamesMetadata
import io.reactivex.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi

/**
 * Created by francois on 2017-01-13.
 */
@Suppress("unused")
class RxGamesMetadata(
        apiClientDescriptor: ApiClientDescriptor,
        gamesOptions: Games.GamesOptions,
        vararg scopes: Scope
) : RxGmsApi<GamesMetadata, Games.GamesOptions>(
        apiClientDescriptor,
        ApiDescriptor(Games.API, Games.GamesMetadata, gamesOptions, *scopes)
) {
    constructor(
            context: Context,
            gamesOptions: Games.GamesOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), gamesOptions, *scopes)

    fun getCurrentGame(): Observable<Game> {
        return map { getCurrentGame(it) }
    }

    fun loadGame(): Observable<GamesMetadata.LoadGamesResult> {
        return fromPendingResult { loadGame(it) }
    }
}