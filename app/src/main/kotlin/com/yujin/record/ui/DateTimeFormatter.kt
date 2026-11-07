package com.yujin.record.ui

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

@OptIn(FormatStringsInDatetimeFormats::class)
private val fullPattern = LocalDateTime.Format {
    byUnicodePattern("yyyy.MM.dd HH:mm:ss")
}

fun Instant.toDisplayString(): String =
    toLocalDateTime(TimeZone.currentSystemDefault())
        .format(fullPattern)

@OptIn(FormatStringsInDatetimeFormats::class)
private val simplePattern = LocalDate.Format {
    byUnicodePattern("yy.MM.dd")
}

fun LocalDate.toDisplayString(): String = format(simplePattern)