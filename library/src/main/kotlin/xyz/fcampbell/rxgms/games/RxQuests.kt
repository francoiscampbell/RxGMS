package xyz.fcampbell.rxgms.games

import android.content.Context
import android.content.Intent
import com.google.android.gms.common.api.Scope
import com.google.android.gms.games.Games
import com.google.android.gms.games.quest.QuestUpdateListener
import com.google.android.gms.games.quest.Quests
import rx.Completable
import rx.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi

/**
 * Created by francois on 2017-01-13.
 */
@Suppress("unused")
class RxQuests(
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

    fun registerQuestUpdateListener(listener: QuestUpdateListener): Completable {
        return toCompletable { Games.Quests.registerQuestUpdateListener(it, listener) }
    }

    fun unregisterQuestUpdateListener(): Completable {
        return toCompletable { Games.Quests.unregisterQuestUpdateListener(it) }
    }

    fun accept(questId: String): Observable<Quests.AcceptQuestResult> {
        return fromPendingResult { Games.Quests.accept(it, questId) }
    }

    fun claim(questId: String, milestoneId: String): Observable<Quests.ClaimMilestoneResult> {
        return fromPendingResult { Games.Quests.claim(it, questId, milestoneId) }
    }

    fun load(questSelectors: IntArray, sortOrder: Int, forceReload: Boolean): Observable<Quests.LoadQuestsResult> {
        return fromPendingResult { Games.Quests.load(it, questSelectors, sortOrder, forceReload) }
    }

    fun loadByIds(forceReload: Boolean, vararg questIds: String): Observable<Quests.LoadQuestsResult> {
        return fromPendingResult { Games.Quests.loadByIds(it, forceReload, *questIds) }
    }

    fun showStateChangedPopup(questId: String): Completable {
        return toCompletable { Games.Quests.showStateChangedPopup(it, questId) }
    }

    fun getQuestsIntent(questSelectors: IntArray): Observable<Intent> {
        return map { Games.Quests.getQuestsIntent(it, questSelectors) }
    }

    fun getQuestIntent(questId: String): Observable<Intent> {
        return map { Games.Quests.getQuestIntent(it, questId) }
    }
}