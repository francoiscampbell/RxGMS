package xyz.fcampbell.rxgms.games

import android.content.Context
import com.google.android.gms.common.api.Scope
import com.google.android.gms.games.Game
import com.google.android.gms.games.Games
import com.google.android.gms.games.GamesMetadata
import rx.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi
import xyz.fcampbell.rxgms.common.util.pendingResultToObservable

/**
 * Created by francois on 2017-01-13.
 */
@Suppress("unused")
class RxGamesMetadata(
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

    fun getCurrentGame(): Observable<Game> {
        return apiClient.map { Games.GamesMetadata.getCurrentGame(it.first) }
    }

    fun loadGame(): Observable<GamesMetadata.LoadGamesResult> {
        return apiClient.pendingResultToObservable { Games.GamesMetadata.loadGame(it.first) }
    }
}