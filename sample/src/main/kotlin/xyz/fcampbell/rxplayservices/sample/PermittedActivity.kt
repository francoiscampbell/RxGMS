package xyz.fcampbell.rxplayservices.sample

import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.tbruyelle.rxpermissions2.RxPermissions

abstract class PermittedActivity : AppCompatActivity() {
    abstract val permissionsToRequest: Array<String>

    override fun onStart() {
        super.onStart()
        RxPermissions(this)
                .request(*permissionsToRequest)
                .subscribe { granted ->
                    if (granted) {
                        onPermissionsGranted(*permissionsToRequest)
                    } else {
                        Toast.makeText(this@PermittedActivity, "Sorry, no demo without permission...", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    protected abstract fun onPermissionsGranted(vararg permissions: String)
}
