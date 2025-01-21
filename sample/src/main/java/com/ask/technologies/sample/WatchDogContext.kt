package com.ask.technologies.sample

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics

/**
 * WatchDogContext is a class that holds the context, activity, event listener,
 * crashlytics, and behavior for the WatchDog service. It provides a builder pattern
 * for configuring and creating an instance of WatchDogContext with the necessary
 * components.
 */
class WatchDogContext private constructor(
    private val context: Context,
    private val activity: Activity?,
    private val watchDogEventListener: WatchDogEventListener?,
    private val crashlytics: FirebaseCrashlytics,
    private val behaviour: WatchDogBehaviour,
    private var title: String? = null,
    private var message: String? = null
) {

    /**
     * Builder class for constructing an instance of WatchDogContext.
     * This class provides methods to set the context, activity, event listener,
     * crashlytics, behavior, title, and message for the WatchDog service.
     */
    class Builder {
        private val TAG = "WatchDogContextInfo"
        private lateinit var mContext: Context
        private var mActivity: Activity? = null
        private var mTitle: String? = null
        private var mMessage: String? = null
        private var mWatchDogBehaviour = WatchDogBehaviour.RESTART_APP
        private lateinit var mCrashlytics: FirebaseCrashlytics
        private var mEventListener: WatchDogEventListener? = null

        /**
         * Sets the context for the WatchDog service.
         */
        fun setContext(context: Context) = apply {
            this.mContext = context
            Log.d(TAG, "setContext: $context")
        }

        /**
         * Sets the activity for the WatchDog service.
         */
        fun setActivity(activity: Activity?) = apply {
            this.mActivity = activity
            Log.d(TAG, "setActivity: $activity") // Log actual activity
        }

        /**
         * Sets the event listener for the WatchDog service.
         */
        fun setEventListener(eventListener: WatchDogEventListener? = null) = apply {
            this.mEventListener = eventListener
            Log.d(TAG, "setEventListener: $eventListener")
        }

        /**
         * Sets the Firebase Crashlytics instance for the WatchDog service.
         */
        fun setFirebaseCrashlytics(firebaseCrashlytics: FirebaseCrashlytics) = apply {
            this.mCrashlytics = firebaseCrashlytics
            Log.d(TAG, "setFirebaseCrashlytics: $firebaseCrashlytics")
        }

        /**
         * Sets the behavior for the WatchDog service (e.g., restarting app or exiting).
         */
        fun setBehaviour(behaviour: WatchDogBehaviour) = apply {
            this.mWatchDogBehaviour = behaviour
            Log.d(TAG, "setBehaviour: $behaviour")
        }

        /**
         * Sets the title for the WatchDog service.
         */
        fun setTitle(title: String? = null) = apply {
            this.mTitle = title
            Log.d(TAG, "setTitle: $title")
        }

        /**
         * Sets the message for the WatchDog service.
         */
        fun setMessage(message: String? = null) = apply {
            this.mMessage = message
            Log.d(TAG, "setMessage: $message")
        }

        /**
         * Builds and returns a WatchDogContext instance with the configured parameters.
         * Ensures that the activity is not null before proceeding.
         */
        fun build(): WatchDogContext {
            Log.d(TAG, "build: Activity is $mActivity")
            requireNotNull(mActivity) { "Activity must not be null in WatchDogContext" } // Ensure activity is not null
            return WatchDogContext(
                context = mContext,
                activity = mActivity,
                watchDogEventListener = mEventListener,
                crashlytics = mCrashlytics,
                behaviour = mWatchDogBehaviour,
                title = mTitle,
                message = mMessage,
            )
        }
    }

    /**
     * Executes the BloodhoundWatchDog service with the provided WatchDogContext configurations.
     */
    fun run(): BloodhoundWatchDog = BloodhoundWatchDogImp(
        context = context,
        activity = activity,
        eventListener = watchDogEventListener,
        mFirebaseCrashlytics = crashlytics,
        behaviour = behaviour,
        title = title,
        message = message,
    )
}
