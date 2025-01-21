package com.ask.technologies.sample


/**
 * WatchDogImp is a concrete implementation of the abstract class `WatchDogBuilder`.
 * It is responsible for managing the `BloodhoundWatchDog` service, specifically starting
 * and stopping the service using the provided `BloodhoundWatchDog` instance.
 */
class WatchDogImp(override var mBloodhoundMonitor: BloodhoundWatchDog?) : WatchDogBuilder() {

    /**
     * Starts the Bloodhound service by invoking `startService` on the `mBloodhoundMonitor` instance.
     * If `mBloodhoundMonitor` is null, the service is not started.
     */
    override fun startService() {
        // Start the Bloodhound monitoring service
        mBloodhoundMonitor?.startService()
    }

    /**
     * Stops the Bloodhound service by invoking `stopService` on the `mBloodhoundMonitor` instance.
     * If `mBloodhoundMonitor` is null, the service is not stopped.
     */
    override fun stopService() {
        // Stop the Bloodhound monitoring service
        mBloodhoundMonitor?.stopService()
    }
}
