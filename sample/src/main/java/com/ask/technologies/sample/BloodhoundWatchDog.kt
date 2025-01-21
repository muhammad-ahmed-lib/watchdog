package com.ask.technologies.sample

import android.app.Activity
import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics

/**
 * Abstract class representing a watchdog service for monitoring crashes and errors
 * in an application. Subclasses should implement the required methods to provide
 * functionality for starting and stopping the monitoring service.
 */
abstract class BloodhoundWatchDog {

    /**
     * The context of the application or activity where this service is running.
     * Used for accessing application-specific resources and operations.
     */
    abstract val context: Context

    /**
     * Optional reference to the activity context, if available.
     * Useful for activity-specific operations or navigation.
     */
    abstract val activity: Activity?

    /**
     * Defines the behavior to be followed when an error or crash occurs.
     * Implementations should specify how errors are handled (e.g., log, notify, terminate).
     */
    abstract val behaviour: WatchDogBehaviour

    /**
     * Listener to capture error events and handle them externally.
     * Useful for providing custom error reporting or analytics.
     */
    abstract val eventListener: WatchDogEventListener?

    /**
     * The title to be displayed when an error or crash occurs.
     * Can be used for showing user-friendly messages in dialogs or notifications.
     */
    abstract var title: String?

    /**
     * The detailed message to be displayed along with the title when an error or crash occurs.
     * Provides additional context about the error.
     */
    abstract var message: String?

    /**
     * An instance of FirebaseCrashlytics used to report crashes or exceptions to Firebase.
     * Allows integration with Firebase Crashlytics for remote error monitoring.
     */
    abstract var mFirebaseCrashlytics: FirebaseCrashlytics

    /**
     * Starts the monitoring service.
     * Subclasses should implement this method to initialize crash monitoring operations
     * and begin tracking for errors. This could include setting up uncaught exception handlers.
     */
    abstract fun startService()

    /**
     * Stops the monitoring service.
     * Subclasses should implement this method to clean up resources and stop any crash
     * monitoring operations. Ensure that ongoing threads or handlers are properly terminated.
     */
    abstract fun stopService()
}
