package uk.whitecrescent.waqti.frontend

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import uk.whitecrescent.waqti.BuildConfig.BUILD_TYPE
import uk.whitecrescent.waqti.BuildConfig.VERSION_CODE
import uk.whitecrescent.waqti.BuildConfig.VERSION_NAME
import uk.whitecrescent.waqti.backend.persistence.Caches
import uk.whitecrescent.waqti.backend.persistence.Database

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        /*if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)*/
        AndroidThreeTen.init(this)
        Database.build(this)
        Caches.initialize()
        Database.applyMigration()
    }
}

const val WAQTI_VERSION = "$BUILD_TYPE $VERSION_NAME $VERSION_CODE"