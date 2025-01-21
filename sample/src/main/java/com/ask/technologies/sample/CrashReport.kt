package com.ask.technologies.sample

/**
 * Data class that represents a crash report containing details about the crash.
 * This report includes information about the activity, thread, exception, stack trace,
 * and package-level tracing to help in diagnosing and resolving issues.
 *
 * @property expectedActivity The name of the activity where the crash occurred (nullable).
 * @property expectedThread The name of the thread on which the crash occurred (nullable).
 * @property expectedException The type of exception that caused the crash (nullable).
 * @property packageTracer A package tracer that provides details about the code path
 *                         leading to the crash (includes method and line numbers).
 * @property stackDetails A detailed stack trace of the crash, including method calls
 *                        and line numbers, which helps in understanding the sequence of events.
 */
data class CrashReport(
    val expectedActivity: String?,  // The name of the activity where the crash occurred (nullable)
    val expectedThread: String?,    // The name of the thread where the crash occurred (nullable)
    val expectedException: String?, // The type of exception that caused the crash (nullable)
    val packageTracer: PackageTracer, // The tracer that helps pinpoint where in the package the crash happened
    val stackDetails: String // The full stack trace of the exception, useful for debugging the cause of the crash
)
