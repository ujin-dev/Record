package com.yujin.record.ui

import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.time.Instant
import kotlin.time.toJavaInstant


private val pattern = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")

fun Instant.toDisplayString(): String = pattern
    .withZone(ZoneId.systemDefault())
    .format(toJavaInstant())