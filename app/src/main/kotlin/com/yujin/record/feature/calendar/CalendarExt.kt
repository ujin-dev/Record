package com.yujin.record.feature.calendar

import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlinx.datetime.number
import kotlinx.datetime.toKotlinTimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.ZoneId
import kotlin.time.Instant

fun YearMonth.firstDay(): LocalDate = LocalDate(year, month, 1)

fun YearMonth.toDisplayString(): String = "%04d.%02d".format(year, month.number)

fun LocalDate.toYearMonth(): YearMonth = YearMonth(year, month)

fun Instant.toYearMonth(): YearMonth =
    toLocalDateTime(ZoneId.systemDefault().toKotlinTimeZone()).date.toYearMonth()

fun Instant.toLocalDate(): LocalDate =
    toLocalDateTime(ZoneId.systemDefault().toKotlinTimeZone()).date