package com.yujin.record.feature.calendar

import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlinx.datetime.number

fun YearMonth.firstDay(): LocalDate = LocalDate(year, month, 1)

fun YearMonth.toDisplayString(): String = "%04d.%02d".format(year, month.number)