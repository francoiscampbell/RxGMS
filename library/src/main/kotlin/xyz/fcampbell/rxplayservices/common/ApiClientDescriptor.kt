package xyz.fcampbell.rxplayservices.common

import android.content.Context
import android.os.Handler
import android.view.View

/**
 * Proxy for [GoogleApiClient.Builder]
 */
@Suppress("unused")
class ApiClientDescriptor(
        val context: Context
) {
    var handler: Handler? = null
        private set

    var viewForPopups: View? = null
        private set

    var accountName = ""
        private set

    var gravityForPopups = Int.MIN_VALUE
        private set

    fun setHandler(handler: Handler): ApiClientDescriptor {
        this.handler = handler
        return this
    }

    fun setViewForPopups(viewForPopups: View): ApiClientDescriptor {
        this.viewForPopups = viewForPopups
        return this
    }

    fun setAccountName(accountName: String): ApiClientDescriptor {
        this.accountName = accountName
        return this
    }

    fun useDefaultAccount(): ApiClientDescriptor {
        return this.setAccountName("<<default account>>")
    }

    fun setGravityForPopups(gravityForPopups: Int): ApiClientDescriptor {
        this.gravityForPopups = gravityForPopups
        return this
    }
}