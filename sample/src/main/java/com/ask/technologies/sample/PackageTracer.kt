package com.ask.technologies.sample

/**
 * Data class representing a package tracer, which holds information about the line numbers
 * and method details within the code path leading to the crash. This helps in pinpointing the
 * exact locations where the crash occurred.
 *
 * @property lineNumbers A list of all line numbers from the stack trace that were part of the crash path.
 * @property methodDetails A list of method details, each containing the method name, file name,
 *                         and line number from the stack trace.
 */
data class PackageTracer(
    val lineNumbers: List<Int>, // List of all line numbers related to the crash path.
    val methodDetails: List<MethodDetail> // List of method details including method name, file name, and line number.
)

/**
 * Data class representing the details of a method within the code stack trace.
 * This includes the method name, the file in which it resides, and the line number where it was called.
 *
 * @property methodName The name of the method where the crash occurred.
 * @property fileName The name of the file where the method is located.
 * @property lineNumber The line number in the file where the method was called.
 */
data class MethodDetail(
    val methodName: String, // The name of the method where the crash occurred.
    val fileName: String,   // The file where the method is located.
    val lineNumber: Int     // The line number where the method was invoked in the file.
)
