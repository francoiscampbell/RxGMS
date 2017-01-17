package xyz.fcampbell.rxgms.games

import android.content.Context
import android.content.Intent
import com.google.android.gms.common.api.Scope
import com.google.android.gms.games.Games
import com.google.android.gms.games.achievement.Achievements
import rx.Completable
import rx.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi

/**
 * Created by francois on 2017-01-13.
 */
@Suppress("unused")
class RxAchievements(
        apiClientDescriptor: ApiClientDescriptor,
        gamesOptions: Games.GamesOptions,
        vararg scopes: Scope
) : RxGmsApi<Achievements, Games.GamesOptions>(
        apiClientDescriptor,
        ApiDescriptor(Games.API, Games.Achievements, gamesOptions, *scopes)
) {
    constructor(
            context: Context,
            gamesOptions: Games.GamesOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), gamesOptions, *scopes)

    fun getAchievementsIntent(): Observable<Intent> {
        return map { getAchievementsIntent(it) }
    }

    fun load(forceReload: Boolean): Observable<Achievements.LoadAchievementsResult> {
        return fromPendingResult { load(it, forceReload) }
    }

    fun reveal(id: String): Completable {
        return toCompletable { reveal(it, id) }
    }

    fun revealImmediate(id: String): Observable<Achievements.UpdateAchievementResult> {
        return fromPendingResult { revealImmediate(it, id) }
    }

    fun unlock(id: String): Completable {
        return toCompletable { unlock(it, id) }
    }

    fun unlockImmediate(id: String): Observable<Achievements.UpdateAchievementResult> {
        return fromPendingResult { unlockImmediate(it, id) }
    }

    fun increment(id: String, numSteps: Int): Completable {
        return toCompletable { increment(it, id, numSteps) }
    }

    fun incrementImmediate(id: String, numSteps: Int): Observable<Achievements.UpdateAchievementResult> {
        return fromPendingResult { incrementImmediate(it, id, numSteps) }
    }

    fun setSteps(id: String, numSteps: Int): Completable {
        return toCompletable { setSteps(it, id, numSteps) }
    }

    fun setStepsImmediate(id: String, numSteps: Int): Observable<Achievements.UpdateAchievementResult> {
        return fromPendingResult { setStepsImmediate(it, id, numSteps) }
    }
}