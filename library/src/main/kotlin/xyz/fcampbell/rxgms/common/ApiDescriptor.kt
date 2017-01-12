package xyz.fcampbell.rxgms.common

import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.Scope

/**
 * Created by francois on 2017-01-09.
 */
class ApiDescriptor<O : Api.ApiOptions>(
        val api: Api<O>,
        val options: O? = null,
        val accountName: String = "",
        vararg val scopes: Scope
)