package xyz.fcampbell.rxplayservices.fitness

import android.app.PendingIntent
import android.content.Context
import android.support.annotation.RequiresPermission
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.Scope
import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.SessionsApi
import com.google.android.gms.fitness.data.Session
import com.google.android.gms.fitness.request.SessionInsertRequest
import com.google.android.gms.fitness.request.SessionReadRequest
import com.google.android.gms.fitness.result.SessionReadResult
import com.google.android.gms.fitness.result.SessionStopResult
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.RxPlayServicesApi

/**
 * Wraps [Fitness.SessionsApi]
 */
@Suppress("unused")
class RxSessionsApi(
        apiClientDescriptor: ApiClientDescriptor,
        options: Api.ApiOptions.NoOptions,
        vararg scopes: Scope
) : RxPlayServicesApi<SessionsApi, Api.ApiOptions.NoOptions>(
        apiClientDescriptor,
        ApiDescriptor(Fitness.SESSIONS_API, Fitness.SessionsApi, options, *scopes)
) {
    constructor(
            context: Context,
            options: Api.ApiOptions.NoOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), options, *scopes)

    fun startSession(session: Session): Observable<Status> {
        return fromPendingResult { startSession(it, session) }
    }

    fun stopSession(identifier: String): Observable<SessionStopResult> {
        return fromPendingResult { stopSession(it, identifier) }
    }

    fun insertSession(request: SessionInsertRequest): Observable<Status> {
        return fromPendingResult { insertSession(it, request) }
    }

    @RequiresPermission(
            anyOf = arrayOf("android.permission.ACCESS_FINE_LOCATION", "android.permission.BODY_SENSORS"),
            conditional = true)
    fun readSession(request: SessionReadRequest): Observable<SessionReadResult> {
        return fromPendingResult { readSession(it, request) }
    }

    fun registerForSessions(pendingIntent: PendingIntent): Observable<Status> {
        return fromPendingResult { registerForSessions(it, pendingIntent) }
    }

    fun unregisterForSessions(pendingIntent: PendingIntent): Observable<Status> {
        return fromPendingResult { unregisterForSessions(it, pendingIntent) }
    }

}