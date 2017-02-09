package xyz.fcampbell.rxplayservices.games

import android.content.Context
import android.content.Intent
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.RxPlayServicesApi

/**
 * Wraps [Games.Invitations]
 */
@Suppress("unused")
class RxInvitations(
        apiClientDescriptor: ApiClientDescriptor,
        gamesOptions: Games.GamesOptions,
        vararg scopes: Scope
) : RxPlayServicesApi<Invitations, Games.GamesOptions>(
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