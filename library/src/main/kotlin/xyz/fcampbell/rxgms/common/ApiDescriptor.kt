package xyz.fcampbell.rxgms.common

import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.Scope

/**
 * Created by francois on 2017-01-09.
 */
class ApiDescriptor<O : Api.ApiOptions>(
        val apis: Array<OptionsHolder<O>>,
        val accountName: String = "",
        vararg val scopes: Scope
) {
    data class OptionsHolder<O : Api.ApiOptions>(
            val service: Api<O>,
            val options: O? = null
    )
}