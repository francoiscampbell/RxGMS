package xyz.fcampbell.rxgms.games

import android.content.Context
import android.content.Intent
import com.google.android.gms.common.api.Scope
import com.google.android.gms.games.Games
import com.google.android.gms.games.multiplayer.Invitations
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener
import rx.Completable
import rx.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi
import xyz.fcampbell.rxgms.common.util.fromPendingResult
import xyz.fcampbell.rxgms.common.util.toCompletable

/**
 * Created by francois on 2017-01-13.
 */
@Suppress("unused")
class RxInvitations(
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

    fun getInvitationInboxIntent(): Observable<Intent> {
        return apiClientPair.map { Games.Invitations.getInvitationInboxIntent(it.first) }
    }

    fun registerInvitationListener(listener: OnInvitationReceivedListener): Completable {
        return apiClientPair.toCompletable { Games.Invitations.registerInvitationListener(it.first, listener) }
    }

    fun unregisterInvitationListener(): Completable {
        return apiClientPair.toCompletable { Games.Invitations.unregisterInvitationListener(it.first) }
    }

    fun loadInvitations(): Observable<Invitations.LoadInvitationsResult> {
        return apiClientPair.fromPendingResult { Games.Invitations.loadInvitations(it.first) }
    }

    fun loadInvitations(sortOrder: Int): Observable<Invitations.LoadInvitationsResult> {
        return apiClientPair.fromPendingResult { Games.Invitations.loadInvitations(it.first, sortOrder) }
    }
}