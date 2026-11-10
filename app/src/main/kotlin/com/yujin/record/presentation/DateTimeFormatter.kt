package com.yujin.record.presentation

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

@OptIn(FormatStringsInDatetimeFormats::class)
fun Instant.toDisplayString(): String =
    toLocalDateTime(TimeZone.currentSystemDefault())
        .format(LocalDateTime.Format {
            byUnicodePattern("yyyy.MM.dd HH:mm")
        })

@OptIn(FormatStringsInDatetimeFormats::class)
fun Instant.toYearString(): String =
    toLocalDateTime(TimeZone.currentSystemDefault())
        .format(LocalDateTime.Format {
            byUnicodePattern("yyyy")
        })

@OptIn(FormatStringsInDatetimeFormats::class)
fun Instant.toMonthDateString(): String =
    toLocalDateTime(TimeZone.currentSystemDefault())
        .format(LocalDateTime.Format {
            byUnicodePattern("MM.dd")
        })

@OptIn(FormatStringsInDatetimeFormats::class)
fun LocalDate.toDisplayString(): String = format(LocalDate.Format {
    byUnicodePattern("yy.MM.dd")
})