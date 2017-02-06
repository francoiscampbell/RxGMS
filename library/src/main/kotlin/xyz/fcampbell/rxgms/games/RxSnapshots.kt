package xyz.fcampbell.rxgms.games

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.gms.common.api.Scope
import com.google.android.gms.games.Games
import com.google.android.gms.games.snapshot.*
import io.reactivex.Completable
import io.reactivex.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi

/**
 * Wraps [Games.Snapshots]
 */
@Suppress("unused")
class RxSnapshots(
        apiClientDescriptor: ApiClientDescriptor,
        gamesOptions: Games.GamesOptions,
        vararg scopes: Scope
) : RxGmsApi<Snapshots, Games.GamesOptions>(
        apiClientDescriptor,
        ApiDescriptor(Games.API, Games.Snapshots, gamesOptions, *scopes)
) {
    constructor(
            context: Context,
            gamesOptions: Games.GamesOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), gamesOptions, *scopes)

    fun getMaxDataSize(): Observable<Int> {
        return map { getMaxDataSize(it) }
    }

    fun getMaxCoverImageSize(): Observable<Int> {
        return map { getMaxCoverImageSize(it) }
    }

    fun getSelectSnapshotIntent(title: String, allowAddButton: Boolean, allowDelete: Boolean, maxSnapshots: Int): Observable<Intent> {
        return map { getSelectSnapshotIntent(it, title, allowAddButton, allowDelete, maxSnapshots) }
    }

    fun load(forceReload: Boolean): Observable<Snapshots.LoadSnapshotsResult> {
        return fromPendingResult { load(it, forceReload) }
    }

    fun open(fileName: String, createIfNotFound: Boolean): Observable<Snapshots.OpenSnapshotResult> {
        return fromPendingResult { open(it, fileName, createIfNotFound) }
    }

    fun open(fileName: String, createIfNotFound: Boolean, conflictPolicy: Int): Observable<Snapshots.OpenSnapshotResult> {
        return fromPendingResult { open(it, fileName, createIfNotFound, conflictPolicy) }
    }

    fun open(metadata: SnapshotMetadata): Observable<Snapshots.OpenSnapshotResult> {
        return fromPendingResult { open(it, metadata) }
    }

    fun open(metadata: SnapshotMetadata, conflictPolicy: Int): Observable<Snapshots.OpenSnapshotResult> {
        return fromPendingResult { open(it, metadata, conflictPolicy) }
    }

    fun commitAndClose(snapshot: Snapshot, metadataChange: SnapshotMetadataChange): Observable<Snapshots.CommitSnapshotResult> {
        return fromPendingResult { commitAndClose(it, snapshot, metadataChange) }
    }

    fun discardAndClose(snapshot: Snapshot): Completable {
        return toCompletable { discardAndClose(it, snapshot) }
    }

    fun delete(metadata: SnapshotMetadata): Observable<Snapshots.DeleteSnapshotResult> {
        return fromPendingResult { delete(it, metadata) }
    }

    fun getSnapshotFromBundle(extras: Bundle): Observable<SnapshotMetadata> {
        return Observable.just(Games.Snapshots.getSnapshotFromBundle(extras))
    }

    fun resolveConflict(conflictId: String, snapshot: Snapshot): Observable<Snapshots.OpenSnapshotResult> {
        return fromPendingResult { resolveConflict(it, conflictId, snapshot) }
    }

    fun resolveConflict(conflictId: String, snapshotId: String, metadataChange: SnapshotMetadataChange, snapshotContents: SnapshotContents): Observable<Snapshots.OpenSnapshotResult> {
        return fromPendingResult { resolveConflict(it, conflictId, snapshotId, metadataChange, snapshotContents) }
    }
}