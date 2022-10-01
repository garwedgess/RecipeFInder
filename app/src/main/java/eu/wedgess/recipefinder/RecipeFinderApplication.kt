package eu.wedgess.recipefinder

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import eu.wedgess.recipefinder.BuildConfig
import timber.log.Timber

@HiltAndroidApp
class RecipeFinderApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}