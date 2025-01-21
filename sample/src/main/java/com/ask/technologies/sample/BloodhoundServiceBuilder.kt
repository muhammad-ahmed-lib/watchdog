package com.ask.technologies.sample

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics




class BloodhoundServiceBuilder private constructor(
    private val context: Context,
    private val activity: Activity?,
    private val watchDogEventListener: WatchDogEventListener?,
    private val crashlytics: FirebaseCrashlytics,
    private val behaviour: WatchDogBehaviour,
    private var title: String? = null,
    private var message: String? = null
) {

    companion object {
        private const val TAG = "BloodhoundServiceBuilder"
    }

    /**
     * Builder class for creating an instance of BloodhoundServiceBuilder.
     *
     * Provides a fluent interface to configure and build the `BloodhoundServiceBuilder` instance
     * with various parameters like `Context`, `Activity`, `Crashlytics`, and event listeners.
     */
    internal class Init {
        private lateinit var mContext: Context
        private var mActivity: Activity? = null
        private var mTitle: String? = null
        private var mMessage: String? = null
        private var mWatchDogBehaviour = WatchDogBehaviour.RESTART_APP
        private lateinit var mCrashlytics: FirebaseCrashlytics
        private var mEventListener: WatchDogEventListener? = null

        /**
         * Sets the context to be used by the service.
         * @param context The application or activity context.
         * @return This builder instance.
         */
        fun setContext(context: Context) = apply {
            Log.d(TAG, "setContext: $context")
            this.mContext = context
        }

        /**
         * Sets the optional activity reference.
         * @param activity The current activity, if available.
         * @return This builder instance.
         */
        fun setActivity(activity: Activity?) = apply {
            Log.d(TAG, "setActivity: $activity")
            this.mActivity = activity
        }

        /**
         * Sets the event listener for monitoring custom events.
         * @param eventListener The event listener to be notified of watchdog events.
         * @return This builder instance.
         */
        fun setEventListener(eventListener: WatchDogEventListener? = null) = apply {
            Log.d(TAG, "setEventListener: $eventListener")
            this.mEventListener = eventListener
        }

        /**
         * Sets the FirebaseCrashlytics instance for logging crashes.
         * @param firebaseCrashlytics An initialized Crashlytics instance.
         * @return This builder instance.
         */
        fun setFirebaseCrashlytics(firebaseCrashlytics: FirebaseCrashlytics) = apply {
            Log.d(TAG, "setFirebaseCrashlytics: $firebaseCrashlytics")
            this.mCrashlytics = firebaseCrashlytics
        }

        /**
         * Sets the behavior for the watchdog service.
         * @param behaviour The desired behavior (e.g., restart app on crash).
         * @return This builder instance.
         */
        fun setBehaviour(behaviour: WatchDogBehaviour) = apply {
            Log.d(TAG, "setBehaviour: $behaviour")
            this.mWatchDogBehaviour = behaviour
        }

        /**
         * Sets a custom title for the watchdog dialog or notification.
         * @param title A string representing the title.
         * @return This builder instance.
         */
        fun setTitle(title: String? = null) = apply {
            Log.d(TAG, "setTitle: $title")
            this.mTitle = title
        }

        /**
         * Sets a custom message for the watchdog dialog or notification.
         * @param message A string representing the message.
         * @return This builder instance.
         */
        fun setMessage(message: String? = null) = apply {
            Log.d(TAG, "setMessage: $message")
            this.mMessage = message
        }

        /**
         * Builds and returns a configured instance of BloodhoundServiceBuilder.
         * Ensures that all required parameters are properly initialized.
         * @return A fully configured BloodhoundServiceBuilder instance.
         * @throws NullPointerException if required parameters are missing.
         */
        fun build(): BloodhoundServiceBuilder {
            Log.d(TAG, "Building BloodhoundServiceBuilder...")
            if (!::mCrashlytics.isInitialized) throw NullPointerException("firebaseCrashlytics must be non-null")
            return BloodhoundServiceBuilder(
                context = mContext,
                activity = mActivity,
                watchDogEventListener = mEventListener,
                behaviour = mWatchDogBehaviour,
                title = mTitle,
                message = mMessage,
                crashlytics = mCrashlytics
            ).also {
                Log.d(TAG, "BloodhoundServiceBuilder built successfully: $it")
            }
        }
    }

    /**
     * Creates and returns an instance of BloodhoundService.
     * This method initializes the actual service with all configured parameters.
     * @return A fully initialized BloodhoundService instance.
     */
    fun monitorService(): BloodhoundService {
        Log.d(TAG, "Creating BloodhoundService...")
        return BloodhoundServiceImp(
            context = context,
            activity = activity,
            watchDogEventListener = watchDogEventListener,
            crashlytics = crashlytics,
            behaviour = behaviour,
            title = title,
            message = message,
        ).also {
            Log.d(TAG, "BloodhoundService created: $it")
        }
    }
}
