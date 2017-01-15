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
import xyz.fcampbell.rxgms.common.util.pendingResultToObservable
import xyz.fcampbell.rxgms.common.util.toCompletable

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
        return apiClient.map { Games.Achievements.getAchievementsIntent(it.first) }
    }

    fun load(forceReload: Boolean): Observable<Achievements.LoadAchievementsResult> {
        return apiClient.pendingResultToObservable { Games.Achievements.load(it.first, forceReload) }
    }

    fun reveal(id: String): Completable {
        return apiClient.toCompletable { Games.Achievements.reveal(it.first, id) }
    }

    fun revealImmediate(id: String): Observable<Achievements.UpdateAchievementResult> {
        return apiClient.pendingResultToObservable { Games.Achievements.revealImmediate(it.first, id) }
    }

    fun unlock(id: String): Completable {
        return apiClient.toCompletable { Games.Achievements.unlock(it.first, id) }
    }

    fun unlockImmediate(id: String): Observable<Achievements.UpdateAchievementResult> {
        return apiClient.pendingResultToObservable { Games.Achievements.unlockImmediate(it.first, id) }
    }

    fun increment(id: String, numSteps: Int): Completable {
        return apiClient.toCompletable { Games.Achievements.increment(it.first, id, numSteps) }
    }

    fun incrementImmediate(id: String, numSteps: Int): Observable<Achievements.UpdateAchievementResult> {
        return apiClient.pendingResultToObservable { Games.Achievements.incrementImmediate(it.first, id, numSteps) }
    }

    fun setSteps(id: String, numSteps: Int): Completable {
        return apiClient.toCompletable { Games.Achievements.setSteps(it.first, id, numSteps) }
    }

    fun setStepsImmediate(id: String, numSteps: Int): Observable<Achievements.UpdateAchievementResult> {
        return apiClient.pendingResultToObservable { Games.Achievements.setStepsImmediate(it.first, id, numSteps) }
    }
}