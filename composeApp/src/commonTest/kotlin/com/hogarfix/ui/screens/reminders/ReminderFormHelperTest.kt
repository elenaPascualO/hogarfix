package com.hogarfix.ui.screens.reminders

import com.hogarfix.domain.model.Category
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ReminderFormHelperTest {

    // --- decomposeInterval tests ---

    @Test
    fun decomposeInterval_exactYears() {
        val (value, unit) = ReminderFormViewModel.decomposeInterval(365)
        assertEquals(1, value)
        assertEquals(IntervalUnit.YEARS, unit)
    }

    @Test
    fun decomposeInterval_multipleYears() {
        val (value, unit) = ReminderFormViewModel.decomposeInterval(1825)
        assertEquals(5, value)
        assertEquals(IntervalUnit.YEARS, unit)
    }

    @Test
    fun decomposeInterval_exactMonths() {
        val (value, unit) = ReminderFormViewModel.decomposeInterval(30)
        assertEquals(1, value)
        assertEquals(IntervalUnit.MONTHS, unit)
    }

    @Test
    fun decomposeInterval_multipleMonths() {
        val (value, unit) = ReminderFormViewModel.decomposeInterval(90)
        assertEquals(3, value)
        assertEquals(IntervalUnit.MONTHS, unit)
    }

    @Test
    fun decomposeInterval_exactWeeks() {
        val (value, unit) = ReminderFormViewModel.decomposeInterval(7)
        assertEquals(1, value)
        assertEquals(IntervalUnit.WEEKS, unit)
    }

    @Test
    fun decomposeInterval_multipleWeeks() {
        val (value, unit) = ReminderFormViewModel.decomposeInterval(14)
        assertEquals(2, value)
        assertEquals(IntervalUnit.WEEKS, unit)
    }

    @Test
    fun decomposeInterval_days() {
        val (value, unit) = ReminderFormViewModel.decomposeInterval(1)
        assertEquals(1, value)
        assertEquals(IntervalUnit.DAYS, unit)
    }

    @Test
    fun decomposeInterval_oddDays() {
        val (value, unit) = ReminderFormViewModel.decomposeInterval(15)
        assertEquals(15, value)
        assertEquals(IntervalUnit.DAYS, unit)
    }

    @Test
    fun decomposeInterval_6months() {
        val (value, unit) = ReminderFormViewModel.decomposeInterval(180)
        assertEquals(6, value)
        assertEquals(IntervalUnit.MONTHS, unit)
    }

    // --- IntervalUnit.toDays tests ---

    @Test
    fun intervalUnit_daysToDays() {
        assertEquals(5, IntervalUnit.DAYS.toDays(5))
    }

    @Test
    fun intervalUnit_weeksToDays() {
        assertEquals(14, IntervalUnit.WEEKS.toDays(2))
    }

    @Test
    fun intervalUnit_monthsToDays() {
        assertEquals(30, IntervalUnit.MONTHS.toDays(1))
    }

    @Test
    fun intervalUnit_yearsToDays() {
        assertEquals(365, IntervalUnit.YEARS.toDays(1))
    }

    // --- ReminderFormState.intervalDays tests ---

    @Test
    fun state_intervalDays_oneMonth() {
        val state = ReminderFormState(intervalValue = 1, intervalUnit = IntervalUnit.MONTHS)
        assertEquals(30, state.intervalDays)
    }

    @Test
    fun state_intervalDays_oneDay() {
        val state = ReminderFormState(intervalValue = 1, intervalUnit = IntervalUnit.DAYS)
        assertEquals(1, state.intervalDays)
    }

    @Test
    fun state_intervalDays_threeWeeks() {
        val state = ReminderFormState(intervalValue = 3, intervalUnit = IntervalUnit.WEEKS)
        assertEquals(21, state.intervalDays)
    }

    @Test
    fun state_intervalDays_fiveYears() {
        val state = ReminderFormState(intervalValue = 5, intervalUnit = IntervalUnit.YEARS)
        assertEquals(1825, state.intervalDays)
    }

    // --- ReminderFormState.isValid tests ---

    @Test
    fun state_isValid_allFieldsPresent() {
        val state = ReminderFormState(
            title = "Test",
            category = Category.HVAC,
            intervalValue = 1,
            nextDueDate = LocalDate(2025, 6, 1)
        )
        assertTrue(state.isValid)
    }

    @Test
    fun state_isValid_blankTitle() {
        val state = ReminderFormState(
            title = "  ",
            category = Category.HVAC,
            intervalValue = 1,
            nextDueDate = LocalDate(2025, 6, 1)
        )
        assertFalse(state.isValid)
    }

    @Test
    fun state_isValid_noCategory() {
        val state = ReminderFormState(
            title = "Test",
            category = null,
            intervalValue = 1,
            nextDueDate = LocalDate(2025, 6, 1)
        )
        assertFalse(state.isValid)
    }

    @Test
    fun state_isValid_zeroInterval() {
        val state = ReminderFormState(
            title = "Test",
            category = Category.HVAC,
            intervalValue = 0,
            nextDueDate = LocalDate(2025, 6, 1)
        )
        assertFalse(state.isValid)
    }

    @Test
    fun state_isValid_noDate() {
        val state = ReminderFormState(
            title = "Test",
            category = Category.HVAC,
            intervalValue = 1,
            nextDueDate = null
        )
        assertFalse(state.isValid)
    }

    // --- ReminderFormState.isEditMode tests ---

    @Test
    fun state_isEditMode_withId() {
        val state = ReminderFormState(id = 1L)
        assertTrue(state.isEditMode)
    }

    @Test
    fun state_isEditMode_withoutId() {
        val state = ReminderFormState(id = null)
        assertFalse(state.isEditMode)
    }

    // --- decomposeInterval roundtrip with IntervalUnit.toDays ---

    @Test
    fun decomposeInterval_roundtrip_allUnits() {
        val cases = listOf(
            1 to IntervalUnit.DAYS,
            7 to IntervalUnit.WEEKS,
            30 to IntervalUnit.MONTHS,
            365 to IntervalUnit.YEARS
        )
        for ((expectedValue, expectedUnit) in cases) {
            val days = expectedUnit.toDays(expectedValue)
            val (value, unit) = ReminderFormViewModel.decomposeInterval(days)
            assertEquals(expectedValue, value, "roundtrip failed for $expectedUnit")
            assertEquals(expectedUnit, unit, "roundtrip failed for $expectedUnit")
        }
    }
}
