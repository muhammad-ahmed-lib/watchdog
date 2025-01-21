package com.ask.technologies.sample


/**
 * Abstract class representing a builder for the WatchDog service.
 * This class provides the structure for starting and stopping the WatchDog service.
 * Concrete implementations must define how to manage the WatchDog service.
 */
abstract class WatchDogBuilder {

    /**
     * The instance of the BloodhoundWatchDog that monitors and handles the WatchDog service.
     * This could be an implementation that provides specific behavior for crash monitoring.
     */
    abstract var mBloodhoundMonitor: BloodhoundWatchDog?

    /**
     * Starts the WatchDog service.
     * Concrete implementations should define the actual start logic for monitoring the service.
     */
    abstract fun startService()

    /**
     * Stops the WatchDog service.
     * Concrete implementations should define the actual stop logic for the monitoring service.
     */
    abstract fun stopService()
}
