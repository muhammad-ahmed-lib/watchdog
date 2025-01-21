package com.ask.technologies.watchdog

import android.app.Application
import com.ask.technologies.sample.CrashReport
import com.ask.technologies.sample.WatchDogActivityBuilder
import com.ask.technologies.sample.WatchDogBehaviour
import com.ask.technologies.sample.WatchDogEventListener
import com.google.firebase.crashlytics.FirebaseCrashlytics

class MyApp:Application() {
    override fun onCreate() {
        super.onCreate()
        val watchDogBehaviour= WatchDogBehaviour.EXIST_APP

        val watchDogEventListener=object:WatchDogEventListener(){
            override fun onErrorCaptured(report: CrashReport) {

            }
        }

        val firebaseCrashlytics=FirebaseCrashlytics.getInstance()

        WatchDogActivityBuilder(
            appContext = this,
            behaviour =watchDogBehaviour,
            watchDogEventListener=watchDogEventListener,
            title = "App Crashed",
            message = "Sorry we are working to fix this error.",
            crashlytics =firebaseCrashlytics
        )
    }
}