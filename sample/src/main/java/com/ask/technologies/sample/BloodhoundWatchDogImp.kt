package com.ask.technologies.sample

import android.app.Activity
import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics

/**
 * BloodhoundWatchDogImp is a concrete implementation of the BloodhoundWatchDog class
 * that monitors application crashes and uncaught exceptions. It uses the builder pattern
 * to configure and manage the BloodhoundService for error handling and reporting.
 *
 * @property context The application context used for starting activities and services.
 * @property behaviour Defines the behavior when a crash occurs (e.g., restarting the app).
 * @property eventListener Listener for handling error events such as crashes.
 * @property title The title for error notifications displayed when a crash occurs.
 * @property message The detailed message for error notifications when a crash occurs.
 * @property mFirebaseCrashlytics Instance of FirebaseCrashlytics for crash logging.
 * @property activity The activity context (optional), used to navigate to crash report screen.
 */
class BloodhoundWatchDogImp(
    override val context: Context,
    override val behaviour: WatchDogBehaviour,
    override val eventListener: WatchDogEventListener?,
    override var title: String?,
    override var message: String?,
    override var mFirebaseCrashlytics: FirebaseCrashlytics,
    override val activity: Activity?
) : BloodhoundWatchDog() {

    // Lazy initialization of BloodhoundService using the builder pattern
    private val mBloodhoundServiceBuilder by lazy {
        BloodhoundServiceBuilder.Init()
            .setContext(context)  // Set the application context
            .setActivity(activity)  // Set the optional activity context
            .setBehaviour(behaviour)  // Set the behavior on crash (e.g., restart)
            .setEventListener(eventListener)  // Set listener for error events
            .setTitle(title)  // Set the title for crash notifications
            .setFirebaseCrashlytics(mFirebaseCrashlytics)  // Set FirebaseCrashlytics instance
            .setMessage(message)  // Set the crash message for notifications
            .build()  // Build the BloodhoundService
            .monitorService()  // Start monitoring the application for crashes
    }

    /**
     * Starts the crash monitoring service. It initializes and connects the BloodhoundService
     * to begin tracking uncaught exceptions and crashes.
     */
    override fun startService() {
        mBloodhoundServiceBuilder.connect()  // Connect and start monitoring
    }

    /**
     * Stops the crash monitoring service. This method disconnects the BloodhoundService
     * and stops tracking uncaught exceptions and crashes.
     */
    override fun stopService() {
        mBloodhoundServiceBuilder.disconnect()  // Disconnect and stop monitoring
    }
}
