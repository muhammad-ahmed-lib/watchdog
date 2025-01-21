package com.ask.technologies.sample

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

/**
 * BloodhoundServiceImp is an implementation of the BloodhoundService interface
 * that monitors and handles uncaught exceptions within the application.
 *
 * @property context The application context used for starting activities and services.
 * @property activity The activity context (optional).
 * @property watchDogEventListener Listener to capture error events.
 * @property crashlytics Instance of FirebaseCrashlytics for logging crashes.
 * @property behaviour The behaviour to apply when a crash is detected.
 * @property title The title for error messages.
 * @property message The detailed message for error notifications.
 */
class BloodhoundServiceImp(
    override val context: Context,
    override val activity: Activity?,
    override val watchDogEventListener: WatchDogEventListener?,
    override val crashlytics: FirebaseCrashlytics,
    override val behaviour: WatchDogBehaviour,
    override var title: String?,
    override var message: String?
) : BloodhoundService(), Thread.UncaughtExceptionHandler {

    // Coroutine exception handler to manage uncaught coroutine exceptions
    private val mainCoroutineException = CoroutineExceptionHandler { _, throwable ->
        uncaughtException(Thread.currentThread(), throwable)
    }

    private var shouldLogException = true // Flag to prevent repeated logging of the same exception
    private val TAG = "BloodhoundService" // Log tag for the service
    private var trackingThread: Thread? = null // Background thread to monitor application state
    private var isRunning = false // Indicates if the tracking thread is active
    private var elapsedDuration = 0 // Tracks time elapsed in monitoring
    private var stateTester = 0 // Tracks the state of the UI thread
    private val checkInterval = 200 // Interval for thread monitoring in milliseconds
    private val thresholdDuration = 3000L // Threshold for detecting unresponsive UI
    private val uiHandler = Handler(Looper.getMainLooper()) // Handler for UI thread operations

    /**
     * Connects and initializes the Bloodhound service by setting up the uncaught exception handler
     * and starting the application state monitoring thread.
     */
    override fun connect() {
        if (!isRunning) {
            initializeExceptionHandler()
            startTrackingThread()
        }
    }

    /**
     * Sets the default uncaught exception handler for the application.
     */
    private fun initializeExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    /**
     * Starts a background thread to monitor the application's main thread state.
     * Detects potential UI freezes or deadlocks and logs stack traces.
     */
    private fun startTrackingThread() {
        isRunning = true
        trackingThread = Thread {
            while (isRunning && !Thread.currentThread().isInterrupted) {
                elapsedDuration += checkInterval

                // Reset stateTester on the UI thread
                uiHandler.post {
                    stateTester = 0
                    elapsedDuration = 0
                }

                // Log thread traces if threshold duration is exceeded
                if (elapsedDuration >= thresholdDuration && stateTester == 1) {
                    val threadTrace = Looper.getMainLooper().thread.stackTrace
                    handleThreadTrace(threadTrace)
                }

                try {
                    Thread.sleep(checkInterval.toLong())
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                }

                if (stateTester == 0) {
                    stateTester = 1
                }
            }
        }.apply { start() }
    }

    /**
     * Logs the stack trace of the monitored thread.
     *
     * @param threadTrace The stack trace elements of the thread.
     */
    private fun handleThreadTrace(threadTrace: Array<StackTraceElement>) {
        Log.e(TAG, "Thread trace: ${threadTrace.joinToString()}")
    }

    /**
     * Stops the background tracking thread and releases resources.
     */
    private fun stopTrackingThread() {
        isRunning = false
        trackingThread?.interrupt()
        trackingThread = null
    }

    /**
     * Disconnects the Bloodhound service by stopping the monitoring thread.
     */
    override fun disconnect() {
        stopTrackingThread()
    }

    /**
     * Handles uncaught exceptions by generating a crash report and invoking the listener.
     *
     * @param thread The thread where the exception occurred.
     * @param throwable The exception that was thrown.
     */
    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        if (shouldLogException) {
            shouldLogException = false
            generateCrashReport(throwable, thread)
        }
    }

    /**
     * Generates a crash report and notifies the listener with the crash details.
     *
     * @param throwable The exception that caused the crash.
     * @param thread The thread where the exception occurred.
     */
    private fun generateCrashReport(throwable: Throwable, thread: Thread?) {
        val fullStack = throwable.stackTraceToString()
        val exceptionType = throwable::class.simpleName ?: "UnknownException"
        val activityName = activity?.javaClass?.simpleName ?: "UnknownActivity"
        val threadName = thread?.name ?: "UnknownThread"
        val packageName = activity?.packageName ?: "UnknownPackage"
        val packageTracer = extractPackageLineNumbers(fullStack, packageName)

        val report = CrashReport(
            expectedActivity = activityName,
            expectedThread = threadName,
            expectedException = exceptionType,
            packageTracer = packageTracer,
            stackDetails = fullStack
        )

        watchDogEventListener?.onErrorCaptured(report)
        navigateToCrashReportActivity()
        exitProcess(0)
    }

    /**
     * Navigates to the crash report activity to display detailed crash information.
     */
    private fun navigateToCrashReportActivity() {
        val intent = Intent(context, ReportCrashActivity::class.java).apply {
            putExtra("reportDetails", "Crash report details here")
        }
        context.startActivity(intent)
    }

    /**
     * Extracts line numbers from the stack trace that are specific to the application's package.
     *
     * @param stackTrace The full stack trace string.
     * @param packageName The package name of the application.
     * @return A PackageTracer object containing relevant stack trace details.
     */
    private fun extractPackageLineNumbers(stackTrace: String, packageName: String): PackageTracer {
        val regex = Regex("""$packageName\.([^(]+)\(([^:]+):(\d+)\)""")
        val methodDetails = regex.findAll(stackTrace)
            .mapNotNull { match ->
                val methodName = match.groups[1]?.value
                val fileName = match.groups[2]?.value
                val lineNumber = match.groups[3]?.value?.toInt()
                if (methodName != null && fileName != null && lineNumber != null) {
                    MethodDetail(methodName, fileName, lineNumber)
                } else null
            }.toList()

        val lineNumbers = methodDetails.map { it.lineNumber }
        return PackageTracer(lineNumbers, methodDetails)
    }

    /**
     * Launches a coroutine safely with error handling.
     *
     * @param dispatcher The coroutine dispatcher to use.
     * @param block The suspend block to execute within the coroutine.
     */
    fun launchSafeCoroutine(dispatcher: CoroutineDispatcher = Dispatchers.IO, block: suspend CoroutineScope.() -> Unit) {
        CoroutineScope(dispatcher + mainCoroutineException).launch {
            block()
        }
    }
}
