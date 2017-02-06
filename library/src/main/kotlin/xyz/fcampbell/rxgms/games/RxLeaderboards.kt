package xyz.fcampbell.rxgms.games

import android.content.Context
import android.content.Intent
import com.google.android.gms.common.api.Scope
import com.google.android.gms.games.Games
import com.google.android.gms.games.leaderboard.LeaderboardScoreBuffer
import com.google.android.gms.games.leaderboard.Leaderboards
import io.reactivex.Completable
import io.reactivex.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi


/**
 * Wraps [Games.Leaderboards]
 */
@Suppress("unused")
class RxLeaderboards(
        apiClientDescriptor: ApiClientDescriptor,
        gamesOptions: Games.GamesOptions,
        vararg scopes: Scope
) : RxGmsApi<Leaderboards, Games.GamesOptions>(
        apiClientDescriptor,
        ApiDescriptor(Games.API, Games.Leaderboards, gamesOptions, *scopes)
) {
    constructor(
            context: Context,
            gamesOptions: Games.GamesOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), gamesOptions, *scopes)

    fun getAllLeaderboardsIntent(): Observable<Intent> {
        return map { getAllLeaderboardsIntent(it) }
    }

    fun getLeaderboardIntent(leaderboardId: String): Observable<Intent> {
        return map { getLeaderboardIntent(it, leaderboardId) }
    }

    fun getLeaderboardIntent(leaderboardId: String, timeSpan: Int): Observable<Intent> {
        return map { getLeaderboardIntent(it, leaderboardId, timeSpan) }
    }

    fun getLeaderboardIntent(leaderboardId: String, timeSpan: Int, collection: Int): Observable<Intent> {
        return map { getLeaderboardIntent(it, leaderboardId, timeSpan, collection) }
    }

    fun loadLeaderboardMetadata(forceReload: Boolean): Observable<Leaderboards.LeaderboardMetadataResult> {
        return fromPendingResult { loadLeaderboardMetadata(it, forceReload) }
    }

    fun loadLeaderboardMetadata(leaderboardId: String, forceReload: Boolean): Observable<Leaderboards.LeaderboardMetadataResult> {
        return fromPendingResult { loadLeaderboardMetadata(it, leaderboardId, forceReload) }
    }

    fun loadCurrentPlayerLeaderboardScore(leaderboardId: String, span: Int, leaderboardCollection: Int): Observable<Leaderboards.LoadPlayerScoreResult> {
        return fromPendingResult { loadCurrentPlayerLeaderboardScore(it, leaderboardId, span, leaderboardCollection) }
    }

    fun loadTopScores(leaderboardId: String, span: Int, leaderboardCollection: Int, maxResults: Int): Observable<Leaderboards.LoadScoresResult> {
        return fromPendingResult { loadTopScores(it, leaderboardId, span, leaderboardCollection, maxResults) }
    }

    fun loadTopScores(leaderboardId: String, span: Int, leaderboardCollection: Int, maxResults: Int, forceReload: Boolean): Observable<Leaderboards.LoadScoresResult> {
        return fromPendingResult { loadTopScores(it, leaderboardId, span, leaderboardCollection, maxResults, forceReload) }
    }

    fun loadPlayerCenteredScores(leaderboardId: String, span: Int, leaderboardCollection: Int, maxResults: Int): Observable<Leaderboards.LoadScoresResult> {
        return fromPendingResult { loadPlayerCenteredScores(it, leaderboardId, span, leaderboardCollection, maxResults) }
    }

    fun loadPlayerCenteredScores(leaderboardId: String, span: Int, leaderboardCollection: Int, maxResults: Int, forceReload: Boolean): Observable<Leaderboards.LoadScoresResult> {
        return fromPendingResult { loadPlayerCenteredScores(it, leaderboardId, span, leaderboardCollection, maxResults, forceReload) }
    }

    fun loadMoreScores(buffer: LeaderboardScoreBuffer, maxResults: Int, pageDirection: Int): Observable<Leaderboards.LoadScoresResult> {
        return fromPendingResult { loadMoreScores(it, buffer, maxResults, pageDirection) }
    }

    fun submitScore(leaderboardId: String, score: Long): Completable {
        return toCompletable { submitScore(it, leaderboardId, score) }
    }

    fun submitScore(leaderboardId: String, score: Long, scoreTag: String): Completable {
        return toCompletable { submitScore(it, leaderboardId, score, scoreTag) }
    }

    fun submitScoreImmediate(leaderboardId: String, score: Long): Observable<Leaderboards.SubmitScoreResult> {
        return fromPendingResult { submitScoreImmediate(it, leaderboardId, score) }
    }

    fun submitScoreImmediate(leaderboardId: String, score: Long, scoreTag: String): Observable<Leaderboards.SubmitScoreResult> {
        return fromPendingResult { submitScoreImmediate(it, leaderboardId, score, scoreTag) }
    }
}