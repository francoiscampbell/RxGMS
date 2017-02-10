package xyz.fcampbell.rxplayservices.drive.test

import android.support.test.InstrumentationRegistry
import com.google.android.gms.drive.Drive
import org.junit.Test
import xyz.fcampbell.rxplayservices.drive.RxDriveApi
import java.util.concurrent.TimeUnit

class RxDriveApiTest {
    val testApi = RxDriveApi(InstrumentationRegistry.getContext(), Drive.SCOPE_FILE, Drive.SCOPE_APPFOLDER)

    @Test
    fun connectsToDrive() {
        testApi.getAppFolder()
                .test()
                .assertSubscribed()
                .awaitDone(20, TimeUnit.SECONDS)
                .assertValueCount(1)
                .assertTerminated()
    }
}