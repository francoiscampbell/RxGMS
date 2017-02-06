package xyz.fcampbell.rxplayservices.games

import android.content.Context
import android.content.Intent
import com.google.android.gms.common.api.Scope
import com.google.android.gms.games.Games
import com.google.android.gms.games.multiplayer.ParticipantResult
import com.google.android.gms.games.multiplayer.turnbased.OnTurnBasedMatchUpdateReceivedListener
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer
import io.reactivex.Completable
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.common.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.common.ApiDescriptor
import xyz.fcampbell.rxplayservices.common.RxGmsApi

/**
 * Wraps [Games.TurnBasedMultiplayer]
 */
@Suppress("unused")
class RxTurnBasedMultiplayer(
        apiClientDescriptor: ApiClientDescriptor,
        gamesOptions: Games.GamesOptions,
        vararg scopes: Scope
) : RxGmsApi<TurnBasedMultiplayer, Games.GamesOptions>(
        apiClientDescriptor,
        ApiDescriptor(Games.API, Games.TurnBasedMultiplayer, gamesOptions, *scopes)
) {
    constructor(
            context: Context,
            gamesOptions: Games.GamesOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), gamesOptions, *scopes)

    fun getInboxIntent(): Observable<Intent> {
        return map { getInboxIntent(it) }
    }

    fun registerMatchUpdateListener(listener: OnTurnBasedMatchUpdateReceivedListener): Completable {
        return toCompletable { registerMatchUpdateListener(it, listener) }
    }

    fun unregisterMatchUpdateListener(): Completable {
        return toCompletable { unregisterMatchUpdateListener(it) }
    }

    fun getSelectOpponentsIntent(minPlayers: Int, maxPlayers: Int): Observable<Intent> {
        return map { getSelectOpponentsIntent(it, minPlayers, maxPlayers) }
    }

    fun getSelectOpponentsIntent(minPlayers: Int, maxPlayers: Int, allowAutomatch: Boolean): Observable<Intent> {
        return map { getSelectOpponentsIntent(it, minPlayers, maxPlayers, allowAutomatch) }
    }

    fun createMatch(config: TurnBasedMatchConfig): Observable<TurnBasedMultiplayer.InitiateMatchResult> {
        return fromPendingResult { createMatch(it, config) }
    }

    fun rematch(matchId: String): Observable<TurnBasedMultiplayer.InitiateMatchResult> {
        return fromPendingResult { rematch(it, matchId) }
    }

    fun acceptInvitation(invitationId: String): Observable<TurnBasedMultiplayer.InitiateMatchResult> {
        return fromPendingResult { acceptInvitation(it, invitationId) }
    }

    fun declineInvitation(invitationId: String): Completable {
        return toCompletable { declineInvitation(it, invitationId) }
    }

    fun dismissInvitation(invitationId: String): Completable {
        return toCompletable { dismissInvitation(it, invitationId) }
    }

    fun getMaxMatchDataSize(): Observable<Int> {
        return map { getMaxMatchDataSize(it) }
    }

    fun takeTurn(matchId: String, matchData: ByteArray, pendingParticipantId: String): Observable<TurnBasedMultiplayer.UpdateMatchResult> {
        return fromPendingResult { takeTurn(it, matchId, matchData, pendingParticipantId) }
    }

    fun takeTurn(matchId: String, matchData: ByteArray, pendingParticipantId: String, vararg results: ParticipantResult): Observable<TurnBasedMultiplayer.UpdateMatchResult> {
        return fromPendingResult { takeTurn(it, matchId, matchData, pendingParticipantId, *results) }
    }

    fun takeTurn(matchId: String, matchData: ByteArray, pendingParticipantId: String, results: List<ParticipantResult>): Observable<TurnBasedMultiplayer.UpdateMatchResult> {
        return fromPendingResult { takeTurn(it, matchId, matchData, pendingParticipantId, results) }
    }

    fun finishMatch(matchId: String): Observable<TurnBasedMultiplayer.UpdateMatchResult> {
        return fromPendingResult { finishMatch(it, matchId) }
    }

    fun finishMatch(matchId: String, matchData: ByteArray, vararg results: ParticipantResult): Observable<TurnBasedMultiplayer.UpdateMatchResult> {
        return fromPendingResult { finishMatch(it, matchId, matchData, *results) }
    }

    fun finishMatch(matchId: String, matchData: ByteArray, results: List<ParticipantResult>): Observable<TurnBasedMultiplayer.UpdateMatchResult> {
        return fromPendingResult { finishMatch(it, matchId, matchData, results) }
    }

    fun leaveMatch(matchId: String): Observable<TurnBasedMultiplayer.LeaveMatchResult> {
        return fromPendingResult { leaveMatch(it, matchId) }
    }

    fun leaveMatchDuringTurn(matchId: String, pendingParticipantId: String): Observable<TurnBasedMultiplayer.LeaveMatchResult> {
        return fromPendingResult { leaveMatchDuringTurn(it, matchId, pendingParticipantId) }
    }

    fun cancelMatch(matchId: String): Observable<TurnBasedMultiplayer.CancelMatchResult> {
        return fromPendingResult { cancelMatch(it, matchId) }
    }

    fun dismissMatch(matchId: String): Completable {
        return toCompletable { dismissMatch(it, matchId) }
    }

    fun loadMatchesByStatus(matchTurnStatuses: IntArray): Observable<TurnBasedMultiplayer.LoadMatchesResult> {
        return fromPendingResult { loadMatchesByStatus(it, matchTurnStatuses) }
    }

    fun loadMatchesByStatus(invitationSortOrder: Int, matchTurnStatuses: IntArray): Observable<TurnBasedMultiplayer.LoadMatchesResult> {
        return fromPendingResult { loadMatchesByStatus(it, invitationSortOrder, matchTurnStatuses) }
    }

    fun loadMatch(matchId: String): Observable<TurnBasedMultiplayer.LoadMatchResult> {
        return fromPendingResult { loadMatch(it, matchId) }
    }

}