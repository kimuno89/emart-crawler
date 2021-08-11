package yhkim.crawler.utils

import java.io.PrintWriter
import java.io.StringWriter

class ExceptionTraceToString {
    companion object {
        fun toString(e: Exception): String {
            val errors = StringWriter()
            e.printStackTrace(PrintWriter(errors))

            return errors.toString()
        }
    }
}