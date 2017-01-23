package xyz.fcampbell.rxgms.games

import android.content.Context
import android.content.Intent
import com.google.android.gms.common.api.Scope
import com.google.android.gms.games.Games
import com.google.android.gms.games.multiplayer.Invitations
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener
import io.reactivex.Completable
import io.reactivex.Observable
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
) : RxGmsApi<Invitations, Games.GamesOptions>(
        apiClientDescriptor,
        ApiDescriptor(Games.API, Games.Invitations, gamesOptions, *scopes)
) {
    constructor(
            context: Context,
            gamesOptions: Games.GamesOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), gamesOptions, *scopes)

    fun getInvitationInboxIntent(): Observable<Intent> {
        return map { getInvitationInboxIntent(it) }
    }

    fun registerInvitationListener(listener: OnInvitationReceivedListener): Completable {
        return toCompletable { registerInvitationListener(it, listener) }
    }

    fun unregisterInvitationListener(): Completable {
        return toCompletable { unregisterInvitationListener(it) }
    }

    fun loadInvitations(): Observable<Invitations.LoadInvitationsResult> {
        return fromPendingResult { loadInvitations(it) }
    }

    fun loadInvitations(sortOrder: Int): Observable<Invitations.LoadInvitationsResult> {
        return fromPendingResult { loadInvitations(it, sortOrder) }
    }
}