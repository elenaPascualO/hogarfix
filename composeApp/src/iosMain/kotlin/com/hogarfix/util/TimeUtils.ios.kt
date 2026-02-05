package com.hogarfix.util

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

actual fun currentInstant(): Instant {
    val epochSeconds = NSDate().timeIntervalSince1970
    return Instant.fromEpochSeconds(epochSeconds.toLong(), ((epochSeconds % 1) * 1_000_000_000).toInt())
}

actual fun currentDate(): LocalDate =
    currentInstant().toLocalDateTime(TimeZone.currentSystemDefault()).date
