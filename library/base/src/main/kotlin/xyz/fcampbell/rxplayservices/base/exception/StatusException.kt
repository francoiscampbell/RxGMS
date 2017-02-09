package xyz.fcampbell.rxplayservices.base.exception

import com.google.android.gms.common.api.Status

class StatusException(val status: Status) : Exception()
