package com.yujin.record.calendar

import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlinx.datetime.atTime
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toKotlinTimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.ZoneId
import kotlin.time.Clock
import kotlin.time.Instant

private val timeZone = ZoneId.systemDefault().toKotlinTimeZone()

fun YearMonth.firstDay(): LocalDate = LocalDate(year, month, 1)

fun YearMonth.toDisplayString(): String = "%04d.%02d".format(year, month.number)

fun LocalDate.toYearMonth(): YearMonth = YearMonth(year, month)

// set instant to the current time
fun LocalDate.toInstant(): Instant = atTime(Clock.System.now().toLocalDateTime(timeZone).time)
    .toInstant(timeZone)

fun Instant.toYearMonth(): YearMonth = toLocalDateTime(timeZone).date.toYearMonth()

fun Instant.toLocalDate(): LocalDate = toLocalDateTime(timeZone).date