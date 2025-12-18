package com.dnfapps.arrmatey.utils

infix fun <T> Boolean.thenGet(result: T): T? {
    return if (this) result else null
}