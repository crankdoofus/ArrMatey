package com.dnfapps.arrmatey.utils

import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

actual fun getCurrentSystemTimeMillis(): Long {
    return (NSDate().timeIntervalSince1970 * 1000).toLong()
}