package xyz.fcampbell.rxplayservices.games

import android.content.Context
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.RxPlayServicesApi

/**
 * Wraps [Games.Notifications]
 */
@Suppress("unused")
class RxNotifications(
        apiClientDescriptor: ApiClientDescriptor,
        gamesOptions: Games.GamesOptions,
        vararg scopes: Scope
) : RxPlayServicesApi<Notifications, Games.GamesOptions>(
        apiClientDescriptor,
        ApiDescriptor(Games.API, Games.Notifications, gamesOptions, *scopes)
) {
    constructor(
            context: Context,
            gamesOptions: Games.GamesOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), gamesOptions, *scopes)

    fun clear(notificationTypes: Int): Completable {
        return toCompletable { clear(it, notificationTypes) }
    }

    fun clearAll(): Completable {
        return toCompletable { clearAll(it) }
    }
}