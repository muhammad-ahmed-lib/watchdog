package com.ask.technologies.sample

import android.app.Activity
import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics


abstract class BloodhoundService {
    abstract val context: Context
    abstract val activity: Activity?
    abstract val watchDogEventListener: WatchDogEventListener?
    abstract val crashlytics: FirebaseCrashlytics
    abstract val behaviour: WatchDogBehaviour
    abstract var title: String?
    abstract var message: String?

    /**
     * Connects the service, starting the monitoring process.
     *
     * This method is intended to initialize and start monitoring
     * for specific events or behaviors based on the provided
     * implementation. Once connected:
     * - The service begins to observe and log events,
     *   depending on the `WatchDogBehaviour` configuration.
     * - Any crash or unexpected behavior can be reported
     *   to `FirebaseCrashlytics` for diagnostics.
     * - Listeners, if provided, are attached to handle
     *   specific `WatchDogEventListener` events.
     */
    abstract fun connect()

    /**
     * Disconnects the service, stopping the monitoring process.
     *
     * This method is responsible for:
     * - Releasing any resources allocated during the monitoring process.
     * - Detaching any listeners or observers, ensuring no memory leaks occur.
     * - Stopping further event observation or crash reporting,
     *   effectively pausing the service.
     *
     * Once this method is called, the service will cease its monitoring activities
     * until `connect` is called again.
     */
    abstract fun disconnect()
}
