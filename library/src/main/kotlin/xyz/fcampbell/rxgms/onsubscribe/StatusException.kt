package xyz.fcampbell.rxgms.onsubscribe

import com.google.android.gms.common.api.Status

class StatusException(val status: Status) : Throwable()
