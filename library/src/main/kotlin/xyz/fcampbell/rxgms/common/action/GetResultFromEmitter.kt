package xyz.fcampbell.rxgms.common.action

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import rx.AsyncEmitter
import xyz.fcampbell.rxgms.common.util.ResultActivity

/**
 * Created by francois on 2017-01-11.
 */
internal class GetResultFromEmitter(
        private val context: Context,
        private val intentSender: IntentSender
) : FromEmitter<Intent?>() {
    override fun call(emitter: AsyncEmitter<Intent?>) {
        super.call(emitter)

        ResultActivity.getResult(context, intentSender) { data ->
            emitter.onNext(data)
            emitter.onCompleted()
        }
    }
}