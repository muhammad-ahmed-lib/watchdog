package com.ask.technologies.sample

import android.app.Activity
import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics

/**
 * The WatchDog class is designed to monitor uncaught exceptions and crashes in the app.
 * It uses a builder pattern to configure the WatchDog instance with various options.
 *
 * @author Muhammad Ahmed (Android Engineer & Library Author)
 *
 */
class WatchDog {

    /**
     * Builder class is used to build and configure the WatchDog instance.
     * The builder pattern allows for setting optional configurations step-by-step before creating the final WatchDog object.
     */
    class Builder {
        private lateinit var mContext: Context // The context for the application, used for various operations.
        private var mActivity: Activity? =
            null // The activity that will be monitored for crashes (optional).
        private var mTitle: String? = null // Title to be displayed in the case of an error.
        private var mMessage: String? =
            null // Message to be displayed along with the title when an error occurs.
        private var mWatchDogBehaviour =
            WatchDogBehaviour.RESTART_APP // The action to take upon crash (default is to restart the app).
        private lateinit var mCrashlytics: FirebaseCrashlytics // Instance of FirebaseCrashlytics for reporting crashes.
        private var mEventListener: WatchDogEventListener? =
            null // Optional event listener for handling error events.

        /**
         * Sets the activity to be monitored for crashes.
         * @param activity The activity that will be observed.
         */
        fun setActivity(activity: Activity?) = apply {
            this.mActivity = activity
        }

        /**
         * Sets the application context for use in operations.
         * @param context The context of the application.
         */
        fun setContext(context: Context) = apply {
            this.mContext = context
        }

        /**
         * Sets the event listener for the WatchDog to capture any errors or events.
         * @param eventListener The event listener to handle errors (optional).
         */
        fun setEventListener(eventListener: WatchDogEventListener? = null) = apply {
            this.mEventListener = eventListener
        }

        /**
         * Sets the FirebaseCrashlytics instance used to log crashes.
         * @param firebaseCrashlytics The FirebaseCrashlytics instance.
         */
        fun setFirebaseCrashlytics(firebaseCrashlytics: FirebaseCrashlytics) = apply {
            this.mCrashlytics = firebaseCrashlytics
        }

        /**
         * Sets the behaviour for the WatchDog when a crash occurs.
         * @param behaviour The action to take upon a crash, such as restarting the app.
         */
        fun setBehaviour(behaviour: WatchDogBehaviour) = apply {
            this.mWatchDogBehaviour = behaviour
        }

        /**
         * Sets the title to display when an error or crash occurs.
         * @param title The optional title to show during a crash.
         */
        fun setTitle(title: String? = null) = apply {
            this.mTitle = title
        }

        /**
         * Sets the message to be displayed along with the title in case of a crash.
         * @param message The optional message to display when an error occurs.
         */
        fun setMessage(message: String? = null) = apply {
            this.mMessage = message
        }

        /**
         * Builds and returns a WatchDog instance with the specified configurations.
         * This method creates the WatchDog instance and initializes all configurations from the builder.
         * @return A WatchDog instance configured with the options set in the builder.
         */

        fun build(): WatchDogBuilder {
            return WatchDogImp(
                WatchDogContext.Builder()
                    .setContext(mContext) // Set the context for the WatchDog.
                    .setActivity(mActivity) // Set the activity to monitor.
                    .setEventListener(mEventListener) // Set the event listener (optional).
                    .setFirebaseCrashlytics(mCrashlytics) // Set the FirebaseCrashlytics instance for crash logging.
                    .setBehaviour(mWatchDogBehaviour) // Set the behaviour on crash (restart app, etc.).
                    .setTitle(mTitle) // Set the crash title (optional).
                    .setMessage(mMessage) // Set the crash message (optional).
                    .build() // Build the WatchDogContext.
                    .run() // Run the WatchDog service after building the context.
            )
        }
    }

}
