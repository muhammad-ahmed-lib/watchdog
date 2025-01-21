package com.ask.technologies.sample

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.firebase.crashlytics.FirebaseCrashlytics

/**
 * The WatchDogActivityBuilder is responsible for monitoring the lifecycle of activities in the app.
 * It ensures that WatchDog starts and tracks the activity lifecycle and reports any crashes or issues.
 * This class implements ActivityLifecycleCallbacks and DefaultLifecycleObserver to handle activity events.
 *
 * @author Muhammad Ahmed (Android Engineer & Library Author)
 *
 */

class WatchDogActivityBuilder constructor(
    private val appContext: Application, // Application context for accessing app-wide resources
    private val watchDogEventListener: WatchDogEventListener, // Listener to handle WatchDog events
    private val title: String, // Title to display when a crash occurs
    private val message: String, // Message to display when a crash occurs
    private val behaviour: WatchDogBehaviour, // The behaviour to execute on crash (e.g., restart app)
    private val crashlytics: FirebaseCrashlytics // Firebase Crashlytics instance for logging crashes
) : Application.ActivityLifecycleCallbacks, // Implements activity lifecycle callbacks
    DefaultLifecycleObserver { // Implements lifecycle observer to observe the application's lifecycle

    private val TAG = "watchDogActivityInfo" // Tag for logging purposes

    init {
        // Adding the current WatchDogActivityBuilder as a lifecycle observer to track app lifecycle
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        // Registering this builder to listen to activity lifecycle callbacks
        appContext.registerActivityLifecycleCallbacks(this)
    }

    /**
     * Starts the WatchDog service for the given activity.
     * Initializes WatchDog with the configured parameters and starts the service.
     * @param activity The activity being monitored.
     */
    private fun start(activity: Activity) {
        WatchDog
            .Builder()
            .setContext(appContext) // Set the application context
            .setTitle(title) // Set the crash title
            .setActivity(activity) // Set the activity being monitored
            .setMessage(message) // Set the crash message
            .setBehaviour(behaviour) // Set the crash behaviour (e.g., restart app)
            .setEventListener(watchDogEventListener) // Set the event listener for handling events
            .setFirebaseCrashlytics(crashlytics) // Set FirebaseCrashlytics for logging crashes
            .build() // Build the WatchDog instance
            .startService() // Start the WatchDog service
    }

    // Activity lifecycle callback methods

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        // No specific action needed for onStart of lifecycle observer
    }

    /**
     * This method is called when an activity is created.
     * It starts the WatchDog service for the created activity.
     * @param activity The created activity.
     * @param savedInstanceState The saved instance state of the activity.
     */
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log.d(TAG, "onActivityCreated: $activity")
        start(activity) // Start WatchDog service for the created activity
    }

    /**
     * This method is called when an activity becomes visible (started).
     * It starts the WatchDog service for the started activity.
     * @param activity The started activity.
     */
    override fun onActivityStarted(activity: Activity) {
        start(activity) // Start WatchDog service for the started activity
        Log.d(TAG, "onActivityStarted: ${activity.javaClass.simpleName}")
    }

    /**
     * This method is called when an activity is resumed (visible and interacting with the user).
     * It starts the WatchDog service for the resumed activity.
     * @param activity The resumed activity.
     */
    override fun onActivityResumed(activity: Activity) {
        start(activity) // Start WatchDog service for the resumed activity
    }

    /**
     * This method is called when an activity is paused (no longer interacting with the user).
     * It starts the WatchDog service for the paused activity.
     * @param activity The paused activity.
     */
    override fun onActivityPaused(activity: Activity) {
        start(activity) // Start WatchDog service for the paused activity
    }

    /**
     * This method is called when an activity is stopped (no longer visible to the user).
     * It starts the WatchDog service for the stopped activity.
     * @param activity The stopped activity.
     */
    override fun onActivityStopped(activity: Activity) {
        start(activity) // Start WatchDog service for the stopped activity
    }

    /**
     * This method is called to save the state of an activity.
     * It starts the WatchDog service when the state is being saved.
     * @param activity The activity whose state is being saved.
     * @param outState The bundle containing the state of the activity.
     */
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        start(activity) // Start WatchDog service when the state is being saved
    }

    /**
     * This method is called when an activity is destroyed (removed from memory).
     * It starts the WatchDog service for the destroyed activity.
     * @param activity The destroyed activity.
     */
    override fun onActivityDestroyed(activity: Activity) {
        start(activity) // Start WatchDog service for the destroyed activity
    }
}
