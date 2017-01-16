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
) : RxGmsApi<Games.GamesOptions>(
        apiClientDescriptor,
        ApiDescriptor(Games.API, gamesOptions, *scopes)
) {
    constructor(
            context: Context,
            gamesOptions: Games.GamesOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), gamesOptions, *scopes)

    fun getAchievementsIntent(): Observable<Intent> {
        return map { Games.Achievements.getAchievementsIntent(it) }
    }

    fun load(forceReload: Boolean): Observable<Achievements.LoadAchievementsResult> {
        return fromPendingResult { Games.Achievements.load(it, forceReload) }
    }

    fun reveal(id: String): Completable {
        return toCompletable { Games.Achievements.reveal(it, id) }
    }

    fun revealImmediate(id: String): Observable<Achievements.UpdateAchievementResult> {
        return fromPendingResult { Games.Achievements.revealImmediate(it, id) }
    }

    fun unlock(id: String): Completable {
        return toCompletable { Games.Achievements.unlock(it, id) }
    }

    fun unlockImmediate(id: String): Observable<Achievements.UpdateAchievementResult> {
        return fromPendingResult { Games.Achievements.unlockImmediate(it, id) }
    }

    fun increment(id: String, numSteps: Int): Completable {
        return toCompletable { Games.Achievements.increment(it, id, numSteps) }
    }

    fun incrementImmediate(id: String, numSteps: Int): Observable<Achievements.UpdateAchievementResult> {
        return fromPendingResult { Games.Achievements.incrementImmediate(it, id, numSteps) }
    }

    fun setSteps(id: String, numSteps: Int): Completable {
        return toCompletable { Games.Achievements.setSteps(it, id, numSteps) }
    }

    fun setStepsImmediate(id: String, numSteps: Int): Observable<Achievements.UpdateAchievementResult> {
        return fromPendingResult { Games.Achievements.setStepsImmediate(it, id, numSteps) }
    }
}