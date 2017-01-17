package xyz.fcampbell.rxgms.games

import android.content.Context
import android.content.Intent
import com.google.android.gms.common.api.Scope
import com.google.android.gms.games.Games
import com.google.android.gms.games.multiplayer.realtime.RealTimeMultiplayer
import com.google.android.gms.games.multiplayer.realtime.Room
import com.google.android.gms.games.multiplayer.realtime.RoomConfig
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener
import rx.Completable
import rx.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi

/**
 * Created by francois on 2017-01-13.
 */
@Suppress("unused")
class RxRealTimeMultiplayer(
        apiClientDescriptor: ApiClientDescriptor,
        gamesOptions: Games.GamesOptions,
        vararg scopes: Scope
) : RxGmsApi<RealTimeMultiplayer, Games.GamesOptions>(
        apiClientDescriptor,
        ApiDescriptor(Games.API, Games.RealTimeMultiplayer, gamesOptions, *scopes)
) {
    constructor(
            context: Context,
            gamesOptions: Games.GamesOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), gamesOptions, *scopes)

    fun getWaitingRoomIntent(room: Room, minParticipantsToStart: Int): Observable<Intent> {
        return map { getWaitingRoomIntent(it, room, minParticipantsToStart) }
    }

    fun getSelectOpponentsIntent(minPlayers: Int, maxPlayers: Int): Observable<Intent> {
        return map { getSelectOpponentsIntent(it, minPlayers, maxPlayers) }
    }

    fun getSelectOpponentsIntent(minPlayers: Int, maxPlayers: Int, allowAutomatch: Boolean): Observable<Intent> {
        return map { getSelectOpponentsIntent(it, minPlayers, maxPlayers, allowAutomatch) }
    }

    fun create(config: RoomConfig): Completable {
        return toCompletable { create(it, config) }
    }

    fun join(config: RoomConfig): Completable {
        return toCompletable { join(it, config) }
    }

    fun leave(listener: RoomUpdateListener, roomId: String): Completable {
        return toCompletable { leave(it, listener, roomId) }
    }

    fun sendReliableMessage(listener: RealTimeMultiplayer.ReliableMessageSentCallback, messageData: ByteArray, roomId: String, recipientParticipantId: String): Observable<Int> {
        return map { sendReliableMessage(it, listener, messageData, roomId, recipientParticipantId) }
    }

    fun sendUnreliableMessage(messageData: ByteArray, roomId: String, recipientParticipantId: String): Observable<Int> {
        return map { sendUnreliableMessage(it, messageData, roomId, recipientParticipantId) }
    }

    fun sendUnreliableMessage(messageData: ByteArray, roomId: String, recipientParticipantIds: List<String>): Observable<Int> {
        return map { sendUnreliableMessage(it, messageData, roomId, recipientParticipantIds) }
    }

    fun sendUnreliableMessageToOthers(messageData: ByteArray, roomId: String): Observable<Int> {
        return map { sendUnreliableMessageToOthers(it, messageData, roomId) }
    }

    fun declineInvitation(invitationId: String): Completable {
        return toCompletable { declineInvitation(it, invitationId) }
    }

    fun dismissInvitation(invitationId: String): Completable {
        return toCompletable { dismissInvitation(it, invitationId) }
    }
}