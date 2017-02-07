package xyz.fcampbell.rxplayservices.games

import android.content.Context
import com.google.android.gms.common.api.Scope
import com.google.android.gms.games.Games
import com.google.android.gms.games.Notifications
import io.reactivex.Completable
import xyz.fcampbell.rxplayservices.common.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.common.ApiDescriptor
import xyz.fcampbell.rxplayservices.common.RxPlayServicesApi

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