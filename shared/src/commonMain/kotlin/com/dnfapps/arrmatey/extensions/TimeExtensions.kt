package com.dnfapps.arrmatey.extensions

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

fun LocalDate.isToday(timeZone: TimeZone = TimeZone.currentSystemDefault()): Boolean {
    val today = Clock.System.todayIn(timeZone)
    return this == today
}

fun LocalDate.isToday() = isToday(TimeZone.currentSystemDefault())

fun LocalDate.isAfterToday(timeZone: TimeZone = TimeZone.currentSystemDefault()): Boolean {
    val today = Clock.System.todayIn(timeZone)
    return this > today
}

fun LocalDate.isAfterToday() = isAfterToday(TimeZone.currentSystemDefault())

fun LocalDate.isTodayOrAfter(timeZone: TimeZone = TimeZone.currentSystemDefault()): Boolean {
    val today = Clock.System.todayIn(timeZone)
    return this >= today
}

fun LocalDate.isTodayOrAfter(): Boolean = isTodayOrAfter(timeZone = TimeZone.currentSystemDefault())

fun LocalDate.isTodayOrBefore(timeZone: TimeZone = TimeZone.currentSystemDefault()): Boolean {
    val today = Clock.System.todayIn(timeZone)
    return this <= today
}

fun LocalDate.isTodayOrBefore(): Boolean = isTodayOrBefore(TimeZone.currentSystemDefault())

fun LocalDate.isBeforeToday(timeZone: TimeZone = TimeZone.currentSystemDefault()): Boolean {
    val today = Clock.System.todayIn(timeZone)
    return this < today
}

fun LocalDate.isBeforeToday(): Boolean = isBeforeToday(TimeZone.currentSystemDefault())