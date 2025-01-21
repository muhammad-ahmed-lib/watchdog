package com.ask.technologies.sample


/**
 * Abstract class WatchDogEventListener is used to define a listener interface
 * for handling error events captured by the WatchDog service.
 * Implement this class to handle error reports, such as crash reports, in your application.
 */
abstract class WatchDogEventListener {

    /**
     * Called when an error is captured by the WatchDog service.
     * This method is invoked with a `CrashReport` containing details about the error.
     *
     * @param report The crash report containing information about the error.
     */
    abstract fun onErrorCaptured(report: CrashReport)
}
