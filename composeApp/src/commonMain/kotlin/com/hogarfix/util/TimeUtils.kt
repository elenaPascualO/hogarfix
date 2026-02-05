package com.hogarfix.util

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

expect fun currentInstant(): Instant

expect fun currentDate(): LocalDate
