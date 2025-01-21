package com.ask.technologies.sample

/**
 * Enum class representing the possible behaviours for the WatchDog in case of a crash or issue.
 * The WatchDog can either restart the application or exit the application based on the selected behaviour.
 */
enum class WatchDogBehaviour {
    /**
     * Restart the application when a crash or issue is detected.
     * This is useful when you want the app to recover automatically after a crash.
     */
    RESTART_APP,

    /**
     * Exit the application when a crash or issue is detected.
     * This behaviour will terminate the app instead of trying to recover it.
     */
    EXIST_APP
}
