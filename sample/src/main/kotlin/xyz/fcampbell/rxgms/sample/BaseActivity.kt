package xyz.fcampbell.rxgms.sample

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.tbruyelle.rxpermissions.RxPermissions

abstract class BaseActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        RxPermissions
                .getInstance(this)
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe { granted ->
                    if (granted) {
                        onLocationPermissionGranted()
                    } else {
                        Toast.makeText(this@BaseActivity, "Sorry, no demo without permission...", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    protected abstract fun onLocationPermissionGranted()
}
