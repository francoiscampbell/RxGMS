package xyz.fcampbell.rxplayservices.appinvite

import android.app.Activity
import android.content.Context
import com.google.android.gms.appinvite.AppInvite
import com.google.android.gms.appinvite.AppInviteApi
import com.google.android.gms.appinvite.AppInviteInvitationResult
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.Scope
import com.google.android.gms.common.api.Status
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.RxPlayServicesApi

/**
 * Wraps [AppInvite.AppInviteApi]
 */
@Suppress("unused")
class RxAppInviteApi(
        apiClientDescriptor: ApiClientDescriptor,
        options: Api.ApiOptions.NoOptions,
        vararg scopes: Scope
) : RxPlayServicesApi<AppInviteApi, Api.ApiOptions.NoOptions>(
        apiClientDescriptor,
        ApiDescriptor(AppInvite.API, AppInvite.AppInviteApi, options, *scopes)
) {
    constructor(
            context: Context,
            options: Api.ApiOptions.NoOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), options, *scopes)

    fun convertInvitation(invitationId: String): Observable<Status> {
        return fromPendingResult { convertInvitation(it, invitationId) }
    }

    fun getInvitation(currentActivity: Activity, launchDeepLink: Boolean): Observable<AppInviteInvitationResult> {
        return fromPendingResult { getInvitation(it, currentActivity, launchDeepLink) }
    }
}