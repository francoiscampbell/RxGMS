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
        return map { Games.Invitations.getInvitationInboxIntent(it) }
    }

    fun registerInvitationListener(listener: OnInvitationReceivedListener): Completable {
        return toCompletable { Games.Invitations.registerInvitationListener(it, listener) }
    }

    fun unregisterInvitationListener(): Completable {
        return toCompletable { Games.Invitations.unregisterInvitationListener(it) }
    }

    fun loadInvitations(): Observable<Invitations.LoadInvitationsResult> {
        return fromPendingResult { Games.Invitations.loadInvitations(it) }
    }

    fun loadInvitations(sortOrder: Int): Observable<Invitations.LoadInvitationsResult> {
        return fromPendingResult { Games.Invitations.loadInvitations(it, sortOrder) }
    }
}